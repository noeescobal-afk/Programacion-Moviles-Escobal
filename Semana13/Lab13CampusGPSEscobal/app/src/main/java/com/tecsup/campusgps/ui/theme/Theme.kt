package com.tecsup.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

// El mapa consulta este valor para seguir la preferencia de la aplicación.
val LocalCampusDarkTheme = compositionLocalOf { false }

private val ColoresClaros = lightColorScheme(
    primary = AzulTecsup,
    onPrimary = Blanco,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulProfundo,
    secondary = AzulProfundo,
    onSecondary = Blanco,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    outline = Color(0xFFD9DEEA)
)


private val ColoresOscuros = darkColorScheme(
    primary = Color(0xFF83BFFF),
    onPrimary = Color(0xFF082B53),

    primaryContainer = Color(0xFF244C80),
    onPrimaryContainer = Color(0xFFEAF3FF),

    secondary = Color(0xFF72D4E8),
    onSecondary = Color(0xFF052D39),

    background = Color(0xFF101C30),
    onBackground = Color(0xFFEAF1FA),

    surface = Color(0xFF1D2B43),
    onSurface = Color(0xFFEAF1FA),

    surfaceVariant = Color(0xFF2A3D56),
    onSurfaceVariant = Color(0xFFC2D2E5),

    outline = Color(0xFF71859F),
    outlineVariant = Color(0xFF405570)
)


@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalCampusDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = if (darkTheme) ColoresOscuros else ColoresClaros,
            content = content
        )
    }
}
