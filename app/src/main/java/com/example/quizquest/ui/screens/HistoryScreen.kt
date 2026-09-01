package com.example.quizquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.example.quizquest.R
import com.example.quizquest.data.model.db.QuizHistory
import com.example.quizquest.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    val history by viewModel.history.collectAsState()

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
        ) {
            TopAppBar(
                title = { Text("Quiz History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            if (history.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No past scores yet.", color = Color.White.copy(alpha = 0.7f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(history) { item ->
                        HistoryItem(
                            history = item,
                            onDelete = { viewModel.deleteHistory(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(history: QuizHistory, onDelete: () -> Unit) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(history.date))
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Score: ${history.score}/${history.totalQuestions}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = dateStr,
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            val percentage = (history.score.toFloat() / history.totalQuestions * 100).toInt()
            Text(
                text = "$percentage%",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}
