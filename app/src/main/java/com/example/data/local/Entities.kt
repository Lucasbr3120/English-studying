package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scene_progress")
data class SceneProgressEntity(
    @PrimaryKey val sceneId: String,
    val levelCode: String,
    val completedPhrases: Int,
    val totalPhrases: Int,
    val isCompleted: Boolean,
    val lastStudiedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "vocabulary_items")
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val term: String,
    val meaning: String,
    val exampleSentence: String,
    val exampleTranslation: String,
    val cefrLevel: String,
    val itemType: String, // CONTRACTION, PHRASAL_VERB, IDIOM, VOCABULARY
    val isMastered: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val currentLevel: String = "A1",
    val streakDays: Int = 1,
    val lastStudyDateMillis: Long = System.currentTimeMillis(),
    val totalPhrasesStudied: Int = 0,
    val totalCorrect: Int = 0,
    val totalErrors: Int = 0
)

@Entity(tableName = "phrase_history")
data class PhraseHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sceneId: String,
    val phraseId: String,
    val fullSentence: String,
    val naturalSentence: String,
    val userTranslation: String,
    val wasCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
