package com.example.composebasics.coordinator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.composebasics.composebase.callbacks.BaseChildNavGraph

class BaseNavCoordinator (
    private val mNavHostController: NavHostController,
    private val mainModifier: Modifier,
    private val listOfChildNavGraphs: List<BaseChildNavGraph>,
    private val mStartDestination: String
) {


    @Composable
    fun AuthenticationNavHost() {
        val mGraphBuilder: NavGraphBuilder.() -> Unit = {
            for (childGraphBuilder in listOfChildNavGraphs) {
                childGraphBuilder.createChildNavGraphBuilder().invoke(this)
            }
        }
        NavHost(
            modifier = mainModifier,
            navController = mNavHostController,
            startDestination = mStartDestination,
            builder = mGraphBuilder
        )
    }
}