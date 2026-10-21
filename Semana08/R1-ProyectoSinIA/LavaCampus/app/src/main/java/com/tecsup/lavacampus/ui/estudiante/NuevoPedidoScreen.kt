package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.SelectorCantidad
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoPedidoScreen(
    usuarioId: Int,
    viewModel: NuevoPedidoViewModel,
    onBack: () -> Unit,
    onPedidoCreado: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val total = remember(uiState.cantidades, uiState.servicios) {
        uiState.cantidades.entries.sumOf { (servId, cant) ->
            val servicio = uiState.servicios.find { it.id == servId }
            val precio = servicio?.precio ?: 0.0
            cant * precio
        }
    }

    val isValid = remember(uiState.cantidades, uiState.recibirPedidos) {
        uiState.recibirPedidos && uiState.cantidades.values.any { it > 0.0 }
    }

    val fechaEntrega = remember(uiState.cantidades, uiState.servicios) {
        var maxHoras = 0
        for ((servId, cant) in uiState.cantidades) {
            if (cant > 0.0) {
                val servicio = uiState.servicios.find { it.id == servId }
                if (servicio != null && servicio.horasEntrega > maxHoras) {
                    maxHoras = servicio.horasEntrega
                }
            }
        }
        val horas = if (maxHoras > 0) maxHoras else 24
        val dias = horas / 24
        var fecha = LocalDate.now().plusDays(dias.toLong())
        if (fecha.dayOfWeek == DayOfWeek.SUNDAY) {
            fecha = fecha.plusDays(1)
        }
        fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Nuevo pedido",
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    if (!uiState.recibirPedidos) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "La lavandería no está recibiendo pedidos",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    items(uiState.servicios) { servicio ->
                        val cantidadActual = uiState.cantidades[servicio.id] ?: 0.0

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconoServicio(icono = servicio.icono)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = servicio.nombre,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = servicio.descripcion,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "S/ ${String.format(Locale.getDefault(), "%.2f", servicio.precio)} por ${servicio.unidad.lowercase()} • ${servicio.horasEntrega} h",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Cantidad (${servicio.unidad}):",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    SelectorCantidad(
                                        cantidad = cantidadActual,
                                        unidad = servicio.unidad,
                                        onCantidadChanged = { nuevaCantidad ->
                                            viewModel.actualizarCantidad(servicio.id, nuevaCantidad)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.nota,
                            onValueChange = { viewModel.actualizarNota(it) },
                            label = { Text("Nota para la lavandería (opcional)") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }

                    if (uiState.error != null) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Fecha estimada de entrega:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = fechaEntrega,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total estimado:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "S/ ${String.format(Locale.getDefault(), "%.2f", total)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        LavaButton(
                            text = if (uiState.guardando) "Creando pedido..." else "Confirmar pedido",
                            onClick = {
                                viewModel.confirmarPedido(usuarioId, onComplete = onPedidoCreado)
                            },
                            enabled = isValid && !uiState.guardando
                        )
                    }
                }
            }
        }
    }
}
