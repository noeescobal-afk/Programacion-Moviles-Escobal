package com.tecsup.campusgps

// ARCHIVO: MainActivity.kt
// Punto de entrada Android. Construye el ViewModel mediante la Factory y entrega el estado a Compose.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.campusgps.viewmodel.*
import com.tecsup.campusgps.ui.PantallaMapa
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme=if(isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
            val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(applicationContext)); PantallaMapa(vm)
        } }
    }
}
