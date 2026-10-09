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
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme
import com.saludplus.citas.ui.components.formatearFechaEspanol


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
    val especialidad = if (medico != null) Repositorio.obtenerEspecialidad(medico.especialidadId) else null

    var mensajeError by remember { mutableStateOf("") }
    var procesando by remember { mutableStateOf(false) }

    val datosValidos = usuario != null && medico != null && especialidad != null && fecha.isNotBlank() && hora.isNotBlank()
    val habilitado = datosValidos && !procesando

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
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Revisa los detalles antes de confirmar tu reserva",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de Resumen de la Cita
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
                        text = "Resumen de tu cita",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ElementoResumen(
                        etiqueta = "Paciente:",
                        valor = usuario?.nombre ?: "No disponible (sin sesión)"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ElementoResumen(
                        etiqueta = "Especialidad:",
                        valor = especialidad?.nombre ?: "No encontrada"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ElementoResumen(
                        etiqueta = "Médico:",
                        valor = medico?.nombre ?: "No encontrado"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ElementoResumen(
                        etiqueta = "CMP:",
                        valor = medico?.cmp ?: "N/A"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ElementoResumen(
                        etiqueta = "Fecha:",
                        valor = formatearFechaEspanol(fecha)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ElementoResumen(
                        etiqueta = "Hora:",
                        valor = hora
                    )
                }
            }

            // Mensajes de error por datos faltantes o fallo en agendamiento
            if (!datosValidos && mensajeError.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                val detalleError = when {
                    usuario == null -> "Debes iniciar sesión para agendar una cita."
                    medico == null -> "El médico seleccionado no existe."
                    especialidad == null -> "La especialidad asociada no fue encontrada."
                    else -> "La fecha u hora seleccionadas no son válidas."
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

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Confirmar Cita
            BotonPrincipal(
                texto = if (procesando) "Procesando..." else "Confirmar cita",
                enabled = habilitado,
                onClick = {
                    val u = usuario ?: return@BotonPrincipal
                    if (!datosValidos || procesando) return@BotonPrincipal

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
                        mensajeError = "No se pudo agendar la cita. El horario podría haber dejado de estar disponible."
                    }
                }
            )
        }
    }
}

@Composable
private fun ElementoResumen(
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
