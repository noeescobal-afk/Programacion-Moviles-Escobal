package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.formatearFechaEspanol
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

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

    var mensajeError by remember { mutableStateOf("") }
    var procesando by remember { mutableStateOf(false) }

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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Text(
                text = "Revisa los detalles de tu reserva",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Confirma que toda la información sea correcta antes de agendar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (medico != null) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF1FF)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(imagenMedico),
                            contentDescription = "Foto de ${medico.nombre}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                        )

                        Column(
                            modifier = Modifier.padding(start = 14.dp)
                        ) {

                            Text(
                                text = medico.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = especialidad?.nombre ?: "Especialidad médica",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = "CMP: ${medico.cmp}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Resumen de tu cita",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F7FB)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    ElementoResumenVisual(
                        icono = Icons.Filled.Person,
                        titulo = "Paciente",
                        valor = usuario?.nombre ?: "No disponible"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ElementoResumenVisual(
                        icono = Icons.Filled.CalendarMonth,
                        titulo = "Fecha",
                        valor = formatearFechaEspanol(fecha)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ElementoResumenVisual(
                        icono = Icons.Filled.AccessTime,
                        titulo = "Hora",
                        valor = hora
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ElementoResumenVisual(
                        icono = Icons.Filled.LocalHospital,
                        titulo = "Modalidad",
                        valor = "Presencial"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ElementoResumenVisual(
                        icono = Icons.Filled.LocationOn,
                        titulo = "Centro médico",
                        valor = "Clínica SaludPlus"
                    )
                }
            }

            if (!datosValidos && mensajeError.isEmpty()) {

                Spacer(modifier = Modifier.height(16.dp))

                val detalleError = when {
                    usuario == null ->
                        "Debes iniciar sesión para agendar una cita."

                    medico == null ->
                        "El médico seleccionado no existe."

                    especialidad == null ->
                        "La especialidad asociada no fue encontrada."

                    else ->
                        "La fecha u hora seleccionadas no son válidas."
                }

                Text(
                    text = detalleError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
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

            BotonPrincipal(
                texto = if (procesando) {
                    "Procesando..."
                } else {
                    "Confirmar cita"
                },
                enabled = habilitado,
                onClick = {
                    val u = usuario ?: return@BotonPrincipal

                    if (!datosValidos || procesando) {
                        return@BotonPrincipal
                    }

                    procesando = true
                    mensajeError = ""

                    val citaCreada = Repositorio.agendarCita(
                        usuarioId = u.id,
                        medicoId = medicoId,
                        fecha = fecha,
                        hora = hora
                    )

                    if (citaCreada != null) {
                        onCitaConfirmada(citaCreada.id)
                    } else {
                        procesando = false
                        mensajeError =
                            "No se pudo agendar la cita. El horario podría haber dejado de estar disponible."
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ElementoResumenVisual(
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
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFE5EEFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(21.dp)
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