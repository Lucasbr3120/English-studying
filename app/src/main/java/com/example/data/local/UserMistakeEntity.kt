package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_mistakes")
data class UserMistakeEntity(
    @PrimaryKey
    val structureTag: String, // e.g. "I am -> I'm", "do not -> don't", "going to -> gonna"
    val fullForm: String,
    val contractedForm: String,
    val category: String,
    val errorCount: Int = 0,
    val successCount: Int = 0,
    val lastMistakeMillis: Long = System.currentTimeMillis(),
    val sampleSentence: String = "",
    val sampleTranslation: String = "",
    val pedagogicalTip: String = ""
)
