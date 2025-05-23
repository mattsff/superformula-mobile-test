package com.qrapp.presentation.components
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.ui.res.stringResource
import com.qrapp.presentation.R


@Composable
fun CameraPermissionsHandler(
    showPermissionDialog: Boolean,
    wasPermissionDenied: Boolean,
    onPermissionUpdated: (Boolean, Boolean) -> Unit,
    onDismissDialog: () -> Unit
) {
    val context = LocalContext.current.getActivity()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            val shouldShowRationale = context?.shouldShowRequestPermissionRationale(
                Manifest.permission.CAMERA
            ) ?: false
            onPermissionUpdated(granted, shouldShowRationale)
        }
    )

    val requestCameraPermission = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }

    if (showPermissionDialog) {
        val shouldShowRequestPermissionRationale =
            context?.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                ?: false
        if (wasPermissionDenied && !shouldShowRequestPermissionRationale) {
            AlertDialog(
                onDismissRequest = { onDismissDialog() },
                title = { Text(stringResource(R.string.camera_permission_denied_title)) },
                text = { Text(stringResource(R.string.camera_permission_denied_message)) },
                confirmButton = {
                    TextButton(onClick = {
                        context?.openAppSettings()
                        onDismissDialog()
                    }) {
                        Text(stringResource(R.string.go_to_settings))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onDismissDialog() }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        } else {
            requestCameraPermission()
            onDismissDialog()
        }
    }
}

