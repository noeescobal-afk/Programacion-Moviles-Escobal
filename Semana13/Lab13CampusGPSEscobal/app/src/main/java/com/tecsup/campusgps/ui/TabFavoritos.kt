package com.tecsup.campusgps.ui

// ARCHIVO: TabFavoritos.kt
// Vista de Favoritos y Recientes con persistencia DataStore/SharedPreferences.

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

@Composable
fun TabFavoritos(
    todosLosLugares: List<LugarConDistancia>,
    favoritos: Set<Int>,
    recientes: List<Int>,
    onToggleFavorito: (Int) -> Unit,
    onSeleccionarLugar: (Int) -> Unit
) {
    val listaFavoritos = todosLosLugares.filter { favoritos.contains(it.lugar.id) }
    val listaRecientes = recientes.mapNotNull { id -> todosLosLugares.firstOrNull { it.lugar.id == id } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Favoritos y Recientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        if (listaFavoritos.isEmpty() && listaRecientes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("⭐", style = MaterialTheme.typography.displayMedium)
                    Text("No tienes favoritos ni consultas recientes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (listaFavoritos.isNotEmpty()) {
                    item {
                        Text("⭐ Favoritos (${listaFavoritos.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    items(listaFavoritos, key = { "fav_${it.lugar.id}" }) { item ->
                        LugarMiniCard(item, esFavorito = true, onToggleFavorito, onSeleccionarLugar)
                    }
                }

                if (listaRecientes.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("🕒 Consultados Recientemente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    items(listaRecientes, key = { "rec_${it.lugar.id}" }) { item ->
                        LugarMiniCard(item, esFavorito = favoritos.contains(item.lugar.id), onToggleFavorito, onSeleccionarLugar)
                    }
                }
            }
        }
    }
}

@Composable
fun LugarMiniCard(
    item: LugarConDistancia,
    esFavorito: Boolean,
    onToggleFavorito: (Int) -> Unit,
    onSeleccionarLugar: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSeleccionarLugar(item.lugar.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(item.lugar.nombre, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                Text(item.lugar.categoria, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = { onToggleFavorito(item.lugar.id) }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorito",
                    tint = if (esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
            Text(
                if (item.lugar.ubicada) item.metros?.let(::formatearDistancia) ?: "—" else "Pendiente",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
