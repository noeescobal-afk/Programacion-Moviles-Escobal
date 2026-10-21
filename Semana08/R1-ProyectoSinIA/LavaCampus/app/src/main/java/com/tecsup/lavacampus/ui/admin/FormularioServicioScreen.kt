package com.tecsup.lavacampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.viewmodel.AdminServiciosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioServicioScreen(
    servicioExistente: Servicio?,
    viewModel: AdminServiciosViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf(servicioExistente?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(servicioExistente?.descripcion ?: "") }
    var unidad by remember { mutableStateOf(servicioExistente?.unidad ?: Unidad.KG.name) }
    var precio by remember { mutableStateOf(servicioExistente?.precio?.toString() ?: "") }
    var horasEntrega by remember { mutableStateOf(servicioExistente?.horasEntrega?.toString() ?: "24") }
    var activo by remember { mutableStateOf(servicioExistente?.activo ?: true) }
    var icono by remember { mutableStateOf(servicioExistente?.icono ?: "wash") }

    val uiState by viewModel.uiState.collectAsState()

    val unidadesPermitidas = listOf(Unidad.KG.name, Unidad.PRENDA.name, Unidad.UNIDAD.name)
    val iconosValidos = listOf("wash", "iron", "shirt")

    var expandedUnidad by remember { mutableStateOf(false) }
    var expandedIcono by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (servicioExistente == null) "Nuevo Servicio" else "Editar Servicio",
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del servicio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            ExposedDropdownMenuBox(
                expanded = expandedUnidad,
                onExpandedChange = { expandedUnidad = !expandedUnidad }
            ) {
                OutlinedTextField(
                    value = unidad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Unidad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnidad) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                )
                ExposedDropdownMenu(
                    expanded = expandedUnidad,
                    onDismissRequest = { expandedUnidad = false }
                ) {
                    unidadesPermitidas.forEach { u ->
                        DropdownMenuItem(
                            text = { Text(u) },
                            onClick = {
                                unidad = u
                                expandedUnidad = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (S/)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = horasEntrega,
                onValueChange = { horasEntrega = it },
                label = { Text("Horas de entrega") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = expandedIcono,
                onExpandedChange = { expandedIcono = !expandedIcono }
            ) {
                OutlinedTextField(
                    value = icono,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Icono (wash, iron, shirt)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedIcono) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                )
                ExposedDropdownMenu(
                    expanded = expandedIcono,
                    onDismissRequest = { expandedIcono = false }
                ) {
                    iconosValidos.forEach { ico ->
                        DropdownMenuItem(
                            text = { Text(ico) },
                            onClick = {
                                icono = ico
                                expandedIcono = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Servicio activo")
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            if (uiState.mensajeError != null) {
                Text(
                    text = uiState.mensajeError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LavaButton(
                text = "Guardar Servicio",
                onClick = {
                    viewModel.guardarServicio(
                        id = servicioExistente?.id ?: 0,
                        nombre = nombre,
                        descripcion = descripcion,
                        unidad = unidad,
                        precioStr = precio,
                        horasStr = horasEntrega,
                        activo = activo,
                        icono = icono,
                        onSuccess = onBack
                    )
                }
            )
        }
    }
}
