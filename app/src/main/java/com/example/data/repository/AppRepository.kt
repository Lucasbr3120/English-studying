package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.ai.GeminiAiService
import com.example.data.local.AppDatabase
import com.example.data.local.PhraseHistoryEntity
import com.example.data.local.PrepopulatedScenes
import com.example.data.local.SceneProgressEntity
import com.example.data.local.UserMistakeEntity
import com.example.data.local.UserStatsEntity
import com.example.data.local.VocabularyEntity
import com.example.data.model.CefrLevel
import com.example.data.model.ContractionCatalog
import com.example.data.model.ContractionPair
import com.example.data.model.Scene
import com.example.data.model.ScenePhrase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class AppRepository(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "scene_english.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    val aiService = GeminiAiService()
    val youtubeRepository = YouTubeRepository(db)

    private val _customScenes = MutableStateFlow<List<Scene>>(emptyList())
    val customScenes: StateFlow<List<Scene>> = _customScenes.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val existingStats = db.userStatsDao().getUserStatsDirect()
        if (existingStats == null) {
            db.userStatsDao().saveUserStats(
                UserStatsEntity(
                    id = 1,
                    currentLevel = "A1",
                    streakDays = 1,
                    lastStudyDateMillis = System.currentTimeMillis(),
                    totalPhrasesStudied = 0,
                    totalCorrect = 0,
                    totalErrors = 0
                )
            )
        }

        // Seed initial vocabulary and contractions
        val vocabList = mutableListOf<VocabularyEntity>()
        ContractionCatalog.allRules.forEach { rule ->
            vocabList.add(
                VocabularyEntity(
                    term = rule.contractedForm,
                    meaning = "${rule.fullForm} • ${rule.explanationPt}",
                    exampleSentence = rule.exampleSentenceContracted,
                    exampleTranslation = rule.translationPt,
                    cefrLevel = "A1",
                    itemType = "CONTRACTION"
                )
            )
        }

        PrepopulatedScenes.allScenes.forEach { scene ->
            scene.phrases.forEach { phrase ->
                vocabList.add(
                    VocabularyEntity(
                        term = phrase.naturalForm.split(" ").take(3).joinToString(" "),
                        meaning = phrase.vocabularyNotes,
                        exampleSentence = phrase.additionalExample,
                        exampleTranslation = phrase.additionalExampleTranslation,
                        cefrLevel = scene.level.code,
                        itemType = "EXPRESSION"
                    )
                )
            }
        }
        db.vocabularyDao().insertAll(vocabList)
    }

    fun getAllScenes(): List<Scene> {
        return PrepopulatedScenes.allScenes + _customScenes.value
    }

    fun getScenesByLevel(level: CefrLevel): List<Scene> {
        return getAllScenes().filter { it.level == level }
    }

    fun getSceneById(id: String): Scene? {
        if (id.startsWith("adaptive_revision_")) {
            return generateAdaptiveRevisionSceneDirect()
        }
        return getAllScenes().firstOrNull { it.id == id }
    }

    fun addCustomScene(scene: Scene) {
        val current = _customScenes.value.toMutableList()
        current.add(0, scene)
        _customScenes.value = current
    }

    fun getSceneProgress(sceneId: String): Flow<SceneProgressEntity?> {
        return db.sceneProgressDao().getProgressBySceneId(sceneId)
    }

    fun getAllProgress(): Flow<List<SceneProgressEntity>> {
        return db.sceneProgressDao().getAllProgress()
    }

    fun getLastStudiedScene(): Flow<SceneProgressEntity?> {
        return db.sceneProgressDao().getLastStudiedScene()
    }

    suspend fun saveSceneProgress(sceneId: String, levelCode: String, completedPhrases: Int, totalPhrases: Int) {
        db.sceneProgressDao().saveProgress(
            SceneProgressEntity(
                sceneId = sceneId,
                levelCode = levelCode,
                completedPhrases = completedPhrases,
                totalPhrases = totalPhrases,
                isCompleted = completedPhrases >= totalPhrases,
                lastStudiedTimestamp = System.currentTimeMillis()
            )
        )
    }

    fun getUserStats(): Flow<UserStatsEntity?> {
        return db.userStatsDao().getUserStats()
    }

    suspend fun recordPhraseAttempt(
        sceneId: String,
        phraseId: String,
        fullSentence: String,
        naturalSentence: String,
        userTranslation: String,
        isCorrect: Boolean
    ) {
        // Record history entry
        db.phraseHistoryDao().insertHistory(
            PhraseHistoryEntity(
                sceneId = sceneId,
                phraseId = phraseId,
                fullSentence = fullSentence,
                naturalSentence = naturalSentence,
                userTranslation = userTranslation,
                wasCorrect = isCorrect
            )
        )

        // Update stats
        val currentStats = db.userStatsDao().getUserStatsDirect() ?: UserStatsEntity()
        val now = System.currentTimeMillis()
        val streak = calculateUpdatedStreak(currentStats.lastStudyDateMillis, now, currentStats.streakDays)

        db.userStatsDao().saveUserStats(
            currentStats.copy(
                totalPhrasesStudied = currentStats.totalPhrasesStudied + 1,
                totalCorrect = currentStats.totalCorrect + if (isCorrect) 1 else 0,
                totalErrors = currentStats.totalErrors + if (!isCorrect) 1 else 0,
                streakDays = streak,
                lastStudyDateMillis = now
            )
        )
    }

    suspend fun recordStructureMistake(
        tag: String,
        fullForm: String,
        contractedForm: String,
        category: String,
        sampleSentence: String,
        sampleTranslation: String,
        tip: String
    ) {
        val existing = db.userMistakeDao().getMistakeByTag(tag)
        if (existing != null) {
            db.userMistakeDao().incrementMistake(tag, System.currentTimeMillis())
        } else {
            db.userMistakeDao().insertOrUpdateMistake(
                UserMistakeEntity(
                    structureTag = tag,
                    fullForm = fullForm,
                    contractedForm = contractedForm,
                    category = category,
                    errorCount = 1,
                    successCount = 0,
                    lastMistakeMillis = System.currentTimeMillis(),
                    sampleSentence = sampleSentence,
                    sampleTranslation = sampleTranslation,
                    pedagogicalTip = tip
                )
            )
        }
    }

    suspend fun recordStructureSuccess(tag: String) {
        val existing = db.userMistakeDao().getMistakeByTag(tag)
        if (existing != null) {
            db.userMistakeDao().incrementSuccess(tag)
        }
    }

    fun getTopMistakes(): Flow<List<UserMistakeEntity>> {
        return db.userMistakeDao().getTopMistakes()
    }

    fun getAllMistakes(): Flow<List<UserMistakeEntity>> {
        return db.userMistakeDao().getAllMistakes()
    }

    private fun generateAdaptiveRevisionSceneDirect(): Scene {
        // Fallback or template adaptive revision scene focusing on high error structures
        return Scene(
            id = "adaptive_revision_default",
            title = "Revisão Adaptativa: Foco em Erros",
            level = CefrLevel.B1,
            durationMinutes = 3,
            difficultyStars = 3,
            contextDescription = "Treino inteligente focado nas estruturas e contrações que você mais errou recentemente.",
            genre = "Treino Personalizado",
            imageResName = "scene_coffee_shop",
            mainVocabulary = listOf("I'm", "don't", "wanna", "gonna", "can't"),
            phrases = listOf(
                ScenePhrase(
                    id = "rev_p1",
                    characterName = "Treinador IA",
                    fullForm = "I am going to make sure that I do not make this mistake again.",
                    naturalForm = "I'm gonna make sure that I don't make this mistake again.",
                    portugueseTranslation = "Eu vou garantir que não vou cometer esse erro de novo.",
                    acceptableTranslations = listOf(
                        "Vou garantir que não cometo esse erro novamente.",
                        "Vou ter certeza de não fazer esse erro de novo."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("I am", "I'm", "'I am' vira 'I'm'."),
                        ContractionPair("going to", "gonna", "'going to' vira 'gonna' na fala natural."),
                        ContractionPair("do not", "don't", "'do not' vira 'don't'.")
                    ),
                    vocabularyNotes = "'make sure' = garantir, certificar-se.",
                    grammarTip = "Observe como pronomes e auxiliares se fundem em cadeia no inglês falado.",
                    additionalExample = "I'm gonna do it right now.",
                    additionalExampleTranslation = "Eu vou fazer isso agora mesmo.",
                    blankSentence = "I ______ gonna make sure I ______ do that.",
                    blankCorrectAnswer = "'m",
                    blankOptions = listOf("'m", "am", "was", "'ve"),
                    quizQuestion = "Qual a forma falada de 'going to' no cinema?",
                    quizCorrectAnswer = "gonna",
                    quizOptions = listOf("gonna", "wanna", "gotta", "kinda")
                ),
                ScenePhrase(
                    id = "rev_p2",
                    characterName = "Treinador IA",
                    fullForm = "You cannot say that you do not want to go with us.",
                    naturalForm = "You can't say that you don't wanna go with us.",
                    portugueseTranslation = "Você não pode dizer que não quer ir com a gente.",
                    acceptableTranslations = listOf(
                        "Você não pode falar que não quer vir conosco.",
                        "Não dá pra dizer que você não quer ir com a gente."
                    ),
                    contractionsUsed = listOf(
                        ContractionPair("cannot", "can't", "'cannot' vira 'can't'."),
                        ContractionPair("do not", "don't", "'do not' vira 'don't'."),
                        ContractionPair("want to", "wanna", "'want to' vira 'wanna'.")
                    ),
                    vocabularyNotes = "'wanna' = want to (querer).",
                    grammarTip = "'Cannot' no negativo falado é sempre 'can't'.",
                    additionalExample = "You can't do this to me.",
                    additionalExampleTranslation = "Você não pode fazer isso comigo.",
                    blankSentence = "You ______ say you ______ wanna go.",
                    blankCorrectAnswer = "can't",
                    blankOptions = listOf("can't", "don't", "won't", "shouldn't"),
                    quizQuestion = "O que significa 'wanna'?",
                    quizCorrectAnswer = "want to (querer)",
                    quizOptions = listOf("want to (querer)", "going to (ir)", "have to (ter que)", "need to (precisar)")
                )
            )
        )
    }

    suspend fun updateUserLevel(newLevel: String) {
        val currentStats = db.userStatsDao().getUserStatsDirect() ?: UserStatsEntity()
        db.userStatsDao().saveUserStats(currentStats.copy(currentLevel = newLevel))
    }

    private fun calculateUpdatedStreak(lastMillis: Long, nowMillis: Long, currentStreak: Int): Int {
        val calLast = Calendar.getInstance().apply { timeInMillis = lastMillis }
        val calNow = Calendar.getInstance().apply { timeInMillis = nowMillis }

        val sameDay = calLast.get(Calendar.YEAR) == calNow.get(Calendar.YEAR) &&
                calLast.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR)

        if (sameDay) return currentStreak

        calLast.add(Calendar.DAY_OF_YEAR, 1)
        val isConsecutiveDay = calLast.get(Calendar.YEAR) == calNow.get(Calendar.YEAR) &&
                calLast.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR)

        return if (isConsecutiveDay) currentStreak + 1 else 1
    }

    fun getAllVocabulary(): Flow<List<VocabularyEntity>> = db.vocabularyDao().getAllVocabulary()

    fun getVocabularyByLevel(level: String): Flow<List<VocabularyEntity>> = db.vocabularyDao().getVocabularyByLevel(level)

    suspend fun toggleVocabularyMastered(id: Long, currentStatus: Boolean) {
        db.vocabularyDao().setMasteredStatus(id, !currentStatus)
    }

    fun getRecentHistory(): Flow<List<PhraseHistoryEntity>> = db.phraseHistoryDao().getRecentHistory()

    fun getPronunciationExercises(category: com.example.data.model.PronunciationCategory? = null): List<com.example.data.model.PronunciationExerciseItem> {
        return if (category == null) {
            com.example.data.local.PrepopulatedPronunciation.allExercises
        } else {
            com.example.data.local.PrepopulatedPronunciation.allExercises.filter { it.category == category }
        }
    }

    fun getPronunciationExerciseById(id: String): com.example.data.model.PronunciationExerciseItem? {
        return com.example.data.local.PrepopulatedPronunciation.allExercises.firstOrNull { it.id == id }
    }
}
