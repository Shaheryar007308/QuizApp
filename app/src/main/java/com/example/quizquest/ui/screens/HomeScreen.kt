package com.example.quizquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizquest.R
import com.example.quizquest.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onStartQuiz: () -> Unit,
    onViewHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val user by authViewModel.user.collectAsState()
    val displayName = user?.displayName ?: "Explorer"

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
                text = "Hello, $displayName!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ready to test your knowledge?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = onStartQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text("START NEW QUIZ", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = onViewHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.5f))),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("MY HISTORY", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            TextButton(onClick = { 
                authViewModel.logout()
                onLogout()
            }) {
                Text("Logout Account", color = Color(0xFFEF5350))
            }
        }
    }
}
