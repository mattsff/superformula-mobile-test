package com.qrapp.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.qrapp.presentation.R

@Composable
fun QrAppScaffold(
    navController: NavController,
    showBack: Boolean,
    title: String,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (padding: androidx.compose.ui.unit.Dp) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.h6) },
                navigationIcon = if (showBack) {
                    {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.cd_back)
                            )
                        }
                    }
                } else null
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = floatingActionButton,
        content = { innerPadding ->
            content(innerPadding.calculateTopPadding())
        }
    )
}

