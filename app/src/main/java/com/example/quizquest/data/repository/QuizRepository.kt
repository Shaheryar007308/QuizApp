package com.example.quizquest.data.repository

import com.example.quizquest.data.local.QuizDao
import com.example.quizquest.data.model.api.QuestionRemote
import com.example.quizquest.data.model.db.QuizHistory
import com.example.quizquest.data.remote.QuizApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizRepository(private val quizDao: QuizDao) {

    private val api = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuizApiService::class.java)

    private var sessionToken: String? = null

    suspend fun getQuestions(): List<QuestionRemote> {
        val questions = mutableListOf<QuestionRemote>()
        val cat = 23
        
        try {
            if (sessionToken == null) {
                val tokenRes = api.getToken()
                sessionToken = tokenRes.token
            }

            // Fetch 10 Multiple Choice Questions
            val multipleResponse = api.getQuestions(10, "multiple", sessionToken , cat)
            questions.addAll(multipleResponse.results)

            // Rate limit delay (OpenTDB allows 1 request per 5 seconds)
            delay(5100L)

            // Fetch 5 True/False Questions
            try {
                val booleanResponse = api.getQuestions(5, "boolean", sessionToken , cat)
                questions.addAll(booleanResponse.results)
            } catch (e: Exception) {
                // Graceful fallback: Proceed with already fetched questions if second call fails
                println("Failed to fetch boolean questions: ${e.message}")
            }

        } catch (e: Exception) {
            // Rethrow or handle based on whether we have any questions
            if (questions.isEmpty()) throw e
        }
        
        return questions.shuffled()
    }

    suspend fun saveHistory(history: QuizHistory) {
        quizDao.insertHistory(history)
    }

    fun getHistory(userId: String): Flow<List<QuizHistory>> {
        return quizDao.getHistoryForUser(userId)
    }

    suspend fun deleteHistory(historyId: Int) {
        quizDao.deleteHistory(historyId)
    }
}
