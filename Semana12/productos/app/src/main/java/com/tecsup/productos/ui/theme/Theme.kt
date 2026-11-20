package com.tecsup.productos.ui.theme

<<<<<<< HEAD
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ProductosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
=======
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CatalogoColors = darkColorScheme(
    primary = Color(0xFFC9FC66),
    onPrimary = Color(0xFF1B2900),
    primaryContainer = Color(0xFF293D1B),
    onPrimaryContainer = Color(0xFFDDFCBA),
    secondary = Color(0xFFB3C6FF),
    onSecondary = Color(0xFF10295C),
    secondaryContainer = Color(0xFF202C54),
    onSecondaryContainer = Color(0xFFDFE5FF),
    background = Color(0xFF0C111D),
    onBackground = Color(0xFFF5F6FF),
    surface = Color(0xFF131B2A),
    onSurface = Color(0xFFF5F6FF),
    surfaceVariant = Color(0xFF242E42),
    onSurfaceVariant = Color(0xFFB8C4DB),
    outline = Color(0xFF77839E),
    outlineVariant = Color(0xFF343F54),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF4B2024),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun ProductosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CatalogoColors,
        typography = Typography,
        content = content
    )
}
>>>>>>> origin/mejora-ia
