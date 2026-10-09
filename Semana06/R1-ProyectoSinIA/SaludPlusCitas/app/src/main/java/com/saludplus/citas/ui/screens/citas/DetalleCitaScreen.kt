package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

private val AzulDetalle = Color(0xFF246BFD)
private val AzulClaroDetalle = Color(0xFFE8F0FF)
private val VerdeDetalle = Color(0xFF168A65)
private val VerdeClaroDetalle = Color(0xFFDDF6EA)
private val RojoDetalle = Color(0xFFC9362B)
private val RojoClaroDetalle = Color(0xFFFFE7E5)
private val FondoDetalle = Color(0xFFF5F7FB)

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onVolverAtras: () -> Unit,
    onCitaCancelada: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    var mostrarDialogoCancelacion by remember {
        mutableStateOf(false)
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

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
            verticalArrangement = Arrangement.Top
        ) {
            if (cita != null) {

                Text(
                    text = "Información de tu reserva",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Consulta los datos de tu atención médica.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Médico
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulClaroDetalle
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val imagenMedico = obtenerImagenMedicoDetalle(
                            medico?.id ?: 0
                        )

                        if (imagenMedico != null) {
                            Image(
                                painter = painterResource(imagenMedico),
                                contentDescription = medico?.nombre,
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .background(
                                        Color.White,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MedicalServices,
                                    contentDescription = null,
                                    tint = AzulDetalle,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = medico?.nombre
                                    ?: "Médico no disponible",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = especialidad?.nombre
                                    ?: "Especialidad no disponible",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AzulDetalle,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = medico?.cmp ?: "CMP no disponible",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Detalles de la cita",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = FondoDetalle
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ElementoDetalleVisual(
                            icono = Icons.Filled.CalendarMonth,
                            titulo = "Fecha",
                            valor = formatearFechaEspanol(cita.fecha)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ElementoDetalleVisual(
                            icono = Icons.Filled.AccessTime,
                            titulo = "Hora",
                            valor = cita.hora
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ElementoDetalleVisual(
                            icono = Icons.Filled.MedicalServices,
                            titulo = "Modalidad",
                            valor = "Presencial"
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ElementoDetalleVisual(
                            icono = Icons.Filled.LocationOn,
                            titulo = "Centro médico",
                            valor = "Clínica SaludPlus"
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ElementoEstado(
                            estado = cita.estado
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

                if (!cita.estado.equals("Cancelada", ignoreCase = true)) {
                    Button(
                        onClick = {
                            mostrarDialogoCancelacion = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RojoDetalle,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(19.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "Cancelar cita",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = RojoClaroDetalle
                        )
                    ) {
                        Text(
                            text = "Esta cita ha sido cancelada.",
                            color = RojoDetalle,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        )
                    }
                }
            } else {
                EstadoCitaNoEncontrada()
            }
        }
    }

    if (mostrarDialogoCancelacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoCancelacion = false
            },
            title = {
                Text(
                    text = "Cancelar cita",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cancelar esta cita? El horario volverá a estar disponible."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCancelacion = false

                        val cancelada =
                            Repositorio.cancelarCita(citaId)

                        if (cancelada) {
                            mensajeError = ""
                            onCitaCancelada()
                        } else {
                            mensajeError =
                                "No se pudo cancelar la cita."
                        }
                    }
                ) {
                    Text(
                        text = "Sí, cancelar",
                        color = RojoDetalle,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCancelacion = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun ElementoDetalleVisual(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = AzulClaroDetalle,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = AzulDetalle,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
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

@Composable
private fun ElementoEstado(
    estado: String
) {
    val cancelada = estado.equals(
        "Cancelada",
        ignoreCase = true
    )

    val color = if (cancelada) {
        RojoDetalle
    } else {
        VerdeDetalle
    }

    val fondo = if (cancelada) {
        RojoClaroDetalle
    } else {
        VerdeClaroDetalle
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = fondo,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (cancelada) {
                    Icons.Filled.Close
                } else {
                    Icons.Filled.CheckCircle
                },
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(
                text = "Estado",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = estado,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun EstadoCitaNoEncontrada() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = RojoClaroDetalle,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = RojoDetalle,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No se encontró la cita",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "La reserva solicitada no existe o no se pudo recuperar la información.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private fun obtenerImagenMedicoDetalle(
    medicoId: Int
): Int? {
    return when (medicoId) {
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
        else -> null
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