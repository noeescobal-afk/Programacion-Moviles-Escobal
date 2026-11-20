package com.tecsup.campusgps.ui

// ARCHIVO: FichaInformativaBottomSheet.kt
// ModalBottomSheet Material 3 premium con ficha informativa, fotografías, carrusel, botón Cómo Llegar, Pasos de Navegación y Favoritos claros.

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.R
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

fun obtenerImagenesLugar(lugarId: Int): List<Int> {
    return when (lugarId) {
        201 -> listOf(R.drawable.tecsup_biblioteca)
        202 -> listOf(R.drawable.tecsup_servicios_educativos)
        203 -> listOf(R.drawable.tecsup_auditorio)
        204 -> listOf(R.drawable.tecsup_loza_deportiva)
        205 -> listOf(R.drawable.tecsup_cafeteria)
        206 -> listOf(R.drawable.tecsup_entrada)
        207 -> listOf(R.drawable.tecsup_estacionamiento_1, R.drawable.tecsup_estacionamiento_2) // Carrusel de estacionamiento
        208 -> listOf(R.drawable.tecsup_laboratorio)
        209 -> listOf(R.drawable.tecsup_laboratorio_2)
        210 -> listOf(R.drawable.tecsup_laboratorio_3)
        211 -> listOf(R.drawable.tecsup_patio_comida)
        212, 213, 214 -> listOf(R.drawable.tecsup_servicios_educativos)
        else -> listOf(R.drawable.tecsup_entrada)
    }
}

fun obtenerEquipamientoServicios(categoria: String): String {
    return when (categoria) {
        "Estudio" -> "• Equipamiento académico especializado\n• Pizarras y proyectores multimedia\n• Espacios de lectura y trabajo colaborativo"
        "Comida" -> "• Variedad de alimentos y bebidas\n• Mesas de descanso y socialización\n• Concesionario higiénico y seguro"
        "Eventos" -> "• Sistema de audio y proyección audiovisual\n• Butacas ergonómicas y climatización\n• Ideal para conferencias y ponencias"
        else -> "• Zona de acceso y control vehicular\n• Vigilancia permanente del campus\n• Señalización e iluminación nocturna"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaInformativaBottomSheet(
    lugarConDistancia: LugarConDistancia,
    esFavorito: Boolean,
    onToggleFavorito: (Int) -> Unit,
    onComoLlegar: (Lugar) -> Unit,
    onDismiss: () -> Unit,
) {
    val lugar = lugarConDistancia.lugar
    val imagenes = remember(lugar.id) { obtenerImagenesLugar(lugar.id) }
    var indiceImagen by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título y Categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(lugar.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Categoría: ${lugar.categoria}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (lugar.ubicada) (lugarConDistancia.metros?.let(::formatearDistancia) ?: "Calculando...") else "Pendiente",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Botón claro de Favoritos (Parte 3)
            Button(
                onClick = { onToggleFavorito(lugar.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (esFavorito) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (esFavorito) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                )
            ) {
                Text(
                    if (esFavorito) "Quitar de Favoritos ⭐" else "Añadir a Favoritos ⭐",
                    fontWeight = FontWeight.Bold
                )
            }

            // Botón "Cómo llegar" (Parte 1)
            if (lugar.ubicada) {
                Button(
                    onClick = {
                        onComoLlegar(lugar)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("🧭 Cómo llegar", fontWeight = FontWeight.Bold)
                }
            }

            // Imagen / Carrusel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = imagenes[indiceImagen]),
                    contentDescription = lugar.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (imagenes.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        imagenes.indices.forEach { idx ->
                            Surface(
                                modifier = Modifier.size(8.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = if (idx == indiceImagen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                            ) {}
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                indiceImagen = (indiceImagen + 1) % imagenes.size
                            }
                    ) {}
                }
            }
            if (imagenes.size > 1) {
                Text(
                    "Toca la imagen para ver más fotografías (${indiceImagen + 1}/${imagenes.size})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Descripción
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Descripción", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    lugar.descripcion.ifEmpty { "Instalación oficial dentro del campus TECSUP Lima." },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Equipamiento y servicios
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Equipamiento y Servicios", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    obtenerEquipamientoServicios(lugar.categoria),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Pasos para llegar (Parte 1)
            if (lugar.ubicada) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Pasos para Llegar", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(
                        "1. Dirígete hacia la vía principal del campus desde tu posición GPS actual.\n" +
                        "2. Sigue la línea guía de navegación indicada en el mapa hacia ${lugar.nombre}.\n" +
                        "3. Continúa en línea recta hasta llegar al destino indicado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Estado de ubicación
            if (!lugar.ubicada) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "⚠️ Ubicación pendiente de configurar mediante el editor visual.",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
