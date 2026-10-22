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
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepositoryRoom
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.admin.*
import com.tecsup.lavacampus.ui.components.AdminTab
import com.tecsup.lavacampus.ui.components.SplashScreen
import com.tecsup.lavacampus.ui.components.StudentTab
import com.tecsup.lavacampus.ui.estudiante.*
import com.tecsup.lavacampus.ui.theme.LavaCampusTheme
import com.tecsup.lavacampus.viewmodel.*
import kotlinx.coroutines.launch

sealed class AppScreen {
    object Inicio : AppScreen()
    data class NuevoPedido(val repetirPedidoId: Int? = null) : AppScreen()
    object MisPedidos : AppScreen()
    object Tarifas : AppScreen()
    object Perfil : AppScreen()
    data class DetallePedido(val pedidoId: Int, val origen: AppScreen) : AppScreen()

    object PanelAdmin : AppScreen()
    object BandejaAdmin : AppScreen()
    data class GestionPedidoAdmin(val pedidoId: Int) : AppScreen()
    object ServiciosAdmin : AppScreen()
    data class FormularioServicioAdmin(val servicio: Servicio?) : AppScreen()
    object ClientesAdmin : AppScreen()
    object AjustesAdmin : AppScreen()
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
            val modoOscuro by preferenciasStore.modoOscuro.collectAsState(initial = false)
            val avisos by preferenciasStore.avisos.collectAsState(initial = true)
            val recibirPedidos by preferenciasStore.recibirPedidos.collectAsState(initial = true)
            val scope = rememberCoroutineScope()

            val usuario = uiState.usuario
            val isAdmin = uiState.rolSeleccionado == Rol.ADMIN

