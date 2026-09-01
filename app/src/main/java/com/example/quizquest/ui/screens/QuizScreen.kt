package com.example.quizquest.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizquest.R
import com.example.quizquest.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    userId: String,
    onQuizFinished: (Int, Int) -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val score by viewModel.score.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val isAnswered by viewModel.isAnswered.collectAsState()

    LaunchedEffect(Unit) {
        if (questions.isEmpty()) {
            viewModel.loadQuestions()
        }
    }

    LaunchedEffect(isFinished) {
        if (isFinished) {
            onQuizFinished(score, questions.size)
        }
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
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(24.dp)
        ) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (error != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = error!!, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadQuestions() }) {
                        Text("Retry")
                    }
                }
            } else if (questions.isNotEmpty()) {
                val currentQuestion = questions[currentIndex]
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentIndex + 1}/${questions.size}",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Score: $score",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(8.dp),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.2f),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.options.forEach { option ->
                        OptionItem(
                            text = option,
                            isSelected = selectedAnswer == option,
                            isCorrect = option == currentQuestion.correctAnswer,
                            isAnswered = isAnswered,
                            onClick = { viewModel.selectAnswer(option) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(30.dp))
                
                Button(
                    onClick = { 
                        if (!isAnswered) {
                            viewModel.submitAnswer()
                        } else {
                            viewModel.nextQuestion(userId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp).padding(bottom = 25.dp),
                    enabled = selectedAnswer != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAnswered) Color.White else Color.White.copy(alpha = 0.2f),
                        contentColor = if (isAnswered) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val label = when {
                        !isAnswered -> "Check Answer"
                        currentIndex == questions.size - 1 -> "Finish Quiz"
                        else -> "Next Question"
                    }
                    Text(label, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isAnswered && isCorrect -> Color(0xCC66BB6A) // Translucent Green
            isAnswered && isSelected && !isCorrect -> Color(0xCCEF5350) // Translucent Red
            isSelected -> Color.White.copy(alpha = 0.3f)
            else -> Color.White.copy(alpha = 0.15f)
        }, label = "color"
    )
    
    val borderColor = when {
        isAnswered && isCorrect -> Color.White
        isAnswered && isSelected && !isCorrect -> Color.White
        isSelected -> Color.White
        else -> Color.White.copy(alpha = 0.5f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isAnswered) { onClick() }
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            
            if (isAnswered) {
                if (isCorrect) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                } else if (isSelected) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}
