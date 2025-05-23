package com.qrapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qrapp.presentation.screen.generate.GenerateScreen
import com.qrapp.presentation.screen.scan.ScanScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "generate") {
        composable("generate") { GenerateScreen() }
        composable("scan") { ScanScreen() }
    }
}