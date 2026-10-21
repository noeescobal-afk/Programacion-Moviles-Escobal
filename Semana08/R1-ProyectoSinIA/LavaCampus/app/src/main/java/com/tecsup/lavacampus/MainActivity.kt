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
import com.tecsup.lavacampus.ui.components.StudentTab
import com.tecsup.lavacampus.ui.estudiante.*
import com.tecsup.lavacampus.ui.theme.LavaCampusTheme
import com.tecsup.lavacampus.viewmodel.*

sealed class AppScreen {
    object Inicio : AppScreen()
    object NuevoPedido : AppScreen()
    object MisPedidos : AppScreen()
    object Tarifas : AppScreen()
    data class DetallePedido(val pedidoId: Int, val origen: AppScreen) : AppScreen()
}

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

            var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Inicio) }

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
                            val handleTabSelected: (StudentTab) -> Unit = { tab ->
                                currentScreen = when (tab) {
                                    StudentTab.INICIO -> AppScreen.Inicio
                                    StudentTab.PEDIDOS -> AppScreen.MisPedidos
                                    StudentTab.TARIFAS -> AppScreen.Tarifas
                                    StudentTab.PERFIL -> AppScreen.Inicio
                                }
                            }

                            when (val screen = currentScreen) {
                                is AppScreen.Inicio -> {
                                    val inicioViewModel: InicioViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore, usuario.id),
                                        key = "inicio_${usuario.id}"
                                    )
                                    InicioScreen(
                                        usuario = usuario,
                                        viewModel = inicioViewModel,
                                        onNuevoPedido = { currentScreen = AppScreen.NuevoPedido },
                                        onVerDetalle = { id -> currentScreen = AppScreen.DetallePedido(id, AppScreen.Inicio) },
                                        onTabSelected = handleTabSelected
                                    )
                                }
                                is AppScreen.MisPedidos -> {
                                    val misPedidosViewModel: MisPedidosViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore, usuarioId = usuario.id),
                                        key = "mis_pedidos_${usuario.id}"
                                    )
                                    MisPedidosScreen(
                                        usuario = usuario,
                                        viewModel = misPedidosViewModel,
                                        onVerDetalle = { id -> currentScreen = AppScreen.DetallePedido(id, AppScreen.MisPedidos) },
                                        onTabSelected = handleTabSelected
                                    )
                                }
                                is AppScreen.Tarifas -> {
                                    val tarifasViewModel: TarifasViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore)
                                    )
                                    TarifasScreen(
                                        viewModel = tarifasViewModel,
                                        onTabSelected = handleTabSelected
                                    )
                                }
                                is AppScreen.NuevoPedido -> {
                                    val nuevoPedidoViewModel: NuevoPedidoViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore)
                                    )
                                    NuevoPedidoScreen(
                                        usuarioId = usuario.id,
                                        viewModel = nuevoPedidoViewModel,
                                        onBack = { currentScreen = AppScreen.Inicio },
                                        onPedidoCreado = { currentScreen = AppScreen.MisPedidos }
                                    )
                                }
                                is AppScreen.DetallePedido -> {
                                    val detalleViewModel: DetallePedidoViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore, usuarioId = usuario.id, pedidoId = screen.pedidoId),
                                        key = "detalle_${screen.pedidoId}"
                                    )
                                    DetallePedidoScreen(
                                        viewModel = detalleViewModel,
                                        onBack = {
                                            currentScreen = when (screen.origen) {
                                                is AppScreen.MisPedidos -> AppScreen.MisPedidos
                                                else -> AppScreen.Inicio
                                            }
                                        }
                                    )
                                }
                            }
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
