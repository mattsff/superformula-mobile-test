package com.qrapp.presentation.screen.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.qrapp.domain.model.QrScanResult
import com.qrapp.presentation.R
import com.qrapp.presentation.components.CameraPermissionsHandler
import com.qrapp.presentation.components.QrAppScaffold
import com.qrapp.presentation.components.QrCodeScannerView
import com.qrapp.presentation.utils.toErrorMessage

/**
 * Main screen for scanning QR codes.
 * Handles camera permissions, scanning, error/result display, and scan reset.
 */
@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    CameraPermissionsHandler(
        showPermissionDialog = uiState.showPermissionDialog,
        wasPermissionDenied = uiState.wasPermissionDenied,
        onPermissionUpdated = { granted, shouldShowRationale ->
            viewModel.onCameraPermissionResult(granted, shouldShowRationale)
        },
        onDismissDialog = { viewModel.dismissCameraPermissionsDialog() }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.showCameraPermissionDialog()
        }
    }

    QrAppScaffold(
        navController = navController,
        showBack = true,
        title = stringResource(R.string.scan_title),
        snackbarHostState = snackbarHostState
    ) { _ ->
        ScanScreenContent(
            uiState = uiState,
            onQrCodeScanned = { code -> viewModel.onQrCodeScanned(code) },
            onScanAgain = { viewModel.resetScan() },
            context = context
        )
    }
}

@VisibleForTesting
@Composable
internal fun ScanScreenContent(
    uiState: ScanViewModel.ScanUiState,
    onQrCodeScanned: (String) -> Unit,
    onScanAgain: () -> Unit,
    context: android.content.Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val scanQrCodeLabel = stringResource(R.string.scan_qr_code)
        Text(
            text = scanQrCodeLabel,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (!uiState.hasScanResult && !uiState.isLoading && uiState.error == null) {
            QrCodeScannerView(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                onQrCodeScanned = onQrCodeScanned,
                shouldReset = uiState.shouldResetScanner
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.scanResult?.let { result ->
            ScanResultCard(
                result = result,
                onScanAgain = onScanAgain
            )
        }

        uiState.error?.let { error ->
            ScanErrorCard(
                errorMessage = error.toErrorMessage(context),
                onScanAgain = onScanAgain
            )
        }
    }
}

@VisibleForTesting
@Composable
internal fun ScanResultCard(
    result: QrScanResult,
    onScanAgain: () -> Unit
) {
    val scanResultLabel = stringResource(R.string.scan_result_label)
    val scanValidQr = stringResource(R.string.scan_valid_qr)
    val scanInvalidQr = stringResource(R.string.scan_invalid_qr)
    val scanAgain = stringResource(R.string.scan_again)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = scanResultLabel,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (result.isValid) {
                Text(
                    text = scanValidQr,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = Color.Green,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                Text(
                    text = scanInvalidQr,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                result.reason?.takeIf { it.isNotBlank() }?.let { reason ->
                    Text(
                        text = reason,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onScanAgain,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = scanAgain)
            }
        }
    }
}

@VisibleForTesting
@Composable
internal fun ScanErrorCard(
    errorMessage: String,
    onScanAgain: () -> Unit
) {
    val scanErrorLabel = stringResource(R.string.scan_error_label)
    val scanAgain = stringResource(R.string.scan_again)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = scanErrorLabel,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onScanAgain,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = scanAgain)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScanResultCard_Valid() {
    ScanResultCard(
        result = QrScanResult(isValid = true, reason = null),
        onScanAgain = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewScanResultCard_Invalid() {
    ScanResultCard(
        result = QrScanResult(isValid = false, reason = "Invalid format"),
        onScanAgain = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewScanErrorCard() {
    ScanErrorCard(
        errorMessage = "Network error occurred",
        onScanAgain = {}
    )
}
