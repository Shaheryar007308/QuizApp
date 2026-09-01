package com.example.quizquest.viewmodel

import android.text.Html
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizquest.data.model.api.QuestionRemote
import com.example.quizquest.data.model.db.QuizHistory
import com.example.quizquest.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer

    private val _isAnswered = MutableStateFlow(false)
    val isAnswered: StateFlow<Boolean> = _isAnswered

    fun loadQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val remote = repository.getQuestions()
                if (remote.isEmpty()) {
                    _error.value = "Could not fetch questions. Please try again."
                } else {
                    _questions.value = remote.map { it.toQuestion() }
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message ?: "Unknown error"}"
            }
            _isLoading.value = false
        }
    }

    fun selectAnswer(answer: String) {
        if (!_isAnswered.value) {
            _selectedAnswer.value = answer
        }
    }

    fun submitAnswer() {
        val currentQ = _questions.value.getOrNull(_currentIndex.value) ?: return
        if (_selectedAnswer.value == currentQ.correctAnswer) {
            _score.value += 1
        }
        _isAnswered.value = true
    }

    fun nextQuestion(userId: String) {
        viewModelScope.launch {
            if (_currentIndex.value < _questions.value.size - 1) {
                _currentIndex.value += 1
                _selectedAnswer.value = null
                _isAnswered.value = false
            } else {
                saveResult(userId)
                _isFinished.value = true
            }
        }
    }

    private fun saveResult(userId: String) {
        viewModelScope.launch {
            try {
                repository.saveHistory(
                    QuizHistory(
                        userId = userId,
                        score = _score.value,
                        totalQuestions = _questions.value.size,
                        date = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                // Silently fail or log for history
            }
        }
    }

    private fun QuestionRemote.toQuestion(): Question {
        val decodedQ = Html.fromHtml(question, Html.FROM_HTML_MODE_LEGACY).toString()
        val decodedCorrect = Html.fromHtml(correctAnswer, Html.FROM_HTML_MODE_LEGACY).toString()
        val decodedIncorrect = incorrectAnswers.map { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString() }
        
        return Question(
            question = decodedQ,
            options = (decodedIncorrect + decodedCorrect).shuffled(),
            correctAnswer = decodedCorrect
        )
    }
}
