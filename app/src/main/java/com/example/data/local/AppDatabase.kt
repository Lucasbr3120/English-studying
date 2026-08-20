package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneProgressDao {
    @Query("SELECT * FROM scene_progress")
    fun getAllProgress(): Flow<List<SceneProgressEntity>>

    @Query("SELECT * FROM scene_progress WHERE sceneId = :sceneId LIMIT 1")
    fun getProgressBySceneId(sceneId: String): Flow<SceneProgressEntity?>

    @Query("SELECT * FROM scene_progress ORDER BY lastStudiedTimestamp DESC LIMIT 1")
    fun getLastStudiedScene(): Flow<SceneProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: SceneProgressEntity)
}

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_items ORDER BY id DESC")
    fun getAllVocabulary(): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary_items WHERE cefrLevel = :level ORDER BY id DESC")
    fun getVocabularyByLevel(level: String): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary_items WHERE itemType = :type ORDER BY id DESC")
    fun getVocabularyByType(type: String): Flow<List<VocabularyEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVocabulary(item: VocabularyEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<VocabularyEntity>)

    @Update
    suspend fun updateVocabulary(item: VocabularyEntity)

    @Query("UPDATE vocabulary_items SET isMastered = :isMastered WHERE id = :id")
    suspend fun setMasteredStatus(id: Long, isMastered: Boolean)
}

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStatsDirect(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserStats(stats: UserStatsEntity)
}

@Dao
interface PhraseHistoryDao {
    @Query("SELECT * FROM phrase_history ORDER BY timestamp DESC LIMIT 50")
    fun getRecentHistory(): Flow<List<PhraseHistoryEntity>>

    @Query("SELECT COUNT(*) FROM phrase_history")
    fun getTotalHistoryCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: PhraseHistoryEntity)
}

@Dao
interface UserMistakeDao {
    @Query("SELECT * FROM user_mistakes ORDER BY errorCount DESC")
    fun getAllMistakes(): Flow<List<UserMistakeEntity>>

    @Query("SELECT * FROM user_mistakes ORDER BY errorCount DESC LIMIT 10")
    fun getTopMistakes(): Flow<List<UserMistakeEntity>>

    @Query("SELECT * FROM user_mistakes WHERE structureTag = :tag LIMIT 1")
    suspend fun getMistakeByTag(tag: String): UserMistakeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMistake(mistake: UserMistakeEntity)

    @Query("UPDATE user_mistakes SET errorCount = errorCount + 1, lastMistakeMillis = :timestamp WHERE structureTag = :tag")
    suspend fun incrementMistake(tag: String, timestamp: Long)

    @Query("UPDATE user_mistakes SET successCount = successCount + 1 WHERE structureTag = :tag")
    suspend fun incrementSuccess(tag: String)
}

@Database(
    entities = [
        SceneProgressEntity::class,
        VocabularyEntity::class,
        UserStatsEntity::class,
        PhraseHistoryEntity::class,
        UserMistakeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sceneProgressDao(): SceneProgressDao
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun phraseHistoryDao(): PhraseHistoryDao
    abstract fun userMistakeDao(): UserMistakeDao
}
