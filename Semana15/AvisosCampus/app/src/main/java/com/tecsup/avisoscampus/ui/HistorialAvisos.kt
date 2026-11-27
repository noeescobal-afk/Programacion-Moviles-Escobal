package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {

        if (avisos.isEmpty()) {
            Text("Sin avisos todavía")
        } else {
            avisos.take(20).forEach { aviso ->

                val color = when (aviso.origen) {
                    Origen.BOTON -> Color(0xFF1565A0)
                    Origen.RECORDATORIO -> Color(0xFFB77900)
                    Origen.MODO_AVION -> Color(0xFF6750A4)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = aviso.origen.texto,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .background(color, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Text(
                            aviso.hora,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Text(
                        aviso.titulo,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        aviso.texto,
                        style = MaterialTheme.typography.bodySmall
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}
