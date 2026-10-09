package com.saludplus.citas.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.SaludPlusBottomBar
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun HomeScreen(
    onAgendarCita: () -> Unit,
    onSedes: () -> Unit,
    onDoctores: () -> Unit,
    onEspecialidadSeleccionada: (Int) -> Unit,
    onMisCitas: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    onNotificaciones: () -> Unit,
    modifier: Modifier = Modifier,
    onAbrirMenu: (() -> Unit)? = null
) {
    val usuario = Repositorio.usuarioActual
    val especialidades = Repositorio.especialidadesDestacadas()

    Scaffold(
        bottomBar = {
            SaludPlusBottomBar(
                rutaActual = "inicio",
                onNavegarInicio = {},
                onNavegarSedes = onSedes,
                onNavegarDoctores = onDoctores,
                onNavegarResultados = onResultados,
                onNavegarPerfil = onPerfil
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {

            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onAbrirMenu != null) {
                        IconButton(
                            onClick = onAbrirMenu,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menú lateral"
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (usuario != null) {
                                "¡Hola, ${usuario.nombre}!"
                            } else {
                                "¡Hola!"
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "¿Qué deseas hacer hoy?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onNotificaciones,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Acciones principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sedes
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .clickable { onSedes() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = "Sedes",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Sedes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Agendar cita",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Doctores
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .clickable { onDoctores() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF1FF)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF246BFD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MedicalServices,
                                contentDescription = "Doctores",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Doctores",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Staff médico",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Agenda
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .clickable { onMisCitas() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Agenda",
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Agenda",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Mis reservas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Especialidades destacadas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onSedes) {
                    Text("Ver sedes")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(especialidades) { especialidad ->

                    val iconoEspecialidad: ImageVector
                    val colorEspecialidad: Color
                    val fondoEspecialidad: Color

                    when (especialidad.id) {
                        1 -> {
                            iconoEspecialidad = Icons.Filled.MedicalServices
                            colorEspecialidad = Color(0xFF246BFD)
                            fondoEspecialidad = Color(0xFFDCE8FF)
                        }

                        2 -> {
                            iconoEspecialidad = Icons.Filled.ChildCare
                            colorEspecialidad = Color(0xFFF59E42)
                            fondoEspecialidad = Color(0xFFFFEBD6)
                        }

                        3 -> {
                            iconoEspecialidad = Icons.Filled.Female
                            colorEspecialidad = Color(0xFFE85D8E)
                            fondoEspecialidad = Color(0xFFFFE1EC)
                        }

                        4 -> {
                            iconoEspecialidad = Icons.Filled.Favorite
                            colorEspecialidad = Color(0xFFE5484D)
                            fondoEspecialidad = Color(0xFFFFE5E5)
                        }

                        5 -> {
                            iconoEspecialidad = Icons.Filled.HealthAndSafety
                            colorEspecialidad = Color(0xFF8B5CF6)
                            fondoEspecialidad = Color(0xFFEDE9FE)
                        }

                        6 -> {
                            iconoEspecialidad = Icons.Filled.Visibility
                            colorEspecialidad = Color(0xFF35A7C8)
                            fondoEspecialidad = Color(0xFFDDF5FA)
                        }

                        else -> {
                            iconoEspecialidad = Icons.Filled.MonitorHeart
                            colorEspecialidad = Color(0xFF22A06B)
                            fondoEspecialidad = Color(0xFFDDF6EA)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .width(155.dp)
                            .height(170.dp)
                            .clickable {
                                onSedes()
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = fondoEspecialidad
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.78f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = iconoEspecialidad,
                                    contentDescription = especialidad.nombre,
                                    tint = colorEspecialidad,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = especialidad.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = especialidad.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SaludPlusCitasTheme {
        HomeScreen(
            onAgendarCita = {},
            onSedes = {},
            onDoctores = {},
            onEspecialidadSeleccionada = {},
            onMisCitas = {},
            onResultados = {},
            onPerfil = {},
            onNotificaciones = {}
        )
    }
}
