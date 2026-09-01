package com.example.quizquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizquest.R
import com.example.quizquest.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    
    val user by viewModel.user.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(user) {
        if (user != null) onLoginSuccess()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.quiz_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "QuizQuest",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                text = "Challenge Your Mind",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    viewModel.clearError()
                },
                label = { Text("Email Address", color = Color.White.copy(alpha = 0.8f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    viewModel.clearError()
                },
                label = { Text("Password", color = Color.White.copy(alpha = 0.8f)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            
            error?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    if (it.contains("verify", ignoreCase = true)) {
                        TextButton(onClick = { viewModel.resendVerification() }) {
                            Text("Resend Email Verification", color = Color.Cyan)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("LOGIN", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Sign Up", color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}
