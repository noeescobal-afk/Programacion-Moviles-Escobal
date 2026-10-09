package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.R
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.formatearFechaEspanol
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulElectric = Color(0xFF246BFD)
private val AzulIce = Color(0xFFEBF2FE)

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onCitaConfirmada: (Int) -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario = Repositorio.usuarioActual
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }
    val sede = medico?.let {
        Repositorio.obtenerSede(it.sedeId)
    }

    var mensajeError by remember { mutableStateOf("") }
    var procesando by remember { mutableStateOf(false) }
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    val datosValidos =
        usuario != null &&
                medico != null &&
                especialidad != null &&
                fecha.isNotBlank() &&
                hora.isNotBlank()

    val habilitado = datosValidos && !procesando

    val imagenMedico = when (medicoId) {
        1 -> R.drawable.medico_1
        2 -> R.drawable.medico_2
        3 -> R.drawable.medico_3
        4 -> R.drawable.medico_4
        5 -> R.drawable.medico_5
        6 -> R.drawable.medico_6
        7 -> R.drawable.medico_7
        8 -> R.drawable.medico_8
        9 -> R.drawable.medico_9
        10 -> R.drawable.medico_10
        else -> R.drawable.medico_1
    }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Confirmar cita",
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // INDICADOR DE PASOS / PROGRESO (Paso 3 activo)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzulIce, RoundedCornerShape(12.dp))
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1. Médico ✓",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "2. Horario ✓",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "3. Confirmar ●",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulElectric
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (usuario != null) "Todo listo, ${usuario.nombre.split(" ").first()}" else "Todo listo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Revisa tu credencial de reserva antes de confirmarla.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            // APPOINTMENT PASS / CREDENCIAL DE RESERVA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // SECCIÓN SUPERIOR: MÉDICO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(imagenMedico),
                            contentDescription = "Foto de ${medico?.nombre}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, AzulElectric, CircleShape)
                        )

                        Column(
                            modifier = Modifier.padding(start = 14.dp)
                        ) {
                            Text(
                                text = medico?.nombre ?: "Médico",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = especialidad?.nombre ?: "Especialidad",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AzulElectric,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "CMP: ${medico?.cmp}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = AzulIce, thickness = 1.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // SECCIÓN CENTRAL: FECHA, HORA, SEDE
                    ElementoPassRow(
                        icono = Icons.Filled.Person,
                        titulo = "Paciente",
                        valor = usuario?.nombre ?: "No disponible"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElementoPassRow(
                        icono = Icons.Filled.CalendarMonth,
                        titulo = "Fecha de la cita",
                        valor = formatearFechaEspanol(fecha)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElementoPassRow(
                        icono = Icons.Filled.AccessTime,
                        titulo = "Hora de la cita",
                        valor = hora
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElementoPassRow(
                        icono = Icons.Filled.Place,
                        titulo = "Sede seleccionada",
                        valor = sede?.nombre ?: "SaludPlus"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElementoPassRow(
                        icono = Icons.Filled.LocationOn,
                        titulo = "Dirección",
                        valor = sede?.direccion ?: "Dirección SaludPlus"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElementoPassRow(
                        icono = Icons.Filled.Call,
                        titulo = "Teléfono de contacto",
                        valor = medico?.telefono ?: "(01) 619-0001"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElementoPassRow(
                        icono = Icons.Filled.LocalHospital,
                        titulo = "Modalidad de atención",
                        valor = "Presencial"
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!datosValidos || procesando) return@Button
                    mostrarDialogoConfirmacion = true
                },
                enabled = habilitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulElectric,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (procesando) "Procesando..." else "Confirmar cita  →",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    // Diálogo M3 de confirmación
    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoConfirmacion = false
            },
            title = {
                Text(
                    text = "Confirmar cita",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Deseas confirmar esta cita con ${medico?.nombre ?: "el médico"} para el ${formatearFechaEspanol(fecha)} a las $hora en ${sede?.nombre ?: "SaludPlus"}?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoConfirmacion = false
                        val u = usuario ?: return@TextButton
                        val m = medico ?: return@TextButton

                        procesando = true
                        mensajeError = ""

                        val citaCreada = Repositorio.agendarCita(
                            usuarioId = u.id,
                            medicoId = medicoId,
                            fecha = fecha,
                            hora = hora,
                            sedeId = m.sedeId
                        )

                        if (citaCreada != null) {
                            onCitaConfirmada(citaCreada.id)
                        } else {
                            procesando = false
                            mensajeError = "No se pudo agendar la cita. El horario podría haber dejado de estar disponible."
                        }
                    }
                ) {
                    Text(
                        text = "Confirmar",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulElectric
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoConfirmacion = false
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        )
    }
}

@Composable
private fun ElementoPassRow(
    icono: ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(AzulIce),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = AzulElectric,
                modifier = Modifier.size(19.dp)
            )
        }

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmarCitaScreenPreview() {
    SaludPlusCitasTheme {
        ConfirmarCitaScreen(
            medicoId = 1,
            fecha = "12/10/2026",
            hora = "09:00",
            onCitaConfirmada = {},
            onVolverAtras = {}
        )
    }
}
