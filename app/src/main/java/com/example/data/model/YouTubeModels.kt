package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class YouTubeSearchFilter(
    val query: String = "",
    val level: CefrLevel? = null,
    val category: YouTubeCategory = YouTubeCategory.CONVERSATION,
    val creativeCommonsOnly: Boolean = false
)

enum class YouTubeCategory(
    val id: String,
    val displayName: String,
    val searchKeywords: String,
    val iconName: String
) {
    CONVERSATION("conversation", "Conversação", "English conversation dialogue natural speaking", "Chat"),
    DAILY_LIFE("daily_life", "Cotidiano", "daily routine life spoken English dialogue", "WbSunny"),
    TRAVEL("travel", "Viagem", "English for travel airport hotel directions dialogue", "Flight"),
    WORK("work", "Trabalho", "business English workplace office meeting dialogue", "Work"),
    SCHOOL("school", "Escola", "English school university classroom student dialogue", "School"),
    INTERVIEW("interview", "Entrevista", "English interview job speaking natural conversation", "RecordVoiceOver"),
    SHORT_FILM("short_film", "Filme / Curta", "English short film dialogue scene", "Movie"),
    COMEDY("comedy", "Comédia", "English comedy sketch funny sitcom dialogue", "SentimentVerySatisfied"),
    DRAMA("drama", "Drama", "English dramatic dialogue movie scene acting", "TheaterComedy")
}

data class YouTubeVideoItem(
    val id: String,
    val title: String,
    val channelTitle: String,
    val description: String,
    val thumbnailUrl: String,
    val durationFormatted: String? = null,
    val durationSeconds: Int = 0,
    val suggestedLevel: CefrLevel = CefrLevel.A2,
    val hasClosedCaptions: Boolean = true,
    val isEmbeddable: Boolean = true,
    val license: String = "youtube", // "creativeCommon" or "youtube"
    val publishedAt: String = "",
    val category: YouTubeCategory = YouTubeCategory.CONVERSATION,
    val authorizedStudySet: YouTubeStudySet? = null
) {
    val isCreativeCommons: Boolean
        get() = license.equals("creativeCommon", ignoreCase = true)
}

data class YouTubeStudySet(
    val videoId: String,
    val topicSummary: String,
    val sourceAttribution: String,
    val phrases: List<YouTubeStudyPhrase>
)

data class YouTubeStudyPhrase(
    val id: String,
    val fullForm: String,
    val contractedForm: String,
    val portugueseTranslation: String,
    val acceptableTranslations: List<String> = emptyList(),
    val informalSpokenForm: String? = null, // e.g. "gonna", "wanna", "gotta"
    val contractionsList: List<ContractionPair> = emptyList(),
    val vocabularyNotes: List<YouTubeVocabularyItem> = emptyList(),
    val grammarExplanation: String,
    val spokenTip: String? = null,
    val comprehensionQuestion: String,
    val comprehensionCorrectAnswer: String,
    val comprehensionOptions: List<String>
)

data class YouTubeVocabularyItem(
    val word: String,
    val meaning: String,
    val translation: String,
    val example: String,
    val level: CefrLevel = CefrLevel.A2,
    val isInformal: Boolean = false
)

// YouTube Data API v3 DTOs (Moshi)
@JsonClass(generateAdapter = true)
data class YouTubeSearchResponse(
    val kind: String? = null,
    val items: List<YouTubeSearchItemDto>? = null,
    val nextPageToken: String? = null,
    val error: YouTubeApiErrorDto? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSearchItemDto(
    val id: YouTubeResourceIdDto? = null,
    val snippet: YouTubeSnippetDto? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeResourceIdDto(
    val kind: String? = null,
    val videoId: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSnippetDto(
    val publishedAt: String? = null,
    val channelId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val thumbnails: YouTubeThumbnailsDto? = null,
    val channelTitle: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnailsDto(
    val default: YouTubeThumbnailDto? = null,
    val medium: YouTubeThumbnailDto? = null,
    val high: YouTubeThumbnailDto? = null,
    val standard: YouTubeThumbnailDto? = null,
    val maxres: YouTubeThumbnailDto? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnailDto(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoListResponse(
    val items: List<YouTubeVideoDto>? = null,
    val error: YouTubeApiErrorDto? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoDto(
    val id: String? = null,
    val snippet: YouTubeSnippetDto? = null,
    val contentDetails: YouTubeContentDetailsDto? = null,
    val status: YouTubeStatusDto? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeContentDetailsDto(
    val duration: String? = null,
    val caption: String? = null, // "true" or "false"
    val licensedContent: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeStatusDto(
    val embeddable: Boolean? = true,
    val license: String? = "youtube" // "creativeCommon" or "youtube"
)

@JsonClass(generateAdapter = true)
data class YouTubeApiErrorDto(
    val code: Int? = null,
    val message: String? = null,
    val errors: List<YouTubeErrorDetailDto>? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeErrorDetailDto(
    val message: String? = null,
    val domain: String? = null,
    val reason: String? = null
)
