package com.saludplus.citas.ui.screens.notificaciones

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.formatearFechaEspanol
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulNotificacion = Color(0xFF246BFD)
private val AzulClaroNotificacion = Color(0xFFE8F0FF)
private val VerdeNotificacion = Color(0xFF168A65)
private val VerdeClaroNotificacion = Color(0xFFDDF6EA)
private val RojoNotificacion = Color(0xFFC9362B)
private val RojoClaroNotificacion = Color(0xFFFFE7E5)
private val FondoNotificacion = Color(0xFFF5F7FB)

@Composable
fun NotificacionesScreen(
    onVolverAtras: () -> Unit,
    onCitaSeleccionada: (Int) -> Unit,
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
                .padding(horizontal = 16.dp)
        ) {
            if (usuario == null) {
                EstadoNotificacionesVacio(
                    titulo = "No hay una sesión activa",
                    descripcion = "Inicia sesión para consultar tus notificaciones.",
                    modifier = Modifier.fillMaxSize()
                )
            } else if (citas.isEmpty()) {
                EstadoNotificacionesVacio(
                    titulo = "Sin notificaciones",
                    descripcion = "Las novedades de tus citas aparecerán aquí.",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = "Novedades de tus citas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Revisa el estado y la información de tus reservas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AzulClaroNotificacion
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
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color.White,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = null,
                                tint = AzulNotificacion,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${citas.size} ${
                                    if (citas.size == 1) {
                                        "notificación"
                                    } else {
                                        "notificaciones"
                                    }
                                }",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = "Actividad reciente de tus citas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                        TarjetaNotificacion(
                            cita = cita,
                            onClick = {
                                onCitaSeleccionada(cita.id)
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaNotificacion(
    cita: Cita,
    onClick: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(cita.medicoId)

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    val esCancelada = cita.estado.equals(
        "Cancelada",
        ignoreCase = true
    )

    val colorEstado = if (esCancelada) {
        RojoNotificacion
    } else {
        VerdeNotificacion
    }

    val fondoEstado = if (esCancelada) {
        RojoClaroNotificacion
    } else {
        VerdeClaroNotificacion
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = FondoNotificacion
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
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = fondoEstado,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (esCancelada) {
                            Icons.Filled.Notifications
                        } else {
                            Icons.Filled.EventAvailable
                        },
                        contentDescription = null,
                        tint = colorEstado,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (esCancelada) {
                            "Cita cancelada"
                        } else {
                            "Cita programada"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorEstado
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = especialidad?.nombre
                            ?: "Especialidad no disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ver detalle",
                    tint = AzulNotificacion
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (esCancelada) {
                    "La cita con ${medico?.nombre ?: "el médico"} fue cancelada."
                } else {
                    "Tienes una cita con ${medico?.nombre ?: "el médico"}."
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.size(7.dp))

                Text(
                    text = formatearFechaEspanol(cita.fecha),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(17.dp)
                )

                Spacer(modifier = Modifier.size(7.dp))

                Text(
                    text = cita.hora,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            Box(
                modifier = Modifier
                    .background(
                        color = fondoEstado,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(
                        horizontal = 11.dp,
                        vertical = 5.dp
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
private fun EstadoNotificacionesVacio(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(82.dp)
                .background(
                    color = AzulClaroNotificacion,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.NotificationsNone,
                contentDescription = null,
                tint = AzulNotificacion,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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