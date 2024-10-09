package com.example.composebasics.composebase.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

object ComposeBaseExtension {
    fun routeWithArgs(someRoute: String, args: List<Any> = emptyList()): String {
        var output: String = someRoute
        for(arg in args) {
            output += "/${arg}"  // todo: maybe add a string builder for efficiency idk
        }
        return output
    }

    // compose e generic T kemne kemne jani erokom hoye gese. I just wanted to extract this code:
    /*
     val rememberBackStackEntry: NavBackStackEntry = remember(mNavHostController.currentBackStackEntry) {
        mNavHostController.getBackStackEntry(HotelHome.Routes.HOTEL_HOME)
      }
      val mainViewModel = viewModel(HotelMainViewModel::class.java, rememberBackStackEntry)
    */
//    @Composable
//    inline fun <reified T: ViewModel>baseNavGraphViewModel(mNavHostController: NavHostController, route: String): T {
//        val rememberBackStackEntry: NavBackStackEntry = remember(mNavHostController.currentBackStackEntry) {
//            mNavHostController.getBackStackEntry(route)
//        }
//
//        val mViewModel: T = viewModel(T::class, rememberBackStackEntry)
//        return mViewModel
//    }

    fun Context.findActivity(): ComponentActivity? = when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}