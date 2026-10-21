package com.tecsup.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepositoryRoom
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.components.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.InicioScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.theme.LavaCampusTheme
import com.tecsup.lavacampus.viewmodel.InicioViewModel
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
            val sesionViewModel: SesionViewModel = viewModel(
                factory = LavaViewModelFactory(repository, preferenciasStore)
            )
            val uiState by sesionViewModel.uiState.collectAsState()

            val usuario = uiState.usuario
            val isAdmin = uiState.rolSeleccionado == Rol.ADMIN

            LavaCampusTheme(isAdmin = isAdmin) {
                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    SplashScreen(onTimeout = { showSplash = false })
                } else {
                    when {
                        usuario == null -> {
                            LoginScreen(viewModel = sesionViewModel)
                        }
                        usuario.rol == Rol.CLIENTE.name -> {
                            val inicioViewModel: InicioViewModel = viewModel(
                                factory = LavaViewModelFactory(repository, preferenciasStore, usuario.id),
                                key = "inicio_${usuario.id}"
                            )
                            InicioScreen(
                                usuario = usuario,
                                viewModel = inicioViewModel,
                                onNuevoPedido = { /* TODO: Step 2 */ }
                            )
                        }
                        else -> {
                            Surface(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "Bienvenido Administrador: ${usuario.nombre}")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { sesionViewModel.cerrarSesion() }) {
                                        Text("Cerrar Sesión")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
