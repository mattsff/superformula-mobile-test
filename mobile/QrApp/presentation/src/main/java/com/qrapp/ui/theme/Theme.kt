package com.qrapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.qrapp.ui.theme.*

private val DarkColorPalette = darkColors(
    primary = BluePrimaryDark,
    primaryVariant = BluePrimaryVariantDark,
    secondary = BlueSecondaryDark,
    background = BlueBackgroundDark,
    surface = BlueSurfaceDark,
    error = BlueErrorDark,
    onPrimary = BlueOnPrimaryDark,
    onSecondary = BlueOnSecondaryDark,
    onBackground = BlueOnBackgroundDark,
    onSurface = BlueOnSurfaceDark,
    onError = BlueOnErrorDark
)

private val LightColorPalette = lightColors(
    primary = BluePrimaryLight,
    primaryVariant = BluePrimaryVariantLight,
    secondary = BlueSecondaryLight,
    background = BlueBackgroundLight,
    surface = BlueSurfaceLight,
    error = BlueErrorLight,
    onPrimary = BlueOnPrimaryLight,
    onSecondary = BlueOnSecondaryLight,
    onBackground = BlueOnBackgroundLight,
    onSurface = BlueOnSurfaceLight,
    onError = BlueOnErrorLight
)

@Composable
fun QrAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

