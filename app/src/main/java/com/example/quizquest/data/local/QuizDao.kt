package com.example.quizquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quizquest.data.model.db.QuizHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert
    suspend fun insertHistory(history: QuizHistory)

    @Query("SELECT * FROM quiz_history WHERE userId = :userId ORDER BY date DESC")
    fun getHistoryForUser(userId: String): Flow<List<QuizHistory>>

    @Query("DELETE FROM quiz_history WHERE id = :historyId")
    suspend fun deleteHistory(historyId: Int)
}
