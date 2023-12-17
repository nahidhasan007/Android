package com.example.tweety.screens

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.tweety.model.Tweet
import com.example.tweety.network.viewmodels.CategoryViewModel

@Composable
fun CategoryScreen(navController: NavHostController, onClick: (category: String) -> Unit) {

    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val categories: State<List<String>> = categoryViewModel.categories.collectAsState()
    val tweets: State<Tweet> = categoryViewModel.allTweets.collectAsState()

    Log.e("AllTweets", tweets.toString())

    if (categories.value.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Loading...", style = MaterialTheme.typography.displaySmall)
        }
    } else {
        Column {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.SpaceAround
            )
            {
                items(categories.value) {
                    CategoryItem(category = it, onClick = onClick)
                }
            }
            Button(
                onClick = {
                    categoryViewModel.getAllTweet()
                    val argumentValue = tweets.value // Replace with your actual argument value
                    val route = "tweet/$argumentValue"
                    navController.navigate(route)

                }, modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp)
                    .background(color = Color.White) // Change the background color
            ) {
                Text(
                    text = "See All",
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )

            }

            Button(
                onClick = {
                    val route = "test"
                    navController.navigate(route)

                }, modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp)
                    .background(color = Color.White) // Change the background color
            ) {
                Text(
                    text = "See All",
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )

            }
        }

    }
}

@Composable
fun CategoryItem(category: String, onClick: (category: String) -> Unit) {
    Box(modifier = Modifier
        .padding(4.dp)
        .clickable {
            onClick(category)
        }
        .size(160.dp)
        .clip(RoundedCornerShape(8.dp))
        .paint(painter = painterResource(id = com.example.tweety.R.drawable.ic_bg))
        .border(1.dp, Color(0xFFEEEEEE)),
        contentAlignment = Alignment.BottomCenter
    )
    {
        Text(
            text = category,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(0.dp, 20.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}