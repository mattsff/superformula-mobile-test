package com.qrapp.presentation.screen.generate

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.qrapp.domain.model.QrSeed
import com.qrapp.presentation.R
import com.qrapp.presentation.components.QrAppScaffold
import com.qrapp.presentation.utils.toErrorMessage
import java.time.Instant

/**
 * Screen for generating and displaying a QR code.
 * Handles loading, error, and success states.
 */
@Composable
fun GenerateScreen(
    navController: NavController,
    viewModel: QrViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error as snackbar if present
    val errorMessage = uiState.error?.toErrorMessage(context)
    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.errorShown()
        }
    }

    QrAppScaffold(
        navController = navController,
        showBack = true,
        title = stringResource(R.string.qr_title),
        snackbarHostState = snackbarHostState
    ) { _ ->
        GenerateScreenContent(uiState = uiState)
    }
}

@VisibleForTesting
@Composable
internal fun GenerateScreenContent(uiState: QrViewModel.QrUiState) {
    val qrGeneratedCode = stringResource(R.string.qr_generated_code)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            qrGeneratedCode,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when {
            uiState.qrBitmap != null -> {
                QrBitmapCard(
                    bitmap = uiState.qrBitmap.asImageBitmap(),
                    contentDescription = qrGeneratedCode
                )
                Spacer(modifier = Modifier.height(24.dp))
                QrSeedInfo(
                    qrSeed = uiState.qrSeed,
                    timeLeft = uiState.timeLeft
                )
            }
            uiState.error == null -> {
                Spacer(modifier = Modifier.height(64.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@VisibleForTesting
@Composable
internal fun QrBitmapCard(
    bitmap: androidx.compose.ui.graphics.ImageBitmap,
    contentDescription: String
) {
    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(260.dp)
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        )
    }
}

@VisibleForTesting
@Composable
internal fun QrSeedInfo(
    qrSeed: QrSeed?,
    timeLeft: String
) {
    val qrSeedLabel = stringResource(R.string.qr_seed_label)
    val expiresIn = stringResource(R.string.qr_expires_in, timeLeft)

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colors.primary.copy(alpha = 0.08f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = qrSeedLabel,
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.primary
            )
            Text(
                text = qrSeed?.seed.orEmpty(),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = expiresIn,
                style = MaterialTheme.typography.subtitle1.copy(
                    color = MaterialTheme.colors.secondary, fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGenerateScreenContent_Loading() {
    GenerateScreenContent(
        uiState = QrViewModel.QrUiState(
            qrBitmap = null,
            qrSeed = null,
            timeLeft = "-",
            error = null
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewGenerateScreenContent_Success() {
    val bmp = androidx.compose.ui.graphics.ImageBitmap(100, 100)
    GenerateScreenContent(
        uiState = QrViewModel.QrUiState(
            qrBitmap = bmp.asAndroidBitmap(),
            qrSeed = QrSeed(seed = "1234567890", expiresAt = Instant.MIN),
            timeLeft = "30",
            error = null
        )
    )
}
