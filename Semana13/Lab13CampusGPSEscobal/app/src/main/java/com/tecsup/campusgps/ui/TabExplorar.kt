package com.tecsup.campusgps.ui

// CampusGPS - Explorar: interfaz adaptada a modo claro y oscuro.
// Conserva la búsqueda, categorías, fotografías, selección y favoritos existentes.

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.ui.theme.LocalCampusDarkTheme
import com.tecsup.campusgps.util.formatearDistancia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabExplorar(
    lugares: List<LugarConDistancia>,
    categoria: String,
    query: String,
    favoritos: Set<Int>,
    onCambiarCategoria: (String) -> Unit,
    onActualizarQuery: (String) -> Unit,
    onToggleFavorito: (Int) -> Unit,
    onSeleccionarLugar: (Int) -> Unit,
) {
    // El tema se obtiene del selector de apariencia de CampusGPS,
    // no de los ajustes generales del teléfono.
    val oscuro = LocalCampusDarkTheme.current
    val fondo = if (oscuro) {
        listOf(Color(0xFF101C30), Color(0xFF172840), Color(0xFF101C30))
    } else {
        listOf(Color(0xFFE8F2FF), Color(0xFFF4F8FF), Color.White)
    }
    val fondoTarjeta = if (oscuro) Color(0xFF223650) else Color.White
    val fondoCampo = if (oscuro) Color(0xFF223650) else Color(0xFFF8FBFF)
    val bordeCampo = if (oscuro) Color(0xFF526A88) else Color(0xFFD9E4F2)
    val textoSecundario = if (oscuro) Color(0xFFC2D2E5) else Color(0xFF5B6472)
    val acento = if (oscuro) Color(0xFF91C5FF) else Color(0xFF0E5BD7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = fondo))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Encabezado institucional, legible en ambos temas.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF0A3D91), Color(0xFF1259AC))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Explorar TECSUP",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Descubre instalaciones, servicios y más",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFDCE8FF)
            )
        }

        // Buscador funcional: sigue enviando el texto al ViewModel.
        OutlinedTextField(
            value = query,
            onValueChange = onActualizarQuery,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("¿Qué necesitas hoy?") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = acento)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onActualizarQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = fondoCampo,
                unfocusedContainerColor = fondoCampo,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = textoSecundario,
                unfocusedPlaceholderColor = textoSecundario,
                focusedBorderColor = acento,
                unfocusedBorderColor = bordeCampo,
                cursorColor = acento
            )
        )

        // Una sola fila horizontal de categorías, sin duplicarlas.
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Todos", "Estudio", "Comida", "Deporte", "Servicios", "Eventos")) { cat ->
                FilterChip(
                    selected = categoria == cat,
                    onClick = { onCambiarCategoria(cat) },
                    label = { Text(cat) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = fondoCampo,
                        labelColor = textoSecundario,
                        selectedContainerColor = if (oscuro) Color(0xFF1766C5) else Color(0xFF0E5BD7),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(lugares, key = { it.lugar.id }) { item ->
                val imagenes = obtenerImagenesLugar(item.lugar.id)
                val esFavorito = item.lugar.id in favoritos

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSeleccionarLugar(item.lugar.id) },
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Se reutilizan las fotografías locales del proyecto.
                        if (imagenes.isNotEmpty()) {
                            Image(
                                painter = painterResource(id = imagenes.first()),
                                contentDescription = item.lugar.nombre,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = item.lugar.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.lugar.categoria,
                                style = MaterialTheme.typography.labelMedium,
                                color = acento
                            )
                            Text(
                                text = if (item.lugar.ubicada) {
                                    item.metros?.let(::formatearDistancia) ?: "—"
                                } else {
                                    "Pendiente de ubicación"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = textoSecundario
                            )
                        }

                        IconButton(onClick = { onToggleFavorito(item.lugar.id) }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = if (esFavorito) "Quitar de favoritos" else "Añadir a favoritos",
                                tint = if (esFavorito) acento else textoSecundario.copy(alpha = 0.62f)
                            )
                        }
                    }
                }
            }
        }
    }
}
