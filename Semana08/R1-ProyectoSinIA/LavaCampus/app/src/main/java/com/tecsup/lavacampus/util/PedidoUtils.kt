package com.tecsup.lavacampus.util

import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.model.EstadoPedido
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Pedido.esAtrasado(): Boolean {
    val activo = estado == EstadoPedido.RECIBIDO.name ||
                 estado == EstadoPedido.EN_PROCESO.name ||
                 estado == EstadoPedido.LISTO.name
    if (!activo) return false
    return try {
        val fechaEntregaDate = LocalDate.parse(fechaEntrega, DateTimeFormatter.ISO_LOCAL_DATE)
        fechaEntregaDate.isBefore(LocalDate.now())
    } catch (e: Exception) {
        false
    }
}
