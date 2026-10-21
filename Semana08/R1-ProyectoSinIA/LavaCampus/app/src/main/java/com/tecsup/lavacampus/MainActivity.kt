package com.tecsup.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepositoryRoom
import com.tecsup.lavacampus.ui.components.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.theme.LavaCampusTheme
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = LavaDatabase.getDatabase(applicationContext)
        val repository = LavaRepositoryRoom(database)
        val preferenciasStore = PreferenciasStore(applicationContext)

        setContent {
            val viewModel: SesionViewModel = viewModel(
                factory = LavaViewModelFactory(repository, preferenciasStore)
            )
            val uiState by viewModel.uiState.collectAsState()

            LavaCampusTheme(isAdmin = uiState.rolSeleccionado == com.tecsup.lavacampus.model.Rol.ADMIN) {
                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    SplashScreen(onTimeout = { showSplash = false })
                } else {
                    LoginScreen(viewModel = viewModel)
                }
            }
        }
    }
}
