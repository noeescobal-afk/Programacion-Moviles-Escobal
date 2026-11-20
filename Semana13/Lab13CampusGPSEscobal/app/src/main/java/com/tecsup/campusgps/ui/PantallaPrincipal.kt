package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaPrincipal(
    vm: MapaViewModel,
    temaActual: String = "sistema",
    onCambiarTema: (String) -> Unit = {}
) {
    val pestanaNavegacion by vm.pestanaNavegacion.collectAsState()
    val lugares by vm.lugaresFiltrados.collectAsState()
    val todos by vm.lugares.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val query by vm.query.collectAsState()
    val favoritos by vm.favoritos.collectAsState()
    val recientes by vm.recientes.collectAsState()
    var mostrarAjustes by remember { mutableStateOf(false) }

    val coloresNavegacion = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = pestanaNavegacion == 0,
                    onClick = { vm.cambiarPestanaNavegacion(0) },
                    icon = { Icon(Icons.Default.Place, contentDescription = "Mapa") },
                    label = { Text("Mapa", fontWeight = if (pestanaNavegacion == 0) FontWeight.SemiBold else FontWeight.Normal) },
                    colors = coloresNavegacion
                )
                NavigationBarItem(
                    selected = pestanaNavegacion == 1,
                    onClick = { vm.cambiarPestanaNavegacion(1) },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar", fontWeight = if (pestanaNavegacion == 1) FontWeight.SemiBold else FontWeight.Normal) },
                    colors = coloresNavegacion
                )
                NavigationBarItem(
                    selected = pestanaNavegacion == 2,
                    onClick = { vm.cambiarPestanaNavegacion(2) },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Favoritos") },
                    label = { Text("Favoritos", fontWeight = if (pestanaNavegacion == 2) FontWeight.SemiBold else FontWeight.Normal) },
                    colors = coloresNavegacion
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (pestanaNavegacion) {
                0 -> TabMapa(vm)
                1 -> TabExplorar(
                    lugares = lugares,
                    categoria = categoria,
                    query = query,
                    favoritos = favoritos,
                    onCambiarCategoria = { vm.cambiarCategoria(it) },
                    onActualizarQuery = { vm.actualizarQuery(it) },
                    onToggleFavorito = { vm.toggleFavorito(it) },
                    onSeleccionarLugar = {
                        vm.seleccionar(it)
                        vm.cambiarPestanaNavegacion(0)
                    }
                )
                2 -> TabFavoritos(
                    todosLosLugares = todos,
                    favoritos = favoritos,
                    recientes = recientes,
                    onToggleFavorito = { vm.toggleFavorito(it) },
                    onSeleccionarLugar = {
                        vm.seleccionar(it)
                        vm.cambiarPestanaNavegacion(0)
                    }
                )
            }
            // Icono de ajustes sobre la cabecera azul de Explorar.
            if (pestanaNavegacion == 1) {
                IconButton(
                    onClick = { mostrarAjustes = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 25.dp, end = 25.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustes de apariencia", tint = Color.White)
                }
            }
        }
    }

    if (mostrarAjustes) {
        AlertDialog(
            onDismissRequest = { mostrarAjustes = false },
            title = { Text("Apariencia") },
            text = {
                Column {
                    listOf(
                        "sistema" to "Automático (según el teléfono)",
                        "claro" to "Modo claro",
                        "oscuro" to "Modo oscuro"
                    ).forEach { (valor, titulo) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = temaActual == valor,
                                onClick = {
                                    onCambiarTema(valor)
                                    mostrarAjustes = false
                                }
                            )
                            TextButton(
                                onClick = {
                                    onCambiarTema(valor)
                                    mostrarAjustes = false
                                }
                            ) { Text(titulo) }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarAjustes = false }) { Text("Cerrar") }
            }
        )
    }
}
