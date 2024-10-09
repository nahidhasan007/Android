package com.example.composebasics.composebase.callbacks

import androidx.navigation.NavGraphBuilder

interface BaseChildNavGraph {
    fun createChildNavGraphBuilder(): (NavGraphBuilder.() -> Unit)
}