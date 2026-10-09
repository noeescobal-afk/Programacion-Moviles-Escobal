package com.saludplus.citas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SaludPlusBlueDarkTheme,
    onPrimary = Color(0xFF002F67),
    primaryContainer = Color(0xFF0A4387),
    onPrimaryContainer = Color(0xFFD8E7FF),

    secondary = SaludPlusGreenDarkTheme,
    secondaryContainer = Color(0xFF14543B),

    tertiary = SaludPlusPinkDarkTheme,

    background = Color(0xFF111318),
    surface = Color(0xFF17191F),
    surfaceVariant = Color(0xFF252932)
)

private val LightColorScheme = lightColorScheme(
    primary = SaludPlusBlue,
    onPrimary = Color.White,
    primaryContainer = SaludPlusBlueLight,
    onPrimaryContainer = SaludPlusBlueDark,

    secondary = SaludPlusGreen,
    onSecondary = Color.White,
    secondaryContainer = SaludPlusGreenLight,
    onSecondaryContainer = Color(0xFF0B5C3C),

    tertiary = SaludPlusPink,
    tertiaryContainer = SaludPlusPinkLight,

    background = SaludPlusBackground,
    onBackground = SaludPlusText,

    surface = SaludPlusSurface,
    onSurface = SaludPlusText,

    surfaceVariant = SaludPlusSurfaceVariant,
    onSurfaceVariant = SaludPlusTextSecondary,

    outline = Color(0xFFD0D5DD)
)

@Composable
fun SaludPlusCitasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}