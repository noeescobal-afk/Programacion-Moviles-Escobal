package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onVolverAtras: () -> Unit,
    onCitaCancelada: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = if (cita != null) Repositorio.obtenerMedico(cita.medicoId) else null
    val especialidad = if (medico != null) Repositorio.obtenerEspecialidad(medico.especialidadId) else null

    var mostrarDialogoCancelacion by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Detalle de cita",
                onVolverAtras = onVolverAtras
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cita != null) {
                // Tarjeta de información completa de la cita
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Información de la cita",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        FilaDetalleCita(
                            etiqueta = "Especialidad:",
                            valor = especialidad?.nombre ?: "No disponible"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalleCita(
                            etiqueta = "Médico:",
                            valor = medico?.nombre ?: "No disponible"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalleCita(
                            etiqueta = "CMP:",
                            valor = medico?.cmp ?: "No disponible"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalleCita(
                            etiqueta = "Fecha:",
                            valor = cita.fecha
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalleCita(
                            etiqueta = "Hora:",
                            valor = cita.hora
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalleCita(
                            etiqueta = "Estado:",
                            valor = cita.estado
                        )
                    }
                }

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Botón de Cancelación (Solo visible si NO está cancelada)
                if (cita.estado != "Cancelada") {
                    Button(
                        onClick = { mostrarDialogoCancelacion = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Cancelar cita",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Estado cita inexistente
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "No se encontró la cita",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "La reserva solicitada no existe o no se pudo recuperar la información.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // Diálogo de Confirmación de Cancelación
    if (mostrarDialogoCancelacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCancelacion = false },
            title = {
                Text(
                    text = "Cancelar cita",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = "¿Estás seguro de que deseas cancelar esta cita?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCancelacion = false
                        val cancelada = Repositorio.cancelarCita(citaId)
                        if (cancelada) {
                            mensajeError = ""
                            onCitaCancelada()
                        } else {
                            mensajeError = "No se pudo cancelar la cita."
                        }
                    }
                ) {
                    Text(
                        text = "Sí, cancelar",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoCancelacion = false }
                ) {
                    Text(text = "No")
                }
            }
        )
    }
}

@Composable
private fun FilaDetalleCita(
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleCitaScreenPreview() {
    SaludPlusCitasTheme {
        DetalleCitaScreen(
            citaId = 1,
            onVolverAtras = {},
            onCitaCancelada = {}
        )
    }
}
