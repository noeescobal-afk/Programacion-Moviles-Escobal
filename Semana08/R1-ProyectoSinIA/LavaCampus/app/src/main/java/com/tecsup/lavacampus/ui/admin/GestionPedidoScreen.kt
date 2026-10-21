package com.tecsup.lavacampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Casilleros
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionPedidoScreen(
    viewModel: GestionPedidoViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pedidoConDetalle = uiState.pedidoConDetalle
    val pedido = pedidoConDetalle?.pedido
    val usuario = pedidoConDetalle?.usuario

    var showCancelDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (pedido != null) "Gestión - Pedido #${pedido.id}" else "Gestión de Pedido",
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
        } else if (pedido == null || pedidoConDetalle == null || usuario == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró el pedido")
            }
        } else {
            val estadoActual = pedido.estado
            val esRecibido = estadoActual == EstadoPedido.RECIBIDO.name
            val esEnProceso = estadoActual == EstadoPedido.EN_PROCESO.name
            val esListo = estadoActual == EstadoPedido.LISTO.name
            val esCancelado = estadoActual == EstadoPedido.CANCELADO.name

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Cliente: ${usuario.nombre}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Código: ${usuario.codigo.ifBlank { "N/A" }} • ${usuario.correo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            EstadoPedidoChip(estado = estadoActual)
                        }

                        if (!esCancelado) {
                            Spacer(modifier = Modifier.height(4.dp))
                            StepperEstado(estadoActual = estadoActual)
                        }
                    }
                }

                if (esEnProceso || esListo) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (esListo) "Casillero Asignado" else "Asignar Casillero (Obligatorio para pasar a Listo)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            if (esListo) {
                                Text(
                                    text = pedido.casillero ?: "Sin casillero",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                var expanded by remember { mutableStateOf(false) }
                                val casilleroActual = uiState.casilleroSeleccionado ?: "Seleccionar Casillero"

                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }
                                ) {
                                    OutlinedTextField(
                                        value = casilleroActual,
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        Casilleros.todos.forEach { casillero ->
                                            val ocupado = uiState.casillerosOcupados.contains(casillero) && casillero != pedido.casillero
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = if (ocupado) "$casillero (Ocupado)" else casillero,
                                                        color = if (ocupado) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                                    )
                                                },
                                                onClick = {
                                                    if (!ocupado) {
                                                        viewModel.seleccionarCasillero(casillero)
                                                        expanded = false
                                                    }
                                                },
                                                enabled = !ocupado
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Estado de Pago",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (pedido.pagado) "Pagado" else "Pendiente de pago",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (pedido.pagado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Switch(
                            checked = pedido.pagado,
                            onCheckedChange = { nuevoPago ->
                                viewModel.marcarPagado(nuevoPago)
                            }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Servicios del Pedido",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider()

                        pedidoConDetalle.detalles.forEach { detalleConServicio ->
                            val detalle = detalleConServicio.detalle
                            val servicio = detalleConServicio.servicio

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconoServicio(icono = servicio.icono)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = servicio.nombre,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    val cantStr = if (servicio.unidad == Unidad.KG.name) {
                                        String.format(Locale.getDefault(), "%.1f kg", detalle.cantidad)
                                    } else {
                                        "${detalle.cantidad.toInt()} unidades"
                                    }
                                    Text(
                                        text = "$cantStr • S/ ${String.format(Locale.getDefault(), "%.2f", detalle.precioUnitario)} c/u",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "S/ ${String.format(Locale.getDefault(), "%.2f", detalle.subtotal)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "S/ ${String.format(Locale.getDefault(), "%.2f", pedido.total)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (uiState.mensajeExito != null) {
                    Text(
                        text = uiState.mensajeExito!!,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (esRecibido || esEnProceso || esListo) {
                        val siguienteTexto = when {
                            esRecibido -> "Avanzar a En Proceso"
                            esEnProceso -> "Avanzar a Listo (Asignar Casillero)"
                            esListo -> "Avanzar a Entregado"
                            else -> ""
                        }
                        Button(
                            onClick = { viewModel.avanzarEstado() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = siguienteTexto)
                        }
                    }

                    if (esRecibido || esEnProceso) {
                        Button(
                            onClick = { showCancelDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(text = "Cancelar pedido")
                        }
                    }
                }
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("¿Cancelar pedido?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        viewModel.cancelarPedido(onSuccess = onBack)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar pedido")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Volver")
                }
            }
        )
    }
}
