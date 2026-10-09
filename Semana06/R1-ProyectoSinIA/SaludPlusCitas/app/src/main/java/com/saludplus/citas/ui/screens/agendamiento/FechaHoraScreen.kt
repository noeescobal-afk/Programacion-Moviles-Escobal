package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (fecha: String, hora: String) -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    val localeEs = remember { Locale.forLanguageTag("es-ES") }
    val hoy = remember { LocalDate.now() }

    // Offset de semanas desde la semana actual
    var semanaOffset by remember { mutableIntStateOf(0) }

    // Fecha inicial base (si hoy es fin de semana, apunta al próximo lunes)
    val baseHoy = remember(hoy) {
        when (hoy.dayOfWeek) {
            DayOfWeek.SATURDAY -> hoy.plusDays(2)
            DayOfWeek.SUNDAY -> hoy.plusDays(1)
            else -> hoy
        }
    }

    val inicioSemana = remember(baseHoy, semanaOffset) {
        baseHoy.with(DayOfWeek.MONDAY).plusWeeks(semanaOffset.toLong())
    }

    // Obtener los 5 días hábiles (Lunes a Viernes) descartando días pasados
// Obtener siempre 5 días hábiles consecutivos, sin fechas pasadas
    val diasHabiles = remember(inicioSemana, hoy, semanaOffset) {
        val dias = mutableListOf<LocalDate>()

        var fecha = if (semanaOffset == 0 && inicioSemana.isBefore(hoy)) {
            hoy
        } else {
            inicioSemana
        }

        while (dias.size < 5) {
            if (
                fecha.dayOfWeek != DayOfWeek.SATURDAY &&
                fecha.dayOfWeek != DayOfWeek.SUNDAY
            ) {
                dias.add(fecha)
            }

            fecha = fecha.plusDays(1)
        }

        dias
    }

    // Formateadores en español
    val mesAnioFormatter = remember(localeEs) { DateTimeFormatter.ofPattern("MMMM yyyy", localeEs) }
    val diaNombreFormatter = remember(localeEs) { DateTimeFormatter.ofPattern("EEE", localeEs) }
    val repoDateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    val mesAnioTexto = remember(diasHabiles, inicioSemana) {
        val referencia = diasHabiles.firstOrNull() ?: inicioSemana
        referencia.format(mesAnioFormatter).replaceFirstChar { it.uppercase() }
    }

    var fechaSeleccionada by remember { mutableStateOf<LocalDate?>(null) }
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

            // Selector de Mes y Año con Flechas de Navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (semanaOffset > 0) {
                            semanaOffset--
                            fechaSeleccionada = null
                            horaSeleccionada = null
                        }
                    },
                    enabled = semanaOffset > 0
                ) {
                    Text(
                        text = "‹",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (semanaOffset > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }

                Text(
                    text = mesAnioTexto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = {
                        semanaOffset++
                        fechaSeleccionada = null
                        horaSeleccionada = null
                    }
                ) {
                    Text(
                        text = "›",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Días con LazyRow
            if (diasHabiles.isEmpty()) {
                Text(
                    text = "No hay días hábiles disponibles para esta semana",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(diasHabiles) { dia ->
                        val esSeleccionada = dia == fechaSeleccionada
                        val diaNombre = dia.format(diaNombreFormatter).replace(".", "").replaceFirstChar { it.uppercase() }
                        val diaNumero = dia.dayOfMonth.toString()

                        Card(
                            modifier = Modifier.clickable {
                                fechaSeleccionada = dia
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
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = diaNombre,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (esSeleccionada) FontWeight.Bold else FontWeight.Normal,
                                    color = if (esSeleccionada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = diaNumero,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (esSeleccionada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título de Horarios Disponibles
            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Grid de Horarios
            val fechaActualLocalDate = fechaSeleccionada
            if (fechaActualLocalDate == null) {
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
                val fechaRepoString = fechaActualLocalDate.format(repoDateFormatter)
                val horariosDisponibles = Repositorio.horariosDisponibles(
                    medicoId = medicoId,
                    fecha = fechaRepoString
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
                        val fechaFormatted = f.format(repoDateFormatter)
                        onContinuar(fechaFormatted, h)
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
