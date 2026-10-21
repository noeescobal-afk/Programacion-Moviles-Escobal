package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(
    recibido: Int,
    enProceso: Int,
    listo: Int,
    entregado: Int,
    cancelado: Int,
    modifier: Modifier = Modifier
) {
    val total = recibido + enProceso + listo + entregado + cancelado

    val colorRecibido = Color(0xFFD97706)
    val colorEnProceso = Color(0xFF2563EB)
    val colorListo = Color(0xFF16A34A)
    val colorEntregado = Color(0xFF475569)
    val colorCancelado = Color(0xFFDC2626)

    val datos = listOf(
        Triple("Recibido", recibido, colorRecibido),
        Triple("En Proceso", enProceso, colorEnProceso),
        Triple("Listo", listo, colorListo),
        Triple("Entregado", entregado, colorEntregado),
        Triple("Cancelado", cancelado, colorCancelado)
    ).filter { it.second > 0 }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (total == 0) {
                    drawArc(
                        color = Color.LightGray,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 36f)
                    )
                } else {
                    var startAngle = -90f
                    datos.forEach { (_, valor, color) ->
                        val sweepAngle = (valor.toFloat() / total.toFloat()) * 360f
                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = 36f)
                        )
                        startAngle += sweepAngle
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = total.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            datos.forEach { (nombre, valor, color) ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, shape = CircleShape)
                    )
                    Text(
                        text = "$nombre ($valor)",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
