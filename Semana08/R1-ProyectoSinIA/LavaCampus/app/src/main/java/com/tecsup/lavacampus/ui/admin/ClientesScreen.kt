package com.tecsup.lavacampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.AdminBottomBar
import com.tecsup.lavacampus.ui.components.AdminTab
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.viewmodel.ClientesViewModel
import java.util.Locale

fun obtenerIniciales(nombre: String): String {
    val partes = nombre.trim().split("\\s+".toRegex())
    return if (partes.size >= 2) {
        "${partes[0].firstOrNull() ?: ""}${partes[1].firstOrNull() ?: ""}"
    } else {
        nombre.take(2)
    }.uppercase()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClientesViewModel,
    onTabSelected: (AdminTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val clientesFiltrados = remember(uiState.clientes, uiState.searchQuery) {
        if (uiState.searchQuery.isBlank()) {
            uiState.clientes
        } else {
            uiState.clientes.filter {
                it.nombre.contains(uiState.searchQuery, ignoreCase = true) ||
                it.codigo.contains(uiState.searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Clientes del Sistema")
        },
        bottomBar = {
            AdminBottomBar(
                currentTab = AdminTab.CLIENTES,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.actualizarBusqueda(it) },
                label = { Text("Buscar por nombre o código") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (uiState.cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (clientesFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron clientes")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(clientesFiltrados) { cliente ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = obtenerIniciales(cliente.nombre),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = cliente.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Código: ${cliente.codigo}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Pedidos: ${cliente.cantidadPedidos}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "S/ ${String.format(Locale.getDefault(), "%.2f", cliente.totalGastado)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
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
