package com.example.quizquest.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun signIn(email: String, pass: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val user = result.user
            if (user != null) {
                if (user.isEmailVerified) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Please verify your email address."))
                }
            } else {
                Result.failure(Exception("Authentication failed."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, pass: String, fullName: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user
            
            // Update Profile with Full Name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()
            user?.updateProfile(profileUpdates)?.await()
            
            // Send Verification Email
            user?.sendEmailVerification()?.await()
            
            if (user != null) Result.success(user)
            else Result.failure(Exception("Registration failed."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resendVerificationEmail(): Result<Unit> {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
