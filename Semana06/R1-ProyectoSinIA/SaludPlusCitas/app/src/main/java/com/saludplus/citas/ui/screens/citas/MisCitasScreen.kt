package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.R
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.formatearFechaEspanol
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulCitas = Color(0xFF246BFD)
private val AzulClaroCitas = Color(0xFFE8F0FF)
private val VerdeCitas = Color(0xFF168A65)
private val VerdeClaroCitas = Color(0xFFDDF6EA)
private val RojoCitas = Color(0xFFC9362B)
private val RojoClaroCitas = Color(0xFFFFE7E5)
private val FondoCita = Color(0xFFF5F7FB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    onCitaSeleccionada: (Int) -> Unit,
    onAgendarCita: () -> Unit,
    onInicio: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usuario = Repositorio.usuarioActual

    val citas = if (usuario != null) {
        Repositorio.citasDelUsuario(usuario.id)
    } else {
        emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis citas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onInicio,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Mis citas"
                        )
                    },
                    label = { Text("Mis citas") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onResultados,
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Assignment,
                            contentDescription = "Resultados"
                        )
                    },
                    label = { Text("Resultados") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onPerfil,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Perfil"
                        )
                    },
                    label = { Text("Perfil") }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Gestiona tus reservas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Consulta tus próximas citas y revisa su información.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (citas.isEmpty()) {
                EstadoCitasVacio(
                    onAgendarCita = onAgendarCita,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = citas,
                        key = { it.id }
                    ) { cita ->
                        TarjetaCita(
                            cita = cita,
                            onClick = {
                                onCitaSeleccionada(cita.id)
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                Button(
                    onClick = onAgendarCita,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulCitas,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "Agendar nueva cita",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TarjetaCita(
    cita: Cita,
    onClick: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(cita.medicoId)

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    val cancelada = cita.estado.equals(
        "Cancelada",
        ignoreCase = true
    )

    val colorEstado = if (cancelada) {
        RojoCitas
    } else {
        VerdeCitas
    }

    val fondoEstado = if (cancelada) {
        RojoClaroCitas
    } else {
        VerdeClaroCitas
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = FondoCita
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imagenMedico = obtenerImagenMedicoCita(cita.medicoId)

                if (imagenMedico != null) {
                    Image(
                        painter = painterResource(id = imagenMedico),
                        contentDescription = medico?.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = AzulClaroCitas,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EventAvailable,
                            contentDescription = null,
                            tint = AzulCitas,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = medico?.nombre ?: "Médico no disponible",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = especialidad?.nombre ?: "Especialidad no disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = AzulCitas
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ver detalle",
                    tint = AzulCitas
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = formatearFechaEspanol(cita.fecha),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = cita.hora,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .background(
                        color = fondoEstado,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
            ) {
                Text(
                    text = cita.estado,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorEstado
                )
            }
        }
    }
}

@Composable
private fun EstadoCitasVacio(
    onAgendarCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(82.dp)
                .background(
                    color = AzulClaroCitas,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = AzulCitas,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Aún no tienes citas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Agenda una consulta médica y aparecerá aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = onAgendarCita,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulCitas,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "Agendar cita",
                fontWeight = FontWeight.Bold
            )
        }
    }
}


private fun obtenerImagenMedicoCita(medicoId: Int): Int? {
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
fun MisCitasScreenPreview() {
    SaludPlusCitasTheme {
        MisCitasScreen(
            onCitaSeleccionada = {},
            onAgendarCita = {},
            onInicio = {},
            onResultados = {},
            onPerfil = {}
        )
    }
}
