package com.example.quizquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizquest.data.model.db.QuizHistory
import com.example.quizquest.data.repository.QuizRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: QuizRepository, userId: String) : ViewModel() {

    val history: StateFlow<List<QuizHistory>> = repository.getHistory(userId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun deleteHistory(historyId: Int) {
        viewModelScope.launch {
            repository.deleteHistory(historyId)
        }
    }
}
