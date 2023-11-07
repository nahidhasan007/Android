package com.example.tweety

import android.annotation.SuppressLint
import android.graphics.Paint.Style
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tweety.api.TweetApi
import com.example.tweety.model.Tweet
import com.example.tweety.network.viewmodels.CategoryViewModel
import com.example.tweety.screens.CategoryItem
import com.example.tweety.screens.CategoryScreen
import com.example.tweety.screens.DetailScreen
import com.example.tweety.screens.TweetScreen
import com.example.tweety.ui.theme.TweetyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var tweetApi : TweetApi

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        GlobalScope.launch {
//            var response = tweetApi.getCategories()
//            Log.e("Check_Categories", response.body().toString())
//        }

        setContent {

            TweetyTheme {
                // A surface container using the 'background' color from the theme
//               DetailScreen()
//                CategoryScreen()
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(text = "Tweety",
                                textAlign = TextAlign.Center)
                        }, colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = Color.Black,
                            titleContentColor = Color.White,
                        ))
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        appNavigation()
                    }

                }
            }
        }
    }
}

@Composable
fun appNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "category"){
        composable(route = "category"){
            CategoryScreen(navController) {
                navController.navigate("detail/${it}")
            }
        }
        composable(route = "tweet/{tweets}",
            arguments = listOf(
                navArgument("tweets"){
                    type = NavType.StringType
                }
            )) {
            val tweets = it.arguments?.getString("tweets")
            TweetScreen(tweets)
        }
        composable(route = "detail/{category}",
            arguments = listOf(
                navArgument("category"){
                    type = NavType.StringType
                }
            )
        ){
            DetailScreen()
        }
    }
    
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TweetyTheme {
//        Greeting("Android")
//    }