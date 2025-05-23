package com.qrapp.presentation.screen.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.qrapp.presentation.R
import com.qrapp.presentation.components.CameraPermissionsHandler
import com.qrapp.presentation.components.QrAppScaffold
import com.qrapp.presentation.utils.toErrorMessage
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.util.Size
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

@Composable
fun ScanScreen(navController: NavController, viewModel: ScanViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error.toErrorMessage(context))
            viewModel.errorShown()
        }
    }

    CameraPermissionsHandler(
        showPermissionDialog = uiState.showPermissionDialog,
        wasPermissionDenied = uiState.wasPermissionDenied,
        onPermissionUpdated = { granted, shouldShowRationale ->
            viewModel.onCameraPermissionResult(granted, shouldShowRationale)
        },
        onDismissDialog = { viewModel.dismissCameraPermissionsDialog() }
    )

    QrAppScaffold(
        navController = navController,
        showBack = true,
        title = stringResource(R.string.scan_title),
        snackbarHostState = snackbarHostState
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.scan_qr_code), style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            uiState.scanResult?.let {
                Text(stringResource(R.string.scan_result, it.seed))
            }
        }
    }
}

