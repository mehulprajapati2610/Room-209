package com.room209.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AccentPrimary,
    onPrimary = SurfaceElevated,
    primaryContainer = AccentSurface,
    onPrimaryContainer = AccentPrimaryDark,
    secondary = AccentSupporting,
    onSecondary = SurfaceElevated,
    secondaryContainer = AccentSupportingLight,
    onSecondaryContainer = AccentSupporting,
    background = SurfaceCanvas,
    onBackground = TextPrimary,
    surface = SurfaceElevated,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSubtle,
    onSurfaceVariant = TextSecondary,
    outline = BorderHairline,
    outlineVariant = BorderStrong,
    error = ErrorColor,
    onError = SurfaceElevated,
    errorContainer = ErrorContainer
)

@Composable
fun Room209Theme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SurfaceCanvas.toArgb()
            window.navigationBarColor = SurfaceElevated.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
