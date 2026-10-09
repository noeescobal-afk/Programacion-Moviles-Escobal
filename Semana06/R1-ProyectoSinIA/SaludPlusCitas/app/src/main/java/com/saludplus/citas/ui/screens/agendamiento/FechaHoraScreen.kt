package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (fecha: String, hora: String) -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    val fechas = listOf(
        "12/10/2026",
        "13/10/2026",
        "14/10/2026",
        "15/10/2026",
        "16/10/2026"
    )

    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Fecha y hora",
                onVolverAtras = onVolverAtras
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Información del médico
            if (medico != null) {
                Text(
                    text = medico.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "CMP: ${medico.cmp}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Médico no encontrado",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Título de selección de fecha
            Text(
                text = "Selecciona una fecha",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selector de fechas con LazyRow
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(fechas) { fecha ->
                    val esSeleccionada = fecha == fechaSeleccionada
                    Card(
                        modifier = Modifier.clickable {
                            fechaSeleccionada = fecha
                            horaSeleccionada = null
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = if (esSeleccionada) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Text(
                            text = fecha,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (esSeleccionada) FontWeight.Bold else FontWeight.Normal,
                            color = if (esSeleccionada) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título de selección de horario
            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sección de horarios con LazyVerticalGrid
            val fechaActual = fechaSeleccionada
            if (fechaActual == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selecciona una fecha para ver los horarios",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val horariosDisponibles = Repositorio.horariosDisponibles(
                    medicoId = medicoId,
                    fecha = fechaActual
                )

                if (horariosDisponibles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        MensajeListaVacia(
                            mensaje = "No hay horarios disponibles para esta fecha"
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(horariosDisponibles) { hora ->
                            val esHoraSeleccionada = hora == horaSeleccionada
                            Card(
                                modifier = Modifier.clickable {
                                    horaSeleccionada = hora
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (esHoraSeleccionada) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hora,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (esHoraSeleccionada) FontWeight.Bold else FontWeight.Normal,
                                        color = if (esHoraSeleccionada) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Continuar
            val habilitado = fechaSeleccionada != null && horaSeleccionada != null
            BotonPrincipal(
                texto = "Continuar",
                enabled = habilitado,
                onClick = {
                    val f = fechaSeleccionada
                    val h = horaSeleccionada
                    if (f != null && h != null) {
                        onContinuar(f, h)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FechaHoraScreenPreview() {
    SaludPlusCitasTheme {
        FechaHoraScreen(
            medicoId = 1,
            onContinuar = { _, _ -> },
            onVolverAtras = {}
        )
    }
}
