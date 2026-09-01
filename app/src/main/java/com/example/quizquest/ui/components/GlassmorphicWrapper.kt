package com.example.quizquest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quizquest.R

@Composable
fun GlassmorphicWrapper(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.quiz_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.White.copy(alpha = 0.85f),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            content()
        }
    }
}
