package com.tecsup.lavacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.lavacampus.model.Rol

// Color Schemes Estudiante (Turquesa #0891B2)
private val LightEstudianteColorScheme = lightColorScheme(
    primary = TurquesaEstudiante,
    secondary = TurquesaEstudianteSecundario,
    tertiary = TurquesaEstudianteTerciario,
    primaryContainer = TurquesaContenedor,
    onPrimary = Color.White
)

private val DarkEstudianteColorScheme = darkColorScheme(
    primary = TurquesaEstudianteTerciario,
    secondary = TurquesaEstudianteSecundario,
    tertiary = TurquesaEstudiante,
    onPrimary = Color.Black
)

// Color Schemes Administrador (Violeta #6D28D9)
private val LightAdminColorScheme = lightColorScheme(
    primary = VioletaAdmin,
    secondary = VioletaAdminSecundario,
    tertiary = VioletaAdminTerciario,
    primaryContainer = VioletaContenedor,
    onPrimary = Color.White
)

private val DarkAdminColorScheme = darkColorScheme(
    primary = VioletaAdminTerciario,
    secondary = VioletaAdminSecundario,
    tertiary = VioletaAdmin,
    onPrimary = Color.Black
)

@Composable
fun LavaTheme(
    rol: Rol = Rol.ESTUDIANTE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (rol) {
        Rol.ESTUDIANTE -> if (darkTheme) DarkEstudianteColorScheme else LightEstudianteColorScheme
        Rol.ADMINISTRADOR -> if (darkTheme) DarkAdminColorScheme else LightAdminColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun LavaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    LavaTheme(rol = Rol.ESTUDIANTE, darkTheme = darkTheme, content = content)
}
