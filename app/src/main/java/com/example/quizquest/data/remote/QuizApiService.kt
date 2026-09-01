package com.example.quizquest.data.remote

import com.example.quizquest.data.model.api.QuizResponse
import com.example.quizquest.data.model.api.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApiService {
    @GET("api_token.php?command=request")
    suspend fun getToken(): TokenResponse

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("type") type: String?,
        @Query("token") token: String?,
        @Query("category") category : Int? = null
    ): QuizResponse
}
