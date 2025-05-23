package com.qrapp.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.qrapp.presentation.R
import com.qrapp.presentation.components.QrAppScaffold

@Composable
fun HomeScreen(navController: NavController) {
    QrAppScaffold(
        navController = navController,
        showBack = false,
        title = stringResource(R.string.home_title),
        floatingActionButton = {
            HomeFab(navController)
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_welcome),
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@Composable
private fun HomeFab(navController: NavController) {
    val expanded = remember { mutableStateOf(false) }
    val fabColor = MaterialTheme.colors.primary
    val fabContentColor = MaterialTheme.colors.onPrimary
    val secondaryFabColor = MaterialTheme.colors.secondary
    val secondaryFabContentColor = MaterialTheme.colors.onSecondary
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = expanded.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = {
                        expanded.value = false
                        navController.navigate("scan")
                    },
                    backgroundColor = secondaryFabColor,
                    contentColor = secondaryFabContentColor
                ) {
                    Icon(
                        Icons.Filled.QrCodeScanner,
                        contentDescription = stringResource(R.string.cd_go_to_scan)
                    )
                }
                FloatingActionButton(
                    onClick = {
                        expanded.value = false
                        navController.navigate("generate")
                    },
                    backgroundColor = secondaryFabColor,
                    contentColor = secondaryFabContentColor
                ) {
                    Icon(
                        Icons.Filled.QrCode,
                        contentDescription = stringResource(R.string.cd_go_to_qr)
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { expanded.value = !expanded.value },
            backgroundColor = fabColor,
            contentColor = fabContentColor
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.cd_add))
        }
    }
}

