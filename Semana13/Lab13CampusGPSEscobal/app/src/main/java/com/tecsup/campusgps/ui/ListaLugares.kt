package com.tecsup.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.TipoLugar
import com.tecsup.campusgps.util.formatearDistancia

/** Tarjetas de lugares con soporte para favoritos, estado de ubicación y selección. */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modoEdicion: Boolean = false,
    lugarAEditar: Lugar? = null,
    favoritos: Set<Int> = emptySet(),
    onToggleFavorito: (Int) -> Unit = {},
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (lugares.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No hay puntos de interés en esta vista",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            val activo = if (modoEdicion) item.lugar.id == lugarAEditar?.id else item.lugar.id == seleccionado
            val fondo = if (activo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            val esFavorito = favoritos.contains(item.lugar.id)

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onClick(item.lugar.id) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = fondo),
                elevation = CardDefaults.cardElevation(defaultElevation = if (activo) 0.dp else 2.dp),
                border = if (activo) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = when (item.lugar.categoria) {
                            "Estudio" -> "📚"
                            "Comida" -> "☕"
                            "Eventos" -> "🎤"
                            else -> "🏛️"
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Column(Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                item.lugar.nombre,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            if (modoEdicion && item.lugar.id == lugarAEditar?.id) {
                                Surface(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("EDITANDO", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            } else if (!item.lugar.ubicada) {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("PENDIENTE", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                                }
                            } else if (item.lugar.tipo == TipoLugar.TECSUP_REAL) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("TECSUP", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(
                            item.lugar.categoria + if (item.lugar.descripcion.isNotEmpty()) " • ${item.lugar.descripcion}" else "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    // Botón favorito y distancia
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { onToggleFavorito(item.lugar.id) }, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Favorito",
                                tint = if (esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                        }
                        Text(
                            if (item.lugar.ubicada) item.metros?.let(::formatearDistancia) ?: "—" else "Pendiente",
                            fontWeight = FontWeight.Bold,
                            color = if (item.lugar.ubicada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
