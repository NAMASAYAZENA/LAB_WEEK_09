package com.example.lab_week_09

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

// =======================================================
// Navigation Graph
// =======================================================
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable(
            route = "summary/{jsonData}",
            arguments = listOf(navArgument("jsonData") { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonData = backStackEntry.arguments?.getString("jsonData") ?: "[]"
            SummaryScreen(navController, jsonData)
        }
    }
}
