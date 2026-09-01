package com.example.quizquest.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object HomeRoute
@Serializable object QuizRoute
@Serializable data class ResultRoute(val score: Int, val total: Int)
@Serializable object HistoryRoute
