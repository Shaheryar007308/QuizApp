package com.example.quizquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizquest.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(repository.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isVerificationSent = MutableStateFlow(false)
    val isVerificationSent: StateFlow<Boolean> = _isVerificationSent

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Email and Password cannot be empty"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            repository.signIn(email, pass).onSuccess {
                _user.value = it
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun register(fullName: String, email: String, pass: String, confirmPass: String) {
        if (fullName.isBlank()) {
            _error.value = "Full Name is required"
            return
        }
        if (!validate(email, pass)) return
        if (pass != confirmPass) {
            _error.value = "Passwords do not match"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            repository.signUp(email, pass, fullName).onSuccess {
                _isVerificationSent.value = true
                _error.value = "Verification email sent. Please check your inbox before logging in."
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun resendVerification() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.resendVerificationEmail().onSuccess {
                _error.value = "Verification email resent."
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    private fun validate(email: String, pass: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$".toRegex()
        if (!email.matches(emailRegex)) {
            _error.value = "Invalid email format"
            return false
        }
        if (pass.length < 6) {
            _error.value = "Password must be at least 6 characters"
            return false
        }
        return true
    }

    fun clearError() {
        _error.value = null
    }

    fun logout() {
        repository.signOut()
        _user.value = null
    }
}
