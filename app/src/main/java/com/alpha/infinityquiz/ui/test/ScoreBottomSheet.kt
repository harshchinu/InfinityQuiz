package com.alpha.infinityquiz.ui.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScoreBottomSheet(
    score: Int,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
           .fillMaxWidth()
           .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Score: $score out of 5", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onContinue) {
            Text(text = "Continue")
        }
    }
}