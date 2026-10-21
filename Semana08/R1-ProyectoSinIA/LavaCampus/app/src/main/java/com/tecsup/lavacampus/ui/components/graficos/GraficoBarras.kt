package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.IngresoServicio
import java.util.Locale

@Composable
fun GraficoBarras(
    ingresosServicios: List<IngresoServicio>,
    modifier: Modifier = Modifier
) {
    val maxIngreso = ingresosServicios.maxOfOrNull { it.ingresoAcumulado } ?: 0.0
    val primaryColor = MaterialTheme.colorScheme.primary

    if (ingresosServicios.isEmpty() || maxIngreso == 0.0) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay datos de ingresos por servicio",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / (ingresosServicios.size * 2f)
            val spacing = barWidth

            ingresosServicios.forEachIndexed { index, item ->
                val barHeight = if (maxIngreso > 0) {
                    (item.ingresoAcumulado.toFloat() / maxIngreso.toFloat()) * (canvasHeight - 20f)
                } else 0f

                val left = spacing + index * (barWidth + spacing)
                val top = canvasHeight - barHeight

                drawRoundRect(
                    color = primaryColor,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ingresosServicios.forEach { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = item.nombreServicio.take(10),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "S/ ${String.format(Locale.getDefault(), "%.1f", item.ingresoAcumulado)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
