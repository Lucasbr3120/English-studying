package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.local.PhraseHistoryEntity
import com.example.data.local.PrepopulatedYouTubeStudy
import com.example.data.local.UserMistakeEntity
import com.example.data.local.UserStatsEntity
import com.example.data.local.VocabularyEntity
import com.example.data.model.CefrLevel
import com.example.data.model.YouTubeCategory
import com.example.data.model.YouTubeSearchFilter
import com.example.data.model.YouTubeStudyPhrase
import com.example.data.model.YouTubeVideoItem
import com.example.data.remote.YouTubeApiService
import com.example.data.remote.YouTubeBackendApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

sealed class YouTubeSearchResult {
    data class Success(val videos: List<YouTubeVideoItem>, val isLiveApi: Boolean, val notice: String? = null) : YouTubeSearchResult()
    data class Error(val message: String, val isQuotaExceeded: Boolean = false, val fallbackVideos: List<YouTubeVideoItem> = emptyList()) : YouTubeSearchResult()
}

class YouTubeRepository(
    private val db: AppDatabase,
    private val apiService: YouTubeApiService = YouTubeApiService.create()
) {

    private val backendUrl: String
        get() {
            return try {
                val field = BuildConfig::class.java.getField("BACKEND_API_URL")
                (field.get(null) as? String)?.trim() ?: ""
            } catch (e: Exception) {
                ""
            }
        }

    private val backendApiService: YouTubeBackendApiService? by lazy {
        if (backendUrl.isNotBlank() && !backendUrl.equals("MY_BACKEND_API_URL", ignoreCase = true)) {
            try {
                YouTubeBackendApiService.create(backendUrl)
            } catch (e: Exception) {
                Log.w("YouTubeRepository", "Could not initialize backend service for URL: $backendUrl", e)
                null
            }
        } else {
            null
        }
    }

    private val apiKey: String
        get() {
            return try {
                val buildKey = BuildConfig.YOUTUBE_API_KEY
                if (buildKey.isNotBlank() && buildKey != "MY_YOUTUBE_API_KEY") {
                    buildKey
                } else {
                    System.getenv("YOUTUBE_API_KEY") ?: buildKey
                }
            } catch (e: Exception) {
                System.getenv("YOUTUBE_API_KEY") ?: ""
            }
        }

    suspend fun searchYouTubeVideos(filter: YouTubeSearchFilter): YouTubeSearchResult = withContext(Dispatchers.IO) {
        // 1. If backend server is available, query through the secure server proxy
        val backend = backendApiService
        if (backend != null) {
            try {
                val response = backend.searchVideos(
                    query = filter.query,
                    category = filter.category.name,
                    level = filter.level?.code,
                    creativeCommonsOnly = filter.creativeCommonsOnly,
                    maxResults = 15
                )
                if (response.isSuccessful && response.body()?.items?.isNotEmpty() == true) {
                    val serverVideos = response.body()?.items.orEmpty().mapNotNull { item ->
                        val videoId = item.id?.videoId ?: return@mapNotNull null
                        val snippet = item.snippet ?: return@mapNotNull null
                        val authorizedSet = PrepopulatedYouTubeStudy.getStudySetForVideo(videoId)
                        val suggestedLevel = filter.level ?: estimateCefrLevel(snippet.title.orEmpty(), snippet.description.orEmpty())

                        YouTubeVideoItem(
                            id = videoId,
                            title = cleanHtmlEntities(snippet.title.orEmpty()),
                            channelTitle = cleanHtmlEntities(snippet.channelTitle.orEmpty()),
                            description = cleanHtmlEntities(snippet.description.orEmpty()),
                            thumbnailUrl = snippet.thumbnails?.high?.url
                                ?: snippet.thumbnails?.medium?.url
                                ?: snippet.thumbnails?.default?.url
                                ?: "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=600",
                            durationFormatted = "3:30",
                            durationSeconds = 210,
                            suggestedLevel = suggestedLevel,
                            hasClosedCaptions = true,
                            isEmbeddable = true,
                            license = if (filter.creativeCommonsOnly) "creativeCommon" else "youtube",
                            publishedAt = snippet.publishedAt.orEmpty().take(10),
                            category = filter.category,
                            authorizedStudySet = authorizedSet
                        )
                    }
                    if (serverVideos.isNotEmpty()) {
                        return@withContext YouTubeSearchResult.Success(
                            videos = serverVideos,
                            isLiveApi = true
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("YouTubeRepository", "Backend query failed, falling back: ${e.message}")
            }
        }

        // 2. Direct API query if API key is provided
        val currentKey = apiKey.trim()
        val isPlaceholder = currentKey.isEmpty() || currentKey == "MY_YOUTUBE_API_KEY"

        if (isPlaceholder) {
            val filteredCurated = filterCuratedVideos(filter)
            return@withContext YouTubeSearchResult.Success(
                videos = filteredCurated,
                isLiveApi = false
            )
        }

        try {
            val levelQuery = filter.level?.let { "English ${it.code}" } ?: "English conversation"
            val baseQuery = filter.query.trim().ifEmpty { filter.category.searchKeywords }
            val fullSearchQuery = "$baseQuery $levelQuery dialogue subtitle"

            val licenseParam = if (filter.creativeCommonsOnly) "creativeCommon" else null

            val response = apiService.searchVideos(
                part = "snippet",
                type = "video",
                videoEmbeddable = "true",
                videoCaption = "closedCaption",
                relevanceLanguage = "en",
                safeSearch = "strict",
                videoLicense = licenseParam,
                query = fullSearchQuery,
                maxResults = 12,
                apiKey = currentKey
            )

            if (!response.isSuccessful) {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: ""
                val isQuota = errorCode == 403 && (errorBody.contains("quotaExceeded", ignoreCase = true) || errorBody.contains("rateLimitExceeded", ignoreCase = true))

                Log.e("YouTubeRepository", "YouTube API Error: $errorCode $errorBody")

                return@withContext YouTubeSearchResult.Success(
                    videos = filterCuratedVideos(filter),
                    isLiveApi = false
                )
            }

            val searchBody = response.body()
            val items = searchBody?.items.orEmpty()
            val videoIds = items.mapNotNull { it.id?.videoId }.filter { it.isNotBlank() }

            if (videoIds.isEmpty()) {
                val fallback = filterCuratedVideos(filter)
                return@withContext YouTubeSearchResult.Success(
                    videos = fallback,
                    isLiveApi = false
                )
            }

            // Fetch video details (duration, embeddable status, license)
            val detailsMap = try {
                val detailsResponse = apiService.getVideoDetails(
                    part = "snippet,contentDetails,status",
                    videoIds = videoIds.joinToString(","),
                    apiKey = currentKey
                )
                if (detailsResponse.isSuccessful) {
                    detailsResponse.body()?.items?.associateBy { it.id.orEmpty() }.orEmpty()
                } else {
                    emptyMap()
                }
            } catch (e: Exception) {
                emptyMap()
            }

            val resultVideos = items.mapNotNull { item ->
                val videoId = item.id?.videoId ?: return@mapNotNull null
                val snippet = item.snippet ?: return@mapNotNull null
                val details = detailsMap[videoId]

                val isEmbeddable = details?.status?.embeddable ?: true
                if (!isEmbeddable) return@mapNotNull null

                val license = details?.status?.license ?: if (filter.creativeCommonsOnly) "creativeCommon" else "youtube"
                val rawDuration = details?.contentDetails?.duration
                val parsedDuration = parseIsoDuration(rawDuration)

                val suggestedLevel = filter.level ?: estimateCefrLevel(snippet.title.orEmpty(), snippet.description.orEmpty())

                val authorizedSet = PrepopulatedYouTubeStudy.getStudySetForVideo(videoId)

                YouTubeVideoItem(
                    id = videoId,
                    title = cleanHtmlEntities(snippet.title.orEmpty()),
                    channelTitle = cleanHtmlEntities(snippet.channelTitle.orEmpty()),
                    description = cleanHtmlEntities(snippet.description.orEmpty()),
                    thumbnailUrl = snippet.thumbnails?.high?.url
                        ?: snippet.thumbnails?.medium?.url
                        ?: snippet.thumbnails?.default?.url
                        ?: "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=600",
                    durationFormatted = parsedDuration.first,
                    durationSeconds = parsedDuration.second,
                    suggestedLevel = suggestedLevel,
                    hasClosedCaptions = true,
                    isEmbeddable = true,
                    license = license,
                    publishedAt = snippet.publishedAt.orEmpty().take(10),
                    category = filter.category,
                    authorizedStudySet = authorizedSet
                )
            }

            if (resultVideos.isEmpty()) {
                return@withContext YouTubeSearchResult.Success(
                    videos = filterCuratedVideos(filter),
                    isLiveApi = false
                )
            }

            YouTubeSearchResult.Success(
                videos = resultVideos,
                isLiveApi = true
            )

        } catch (e: Exception) {
            Log.e("YouTubeRepository", "Search exception", e)
            YouTubeSearchResult.Success(
                videos = filterCuratedVideos(filter),
                isLiveApi = false
            )
        }
    }

    private fun filterCuratedVideos(filter: YouTubeSearchFilter): List<YouTubeVideoItem> {
        return PrepopulatedYouTubeStudy.curatedVideos.filter { video ->
            val matchQuery = filter.query.isBlank() ||
                    video.title.contains(filter.query, ignoreCase = true) ||
                    video.description.contains(filter.query, ignoreCase = true)
            val matchLevel = filter.level == null || video.suggestedLevel == filter.level
            val matchCC = !filter.creativeCommonsOnly || video.isCreativeCommons
            val matchCat = video.category == filter.category || filter.category == YouTubeCategory.CONVERSATION
            matchQuery && matchLevel && matchCC && (matchCat || filter.query.isNotBlank())
        }.ifEmpty { PrepopulatedYouTubeStudy.curatedVideos }
    }

    suspend fun getVideoById(videoId: String): YouTubeVideoItem? = withContext(Dispatchers.IO) {
        val curated = PrepopulatedYouTubeStudy.curatedVideos.firstOrNull { it.id == videoId }
        if (curated != null) return@withContext curated

        // If not in curated, attempt live API details
        val currentKey = apiKey.trim()
        if (currentKey.isNotEmpty() && currentKey != "MY_YOUTUBE_API_KEY") {
            try {
                val response = apiService.getVideoDetails(
                    part = "snippet,contentDetails,status",
                    videoIds = videoId,
                    apiKey = currentKey
                )
                if (response.isSuccessful) {
                    val item = response.body()?.items?.firstOrNull()
                    if (item != null && item.snippet != null) {
                        val parsedDuration = parseIsoDuration(item.contentDetails?.duration)
                        return@withContext YouTubeVideoItem(
                            id = videoId,
                            title = cleanHtmlEntities(item.snippet.title.orEmpty()),
                            channelTitle = cleanHtmlEntities(item.snippet.channelTitle.orEmpty()),
                            description = cleanHtmlEntities(item.snippet.description.orEmpty()),
                            thumbnailUrl = item.snippet.thumbnails?.high?.url
                                ?: item.snippet.thumbnails?.medium?.url
                                ?: item.snippet.thumbnails?.default?.url
                                ?: "",
                            durationFormatted = parsedDuration.first,
                            durationSeconds = parsedDuration.second,
                            suggestedLevel = estimateCefrLevel(item.snippet.title.orEmpty(), item.snippet.description.orEmpty()),
                            hasClosedCaptions = true,
                            isEmbeddable = item.status?.embeddable ?: true,
                            license = item.status?.license ?: "youtube",
                            publishedAt = item.snippet.publishedAt.orEmpty().take(10),
                            authorizedStudySet = PrepopulatedYouTubeStudy.getStudySetForVideo(videoId)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("YouTubeRepository", "Failed to fetch video details for $videoId", e)
            }
        }

        null
    }

    suspend fun recordYouTubeStudyResult(
        videoId: String,
        phrase: YouTubeStudyPhrase,
        wasContractionCorrect: Boolean,
        wasTranslationCorrect: Boolean,
        wasComprehensionCorrect: Boolean
    ) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val allCorrect = wasContractionCorrect && wasTranslationCorrect && wasComprehensionCorrect

        // 1. Update User Stats
        val stats = db.userStatsDao().getUserStatsDirect() ?: UserStatsEntity()
        val updatedStats = stats.copy(
            totalPhrasesStudied = stats.totalPhrasesStudied + 1,
            totalCorrect = stats.totalCorrect + (if (allCorrect) 1 else 0),
            totalErrors = stats.totalErrors + (if (!allCorrect) 1 else 0),
            lastStudyDateMillis = now
        )
        db.userStatsDao().saveUserStats(updatedStats)

        // 2. Record Mistakes if any
        phrase.contractionsList.forEach { contraction ->
            val tag = "${contraction.fullForm} -> ${contraction.contractedForm}"
            val existing = db.userMistakeDao().getMistakeByTag(tag)
            if (!wasContractionCorrect) {
                if (existing != null) {
                    db.userMistakeDao().incrementMistake(tag, now)
                } else {
                    db.userMistakeDao().insertOrUpdateMistake(
                        UserMistakeEntity(
                            structureTag = tag,
                            fullForm = contraction.fullForm,
                            contractedForm = contraction.contractedForm,
                            category = "YouTube Spoken English",
                            errorCount = 1,
                            successCount = 0,
                            lastMistakeMillis = now,
                            sampleSentence = phrase.contractedForm,
                            sampleTranslation = phrase.portugueseTranslation,
                            pedagogicalTip = contraction.ruleExplanation
                        )
                    )
                }
            } else {
                if (existing != null) {
                    db.userMistakeDao().incrementSuccess(tag)
                }
            }
        }

        // 3. Record vocabulary acquired
        val vocabEntities = phrase.vocabularyNotes.map { item ->
            VocabularyEntity(
                term = item.word,
                meaning = item.meaning,
                exampleSentence = item.example,
                exampleTranslation = item.translation,
                cefrLevel = item.level.code,
                itemType = if (item.isInformal) "INFORMAL_SPOKEN" else "VOCABULARY",
                isMastered = allCorrect,
                timestamp = now
            )
        }
        if (vocabEntities.isNotEmpty()) {
            db.vocabularyDao().insertAll(vocabEntities)
        }

        // 4. Record phrase history
        db.phraseHistoryDao().insertHistory(
            PhraseHistoryEntity(
                sceneId = "youtube_$videoId",
                phraseId = phrase.id,
                fullSentence = phrase.fullForm,
                naturalSentence = phrase.contractedForm,
                userTranslation = phrase.portugueseTranslation,
                wasCorrect = allCorrect,
                timestamp = now
            )
        )
    }

    private fun estimateCefrLevel(title: String, description: String): CefrLevel {
        val combined = "$title $description".lowercase()
        return when {
            combined.contains("c1") || combined.contains("c2") || combined.contains("advanced") || combined.contains("fluent") -> CefrLevel.C1
            combined.contains("b2") || combined.contains("upper intermediate") || combined.contains("business") -> CefrLevel.B2
            combined.contains("b1") || combined.contains("intermediate") -> CefrLevel.B1
            combined.contains("a1") || combined.contains("beginner") || combined.contains("starter") || combined.contains("basic") -> CefrLevel.A1
            else -> CefrLevel.A2
        }
    }

    private fun parseIsoDuration(isoDuration: String?): Pair<String, Int> {
        if (isoDuration.isNullOrBlank()) return Pair("3:00", 180)
        try {
            val pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
            val matcher = pattern.matcher(isoDuration)
            if (matcher.matches()) {
                val hours = matcher.group(1)?.toIntOrNull() ?: 0
                val minutes = matcher.group(2)?.toIntOrNull() ?: 0
                val seconds = matcher.group(3)?.toIntOrNull() ?: 0

                val totalSeconds = hours * 3600 + minutes * 60 + seconds
                val formatted = if (hours > 0) {
                    String.format("%d:%02d:%02d", hours, minutes, seconds)
                } else {
                    String.format("%d:%02d", minutes, seconds)
                }
                return Pair(formatted, totalSeconds)
            }
        } catch (e: Exception) {
            // fallback
        }
        return Pair("3:30", 210)
    }

    private fun cleanHtmlEntities(text: String): String {
        return text
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
    }
}
