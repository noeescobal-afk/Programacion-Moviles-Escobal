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
import com.tecsup.lavacampus.ui.theme.*

@Composable
fun EstadoPedidoChip(estado: String) {
    val (bgColor, textColor) = when (estado) {
        EstadoPedido.RECIBIDO.name -> StatusRecibido.copy(alpha = 0.2f) to StatusRecibido
        EstadoPedido.EN_PROCESO.name -> StatusEnProceso.copy(alpha = 0.2f) to StatusEnProceso
        EstadoPedido.LISTO.name -> StatusListo.copy(alpha = 0.2f) to StatusListo
        EstadoPedido.ENTREGADO.name -> StatusEntregado.copy(alpha = 0.2f) to StatusEntregado
        EstadoPedido.CANCELADO.name -> StatusCancelado.copy(alpha = 0.2f) to StatusCancelado
        else -> Color.DarkGray to Color.LightGray
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
