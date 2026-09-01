package com.example.quizquest.data.model.api

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("response_message") val responseMessage: String,
    val token: String
)

data class QuizResponse(
    @SerializedName("response_code") val responseCode: Int,
    val results: List<QuestionRemote>
)

data class QuestionRemote(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
)
