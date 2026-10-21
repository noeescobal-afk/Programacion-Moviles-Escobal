package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ServicioItem(
    val nombre: String,
    val precio: String,
    val unidad: String,
    val tiempoEntrega: String,
    val activo: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreen() {
    val servicios = listOf(
        ServicioItem("Lavado y secado", "S/ 8.00", "por kg", "24 h", true),
        ServicioItem("Planchado", "S/ 2.50", "por prenda", "48 h", true),
        ServicioItem("Lavado en seco", "S/ 12.00", "por prenda", "48 h", true),
        ServicioItem("Edredón / frazada", "S/ 20.00", "por unidad", "48 h", false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servicios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(servicios) { servicio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(if (servicio.activo) 1f else 0.6f),
                    colors = CardDefaults.cardColors(
                        containerColor = if (servicio.activo) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = servicio.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (!servicio.activo) {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Inactivo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${servicio.precio} ${servicio.unidad}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = servicio.tiempoEntrega,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
