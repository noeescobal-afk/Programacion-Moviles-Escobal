package com.tecsup.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.tecsup.lavacampus.ui.navigation.NavGraph
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rolActual by viewModel.rolActual.collectAsState()

            LavaTheme(rol = rolActual) {
                NavGraph(
                    rol = rolActual,
                    onCambiarRol = { nuevoRol ->
                        viewModel.cambiarRol(nuevoRol)
                    }
                )
            }
        }
    }
}
