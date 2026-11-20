package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.campusgps.ui.PantallaBienvenida
import com.tecsup.campusgps.ui.PantallaPrincipal
import com.tecsup.campusgps.ui.theme.CampusGPSTheme
import com.tecsup.campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.campusgps.viewmodel.MapaViewModel
import kotlinx.coroutines.delay
import org.maplibre.android.MapLibre

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(applicationContext)

        // Almacena solo la preferencia visual del usuario, sin tocar datos del campus.
        val preferencias = getSharedPreferences("campusgps_apariencia", MODE_PRIVATE)

        setContent {
            val temaSistemaOscuro = isSystemInDarkTheme()
            var temaElegido by remember {
                mutableStateOf(preferencias.getString("tema", "sistema") ?: "sistema")
            }
            val modoOscuro = when (temaElegido) {
                "oscuro" -> true
                "claro" -> false
                else -> temaSistemaOscuro
            }

            CampusGPSTheme(darkTheme = modoOscuro) {
                val vm: MapaViewModel = viewModel(
                    factory = CampusViewModelFactory(applicationContext)
                )
                var mostrarBienvenida by rememberSaveable { mutableStateOf(true) }
                LaunchedEffect(mostrarBienvenida) {
                    if (mostrarBienvenida) {
                        delay(1700)
                        mostrarBienvenida = false
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    PantallaPrincipal(
                        vm = vm,
                        temaActual = temaElegido,
                        onCambiarTema = { nuevoTema ->
                            temaElegido = nuevoTema
                            preferencias.edit().putString("tema", nuevoTema).apply()
                        }
                    )
                    if (mostrarBienvenida) PantallaBienvenida()
                }
            }
        }
    }
}
