package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen
import com.tecsup.avisoscampus.ui.theme.ColorOrigenBoton
import com.tecsup.avisoscampus.ui.theme.ColorOrigenCargador
import com.tecsup.avisoscampus.ui.theme.ColorOrigenModoAvion
import com.tecsup.avisoscampus.ui.theme.ColorOrigenRecordatorio

@Composable
fun HistorialAvisos(
    avisos: List<Aviso>,
    onGenerarPrueba: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Inbox,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Historial de Notificaciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = "${avisos.size} registradas",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        if (avisos.isEmpty()) {
            EstadoVacioHistorial(onGenerarPrueba = onGenerarPrueba)
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                avisos.forEachIndexed { index, aviso ->
                    val esUltimo = index == avisos.lastIndex
                    LineaTiempoItem(
                        aviso = aviso,
                        esUltimo = esUltimo,
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoVacioHistorial(onGenerarPrueba: (() -> Unit)?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = "Sin avisos todavía",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = "Los avisos generados mediante el botón principal, recordatorios o receptores del sistema aparecerán en esta línea de tiempo.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            if (onGenerarPrueba != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onGenerarPrueba,
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generar primer aviso")
                }
            }
        }
    }
}

@Composable
private fun LineaTiempoItem(
    aviso: Aviso,
    esUltimo: Boolean,
) {
    val (colorCategoria, iconoCategoria) = obtenerDatosOrigen(aviso.origen)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Columna de la línea de tiempo (Nodo + Línea vertical)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorCategoria.copy(alpha = 0.15f))
                    .border(2.dp, colorCategoria, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = iconoCategoria,
                    contentDescription = null,
                    tint = colorCategoria,
                    modifier = Modifier.size(18.dp),
                )
            }

            if (!esUltimo) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                )
            }
        }

        // Tarjeta con detalle del aviso
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (esUltimo) 0.dp else 12.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colorCategoria.copy(alpha = 0.15f),
                    ) {
                        Text(
                            text = aviso.origen.texto,
                            color = colorCategoria,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        )
                    }

                    Text(
                        text = aviso.hora,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Text(
                    text = aviso.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = aviso.texto,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun obtenerDatosOrigen(origen: Origen): Pair<Color, ImageVector> {
    return when (origen) {
        Origen.BOTON -> ColorOrigenBoton to Icons.Default.NotificationsActive
        Origen.RECORDATORIO -> ColorOrigenRecordatorio to Icons.Default.Timer
        Origen.MODO_AVION -> ColorOrigenModoAvion to Icons.Default.Flight
        Origen.CARGADOR -> ColorOrigenCargador to Icons.Default.Bolt
    }
}
