package com.qrapp.presentation.screen.generate

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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.qrapp.presentation.R
import com.qrapp.presentation.components.QrAppScaffold
import com.qrapp.presentation.utils.toErrorMessage

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
            Text(
                stringResource(R.string.qr_generated_code),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            uiState.qrBitmap?.let { bmp ->
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(260.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = stringResource(R.string.qr_generated_code),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(Color.White)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

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
                            text = stringResource(R.string.qr_seed_label),
                            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = uiState.qrSeed?.seed.orEmpty(),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = stringResource(R.string.qr_expires_in, uiState.timeLeft),
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = MaterialTheme.colors.secondary, fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } ?: if (uiState.error == null) {
                Spacer(modifier = Modifier.height(64.dp))
                CircularProgressIndicator()
            } else {
                // Do nothing, error is already handled
            }
        }
    }
}

