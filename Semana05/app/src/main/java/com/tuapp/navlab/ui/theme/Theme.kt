package com.tuapp.navlab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = LightSurface,
    primaryContainer = NavyVariant,
    onPrimaryContainer = LightSurface,
    secondary = TurquoiseSecondary,
    onSecondary = LightSurface,
    secondaryContainer = TurquoiseVariant.copy(alpha = 0.2f),
    onSecondaryContainer = NavyPrimary,
    background = LightBackground,
    onBackground = OnSurfaceText,
    surface = LightSurface,
    onSurface = OnSurfaceText,
    surfaceVariant = LightBackground,
    onSurfaceVariant = OnSurfaceVariantText
)

@Composable
fun NavLabTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
