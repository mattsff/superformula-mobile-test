package com.qrapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qrapp.presentation.screen.generate.GenerateScreen
import com.qrapp.presentation.screen.scan.ScanScreen
import com.qrapp.presentation.screen.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("generate") { GenerateScreen(navController) }
        composable("scan") { ScanScreen(navController) }
    }
}

