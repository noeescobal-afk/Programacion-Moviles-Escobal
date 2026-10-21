package com.tecsup.lavacampus.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val EstudianteLightColorScheme = lightColorScheme(
    primary = EstudiantePrimary,
    primaryContainer = EstudiantePrimaryContainer,
    background = EstudianteBackground,
    surface = EstudianteBackground,
    onPrimary = Color.White,
    onPrimaryContainer = Color(0xFF0E7490),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

private val AdminLightColorScheme = lightColorScheme(
    primary = AdminPrimary,
    primaryContainer = AdminPrimaryContainer,
    background = AdminBackground,
    surface = AdminBackground,
    onPrimary = Color.White,
    onPrimaryContainer = Color(0xFF5B21B6),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

private val DarkColorScheme = darkColorScheme(
    primary = EstudiantePrimary,
    primaryContainer = Color(0xFF155E75),
    background = Color(0xFF0F172A),
    surface = Color(0xFF0F172A),
    onPrimary = Color.White,
    onPrimaryContainer = EstudiantePrimaryContainer,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun LavaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isAdmin: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        isAdmin -> AdminLightColorScheme
        else -> EstudianteLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
