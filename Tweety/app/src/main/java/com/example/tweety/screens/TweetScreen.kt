package com.example.tweety.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

@Composable
fun TweetScreen(tweets : String?) {
    if (tweets != null) {
        items(tweet = tweets)
    }
}

@Composable
fun items(tweet: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
        content = {
            Text(
                text = "Hi this is from Tweet Screen\n${tweet}",
                modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}