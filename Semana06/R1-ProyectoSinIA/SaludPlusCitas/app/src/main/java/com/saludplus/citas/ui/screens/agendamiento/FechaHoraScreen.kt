package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.R
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AzulElectric = Color(0xFF246BFD)
private val AzulIce = Color(0xFFEBF2FE)

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onContinuar: (fecha: String, hora: String) -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }
    val sede = medico?.let {
        Repositorio.obtenerSede(it.sedeId)
    }

    val localeEs = remember { Locale.forLanguageTag("es-ES") }
    val hoy = remember { LocalDate.now() }

    var bloqueOffset by remember { mutableIntStateOf(0) }

    val primerDiaHabil = remember(hoy) {
        when (hoy.dayOfWeek) {
            DayOfWeek.SATURDAY -> hoy.plusDays(2)
            DayOfWeek.SUNDAY -> hoy.plusDays(1)
            else -> hoy
        }
    }

    fun generarDiasHabiles(desde: LocalDate): List<LocalDate> {
        val dias = mutableListOf<LocalDate>()
        var fecha = desde

        while (dias.size < 5) {
            if (
                fecha.dayOfWeek != DayOfWeek.SATURDAY &&
                fecha.dayOfWeek != DayOfWeek.SUNDAY
            ) {
                dias.add(fecha)
            }

            fecha = fecha.plusDays(1)
        }

        return dias
    }

    val diasHabiles = remember(primerDiaHabil, bloqueOffset) {
        var inicioBloque = primerDiaHabil

        repeat(bloqueOffset) {
            val bloqueAnterior = generarDiasHabiles(inicioBloque)
            inicioBloque = bloqueAnterior.last().plusDays(1)

            while (
                inicioBloque.dayOfWeek == DayOfWeek.SATURDAY ||
                inicioBloque.dayOfWeek == DayOfWeek.SUNDAY
            ) {
                inicioBloque = inicioBloque.plusDays(1)
            }
        }

        generarDiasHabiles(inicioBloque)
    }

    val mesAnioFormatter = remember(localeEs) {
        DateTimeFormatter.ofPattern("MMMM yyyy", localeEs)
    }

    val diaNombreFormatter = remember(localeEs) {
        DateTimeFormatter.ofPattern("EEE", localeEs)
    }

    val repoDateFormatter = remember {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    val mesAnioTexto = remember(diasHabiles) {
        diasHabiles.first()
            .format(mesAnioFormatter)
            .replaceFirstChar { it.uppercase() }
    }

    var fechaSeleccionada by remember {
        mutableStateOf<LocalDate?>(null)
    }

    var horaSeleccionada by remember {
        mutableStateOf<String?>(null)
    }

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
                titulo = "Reservar cita",
                onVolverAtras = onVolverAtras
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // INDICADOR DE PASOS / PROGRESO (Paso 2 activo)
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
                    text = "2. Horario ●",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulElectric
                )
                Text(
                    text = "3. Confirmar",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PERFIL RESUMIDO DEL MÉDICO
            if (medico != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(imagenMedico),
                                contentDescription = "Foto de ${medico.nombre}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, AzulElectric, CircleShape)
                            )

                            Column(
                                modifier = Modifier
                                    .padding(start = 14.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = medico.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = especialidad?.nombre ?: "Especialidad médica",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AzulElectric,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "CMP: ${medico.cmp}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (sede != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Place,
                                        contentDescription = null,
                                        tint = AzulElectric,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = sede.nombre,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Call,
                                        contentDescription = null,
                                        tint = AzulElectric,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = medico.telefono,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Elige el día",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (bloqueOffset > 0) {
                            bloqueOffset--
                            fechaSeleccionada = null
                            horaSeleccionada = null
                        }
                    },
                    enabled = bloqueOffset > 0
                ) {
                    Text(
                        text = "‹",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (bloqueOffset > 0) AzulElectric else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                    )
                }

                Text(
                    text = mesAnioTexto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        bloqueOffset++
                        fechaSeleccionada = null
                        horaSeleccionada = null
                    }
                ) {
                    Text(
                        text = "›",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulElectric
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CÁPSULAS DÍAS HABILESS
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(diasHabiles) { dia ->
                    val esSeleccionada = dia == fechaSeleccionada
                    val diaNombre = dia
                        .format(diaNombreFormatter)
                        .replace(".", "")
                        .replaceFirstChar { it.uppercase() }

                    Card(
                        modifier = Modifier
                            .clickable {
                                fechaSeleccionada = dia
                                horaSeleccionada = null
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (esSeleccionada) AzulElectric else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (esSeleccionada) 4.dp else 1.dp
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = diaNombre,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (esSeleccionada) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = dia.dayOfMonth.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (esSeleccionada) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(AzulIce),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = AzulElectric,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Elige una hora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val fechaActualLocalDate = fechaSeleccionada

            if (fechaActualLocalDate == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selecciona una fecha para ver los horarios disponibles",
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
                        MensajeListaVacia(mensaje = "No hay horarios disponibles para esta fecha")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(horariosDisponibles) { hora ->
                            val esHoraSeleccionada = hora == horaSeleccionada

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { horaSeleccionada = hora },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (esHoraSeleccionada) AzulElectric else Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = if (esHoraSeleccionada) 3.dp else 1.dp
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 13.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hora,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (esHoraSeleccionada) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val habilitado = fechaSeleccionada != null && horaSeleccionada != null

            Button(
                onClick = {
                    val f = fechaSeleccionada
                    val h = horaSeleccionada
                    if (f != null && h != null) {
                        val fechaFormatted = f.format(repoDateFormatter)
                        onContinuar(fechaFormatted, h)
                    }
                },
                enabled = habilitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulElectric,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Continuar  →",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
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
