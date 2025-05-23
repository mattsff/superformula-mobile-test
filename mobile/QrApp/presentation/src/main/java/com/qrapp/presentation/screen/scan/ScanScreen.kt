package com.qrapp.presentation.screen.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qrapp.presentation.utils.toErrorMessage

@Composable
fun ScanScreen(viewModel: ScanViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error.toErrorMessage(context))
            viewModel.errorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbaºrHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Escanear QR", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onQrCodeScanned("simulated-seed") },
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Escaneando..." else "Simular escaneo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            uiState.scanResult?.let {
                Text("Resultado: ${it.seed}")
            }
        }
    }
}