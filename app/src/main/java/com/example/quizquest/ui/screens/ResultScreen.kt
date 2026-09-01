package com.example.quizquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizquest.R

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    onBackToHome: () -> Unit
) {
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
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "QUIZ COMPLETED",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Your Score",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
            
            Text(
                text = "$score / $total",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            val message = when {
                score == total -> "Perfect! You're a genius! 🏆"
                score >= total * 0.7 -> "Great job! Almost perfect! 🌟"
                score >= total * 0.5 -> "Good effort! Keep it up! 👍"
                else -> "Keep practicing! You'll get better! 💪"
            }
            
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("BACK TO HOME", fontWeight = FontWeight.Bold)
            }
        }
    }
}
