package com.tecsup.centrotareas

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.ui.PantallaTareas
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

private const val PREFS_NAME = "centrotareas_prefs"
private const val KEY_DARK_MODE = "is_dark_mode"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setContent {
            var isDarkMode by remember {
                mutableStateOf(prefs.getBoolean(KEY_DARK_MODE, true))
            }

            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = !isDarkMode

            val colorScheme = if (isDarkMode) {
                darkColorScheme(
                    primary = Color(0xFF527BFF),
                    primaryContainer = Color(0xFF1E2E52),
                    secondary = Color(0xFF56F0DC),
                    background = Color(0xFF080D1D),
                    surface = Color(0xFF172442),
                    onSurface = Color(0xFFFFFFFF),
                    onBackground = Color(0xFFFFFFFF)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF2563EB),
                    primaryContainer = Color(0xFFDBEAFE),
                    secondary = Color(0xFF0D9488),
                    background = Color(0xFFF1F5FF),
                    surface = Color(0xFFFFFFFF),
                    onSurface = Color(0xFF172442),
                    onBackground = Color(0xFF172442)
                )
            }

            MaterialTheme(colorScheme = colorScheme) {
                val vm: TareasViewModel = viewModel(factory = TareasViewModelFactory(TareasRepositoryLocal()))
                PantallaTareas(
                    vm = vm,
                    isDarkMode = isDarkMode,
                    onThemeToggle = {
                        val nextDark = !isDarkMode
                        isDarkMode = nextDark
                        prefs.edit().putBoolean(KEY_DARK_MODE, nextDark).apply()
                        windowInsetsController.isAppearanceLightStatusBars = !nextDark
                    }
                )
            }
        }
    }
}
