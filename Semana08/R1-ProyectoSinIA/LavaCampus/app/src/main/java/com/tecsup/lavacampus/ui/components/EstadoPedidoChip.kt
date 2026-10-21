package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.lavacampus.model.EstadoPedido

@Composable
fun EstadoPedidoChip(estado: String) {
    val (bgColor, textColor) = when (estado) {
        EstadoPedido.RECIBIDO.name -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        EstadoPedido.EN_PROCESO.name -> Color(0xFFDBEAFE) to Color(0xFF2563EB)
        EstadoPedido.LISTO.name -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        EstadoPedido.ENTREGADO.name -> Color(0xFFF1F5F9) to Color(0xFF475569)
        EstadoPedido.CANCELADO.name -> Color(0xFFFEE2E2) to Color(0xFFDC2626)
        else -> Color.LightGray to Color.DarkGray
    }

    val label = when (estado) {
        EstadoPedido.RECIBIDO.name -> "Recibido"
        EstadoPedido.EN_PROCESO.name -> "En Proceso"
        EstadoPedido.LISTO.name -> "Listo"
        EstadoPedido.ENTREGADO.name -> "Entregado"
        EstadoPedido.CANCELADO.name -> "Cancelado"
        else -> estado
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
