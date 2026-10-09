package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun NotificacionesScreen(
    onVolverAtras: () -> Unit,
    onCitaSeleccionada: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario = Repositorio.usuarioActual
    val citas = if (usuario != null) Repositorio.citasDelUsuario(usuario.id) else emptyList()

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Notificaciones",
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
            if (usuario == null) {
                // Estado sin sesión activa
                MensajeListaVacia(
                    mensaje = "No hay una sesión activa"
                )
            } else if (citas.isEmpty()) {
                // Estado sin citas
                MensajeListaVacia(
                    mensaje = "No tienes notificaciones"
                )
            } else {
                // Listado de notificaciones derivadas de las citas
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(citas) { cita ->
                        val medico = Repositorio.obtenerMedico(cita.medicoId)
                        val especialidad = if (medico != null) Repositorio.obtenerEspecialidad(medico.especialidadId) else null

                        val esCancelada = cita.estado == "Cancelada"

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCitaSeleccionada(cita.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (esCancelada) {
                                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (esCancelada) {
                                            "Cita cancelada de ${especialidad?.nombre ?: "No disponible"}"
                                        } else {
                                            "Cita de ${especialidad?.nombre ?: "No disponible"}"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (esCancelada) {
                                            MaterialTheme.colorScheme.error
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Tienes una cita con ${medico?.nombre ?: "No disponible"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${cita.fecha} - ${cita.hora}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Estado: ${cita.estado}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (esCancelada) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificacionesScreenPreview() {
    SaludPlusCitasTheme {
        NotificacionesScreen(
            onVolverAtras = {},
            onCitaSeleccionada = {}
        )
    }
}
