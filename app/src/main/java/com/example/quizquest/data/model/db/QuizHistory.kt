package com.example.quizquest.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_history")
data class QuizHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val score: Int,
    val totalQuestions: Int,
    val date: Long = System.currentTimeMillis()
)