            var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Inicio) }

            LaunchedEffect(isAdmin) {
                currentScreen = if (isAdmin) AppScreen.PanelAdmin else AppScreen.Inicio
            }

            LavaCampusTheme(darkTheme = modoOscuro, isAdmin = isAdmin) {
                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    SplashScreen(onTimeout = { showSplash = false })
                } else {
                    when {
                        usuario == null -> {
                            LoginScreen(viewModel = sesionViewModel)
                        }
                        usuario.rol == Rol.ADMIN.name -> {
                            val handleAdminTabSelected: (AdminTab) -> Unit = { tab ->
                                currentScreen = when (tab) {
                                    AdminTab.PANEL -> AppScreen.PanelAdmin
                                    AdminTab.BANDEJA -> AppScreen.BandejaAdmin
                                    AdminTab.SERVICIOS -> AppScreen.ServiciosAdmin
                                    AdminTab.CLIENTES -> AppScreen.ClientesAdmin
                                    AdminTab.AJUSTES -> AppScreen.AjustesAdmin
                                }
                            }

                            when (val screen = currentScreen) {
                                is AppScreen.PanelAdmin -> {
                                    val panelViewModel: PanelViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore),
                                        key = "panel_admin"
                                    )
                                    PanelScreen(
                                        usuario = usuario,
                                        viewModel = panelViewModel,
                                        onTabSelected = handleAdminTabSelected,
                                        onLogout = { sesionViewModel.cerrarSesion() }
                                    )
                                }
                                is AppScreen.BandejaAdmin -> {
                                    val bandejaViewModel: BandejaViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore),
                                        key = "bandeja_admin"
                                    )
                                    BandejaScreen(
                                        viewModel = bandejaViewModel,
                                        onVerGestion = { id -> currentScreen = AppScreen.GestionPedidoAdmin(id) },
                                        onTabSelected = handleAdminTabSelected
                                    )
                                }
                                is AppScreen.GestionPedidoAdmin -> {
                                    val gestionViewModel: GestionPedidoViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore, pedidoId = screen.pedidoId),
                                        key = "gestion_${screen.pedidoId}"
                                    )
                                    GestionPedidoScreen(
                                        viewModel = gestionViewModel,
                                        onBack = { currentScreen = AppScreen.BandejaAdmin }
                                    )
                                }
                                is AppScreen.ServiciosAdmin -> {
                                    val adminServiciosViewModel: AdminServiciosViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore),
                                        key = "admin_servicios"
                                    )
                                    ServiciosScreen(
                                        viewModel = adminServiciosViewModel,
                                        onCrearServicio = { currentScreen = AppScreen.FormularioServicioAdmin(null) },
                                        onEditarServicio = { serv -> currentScreen = AppScreen.FormularioServicioAdmin(serv) },
                                        onTabSelected = handleAdminTabSelected
                                    )
                                }
                                is AppScreen.FormularioServicioAdmin -> {
                                    val adminServiciosViewModel: AdminServiciosViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore),
                                        key = "admin_servicios"
                                    )
                                    FormularioServicioScreen(
                                        servicioExistente = screen.servicio,
                                        viewModel = adminServiciosViewModel,
                                        onBack = { currentScreen = AppScreen.ServiciosAdmin }
                                    )
                                }
                                is AppScreen.ClientesAdmin -> {
                                    val clientesViewModel: ClientesViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore),
                                        key = "clientes_admin"
                                    )
                                    ClientesScreen(
                                        viewModel = clientesViewModel,
                                        onTabSelected = handleAdminTabSelected
                                    )
                                }
                                is AppScreen.AjustesAdmin -> {
                                    AjustesScreen(
                                        usuario = usuario,
                                        modoOscuro = modoOscuro,
                                        onModoOscuroChanged = { active ->
                                            scope.launch { preferenciasStore.setModoOscuro(active) }
                                        },
                                        avisos = avisos,
                                        onAvisosChanged = { active ->
                                            scope.launch { preferenciasStore.setAvisos(active) }
                                        },
                                        recibirPedidos = recibirPedidos,
                                        onRecibirPedidosChanged = { active ->
                                            scope.launch { preferenciasStore.setRecibirPedidos(active) }
                                        },
                                        onCambiarPerfil = {
                                            sesionViewModel.cambiarPerfil(Rol.CLIENTE)
                                            currentScreen = AppScreen.Inicio
                                        },
                                        onLogout = { sesionViewModel.cerrarSesion() },
                                        onTabSelected = handleAdminTabSelected
                                    )
                                }
                                else -> {
                                    val panelViewModel: PanelViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore),
                                        key = "panel_admin"
                                    )
                                    PanelScreen(
                                        usuario = usuario,
                                        viewModel = panelViewModel,
                                        onTabSelected = handleAdminTabSelected,
                                        onLogout = { sesionViewModel.cerrarSesion() }
                                    )
                                }
                            }
                        }
                        usuario.rol == Rol.CLIENTE.name -> {
                            val handleTabSelected: (StudentTab) -> Unit = { tab ->
                                currentScreen = when (tab) {
                                    StudentTab.INICIO -> AppScreen.Inicio
                                    StudentTab.PEDIDOS -> AppScreen.MisPedidos
                                    StudentTab.TARIFAS -> AppScreen.Tarifas
                                    StudentTab.PERFIL -> AppScreen.Perfil
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
                                        onNuevoPedido = { currentScreen = AppScreen.NuevoPedido() },
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
                                is AppScreen.Perfil -> {
                                    val perfilViewModel: PerfilViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore, usuarioId = usuario.id),
                                        key = "perfil_${usuario.id}"
                                    )
                                    PerfilScreen(
                                        usuario = usuario,
                                        viewModel = perfilViewModel,
                                        modoOscuro = modoOscuro,
                                        onModoOscuroChanged = { active ->
                                            scope.launch { preferenciasStore.setModoOscuro(active) }
                                        },
                                        avisos = avisos,
                                        onAvisosChanged = { active ->
                                            scope.launch { preferenciasStore.setAvisos(active) }
                                        },
                                        onCambiarPerfil = {
                                            sesionViewModel.cambiarPerfil(Rol.ADMIN)
                                            currentScreen = AppScreen.PanelAdmin
                                        },
                                        onLogout = { sesionViewModel.cerrarSesion() },
                                        onTabSelected = handleTabSelected
                                    )
                                }
                                is AppScreen.NuevoPedido -> {
                                    val nuevoPedidoViewModel: NuevoPedidoViewModel = viewModel(
                                        factory = LavaViewModelFactory(repository, preferenciasStore, repetirPedidoId = screen.repetirPedidoId),
                                        key = "nuevo_pedido_${screen.repetirPedidoId ?: 0}"
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
                                        },
                                        onRepetirPedido = { id ->
                                            currentScreen = AppScreen.NuevoPedido(repetirPedidoId = id)
                                        }
                                    )
                                }
                                else -> {
                                    currentScreen = AppScreen.Inicio
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
                                    Text(text = "Bienvenido: ${usuario.nombre}")
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
