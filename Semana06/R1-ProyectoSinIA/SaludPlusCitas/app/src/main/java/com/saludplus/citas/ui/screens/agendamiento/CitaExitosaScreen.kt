package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onVerMisCitas: () -> Unit,
    onIrInicio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = if (cita != null) Repositorio.obtenerMedico(cita.medicoId) else null
    val especialidad = if (medico != null) Repositorio.obtenerEspecialidad(medico.especialidadId) else null

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (cita != null) {
                // Símbolo visual de éxito
                Text(
                    text = "✓",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Título de éxito
                Text(
                    text = "¡Cita agendada!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mensaje explicativo
                Text(
                    text = "Tu cita fue registrada correctamente.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Tarjeta de Resumen
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
                            text = "Detalles de la reserva",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FilaDetalle(
                            etiqueta = "Especialidad:",
                            valor = especialidad?.nombre ?: "No disponible"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalle(
                            etiqueta = "Médico:",
                            valor = medico?.nombre ?: "No disponible"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalle(
                            etiqueta = "CMP:",
                            valor = medico?.cmp ?: "No disponible"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalle(
                            etiqueta = "Fecha:",
                            valor = cita.fecha
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalle(
                            etiqueta = "Hora:",
                            valor = cita.hora
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FilaDetalle(
                            etiqueta = "Estado:",
                            valor = cita.estado
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Principal a Mis Citas
                BotonPrincipal(
                    texto = "Ver mis citas",
                    onClick = onVerMisCitas
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Opción secundaria Ir al Inicio
                TextButton(onClick = onIrInicio) {
                    Text(
                        text = "Ir al inicio",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                // Estado cuando no se encuentra la cita
                Text(
                    text = "No se encontró la cita",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "No fue posible recuperar la información de la reserva solicitada.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextButton(onClick = onIrInicio) {
                    Text(
                        text = "Ir al inicio",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaDetalle(
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
fun CitaExitosaScreenPreview() {
    SaludPlusCitasTheme {
        CitaExitosaScreen(
            citaId = 1,
            onVerMisCitas = {},
            onIrInicio = {}
        )
    }
}
