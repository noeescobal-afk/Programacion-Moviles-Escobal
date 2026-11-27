package com.tecsup.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.avisoscampus.model.ModoTema

private val DarkColorScheme = darkColorScheme(
    primary = CelesteLight,
    onPrimary = NavyDark,
    primaryContainer = CelesteContainerDark,
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = CelestePrimary,
    onSecondary = Color.White,
    background = NavyDark,
    onBackground = TextPrimaryDark,
    surface = NavySurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = NavySurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF334155),
    error = Color(0xFFF87171),
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFECDD3)
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = CelestePrimary,
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFCBD5E1),
    error = Color(0xFFDC2626),
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B)
)

@Composable
fun AvisosCampusTheme(
    modoTema: ModoTema = ModoTema.AUTOMATICO,
    content: @Composable () -> Unit
) {
    val darkTheme = when (modoTema) {
        ModoTema.AUTOMATICO -> isSystemInDarkTheme()
        ModoTema.CLARO -> false
        ModoTema.OSCURO -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
