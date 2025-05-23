package com.qrapp.presentation.screen.generate

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.qrapp.presentation.R
import com.qrapp.presentation.utils.generateQrCodeBitmap
import com.qrapp.presentation.utils.toErrorMessage
import com.qrapp.presentation.components.QrAppScaffold

@Composable
fun GenerateScreen(navController: NavController, viewModel: QrViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(error.toErrorMessage(context))
            viewModel.errorShown()
        }
    }

    QrAppScaffold(
        navController = navController,
        showBack = true,
        title = stringResource(R.string.qr_title),
        snackbarHostState = snackbarHostState
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.qr_generated_code), style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))

            Column (
                modifier = Modifier.fillMaxSize(),
            ) {
                uiState.qrBitmap?.let { bmp ->
                    Image(bitmap = bmp.asImageBitmap(), contentDescription = null)
                    Spacer(modifier = Modifier.height(8.dp))
                    uiState.qrSeed?.let { seed ->
                        Text(seed.seed)
                        Text("${seed.expiresAt}")
                        Text(stringResource(R.string.qr_expires_in, uiState.timeLeft))
                    }
                } ?: CircularProgressIndicator()
            }
        }
    }
}
