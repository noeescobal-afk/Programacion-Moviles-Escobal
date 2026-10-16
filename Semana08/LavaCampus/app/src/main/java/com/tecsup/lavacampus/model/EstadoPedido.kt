package com.tecsup.lavacampus.model

import androidx.compose.ui.graphics.Color

enum class EstadoPedido(val etiqueta: String, val color: Color) {
    PENDIENTE("Pendiente", Color(0xFFF59E0B)),
    RECIBIDO("Recibido", Color(0xFF3B82F6)),
    EN_PROCESO("En Proceso", Color(0xFF8B5CF6)),
    LISTO("Listo", Color(0xFF10B981)),
    ENTREGADO("Entregado", Color(0xFF6B7280)),
    CANCELADO("Cancelado", Color(0xFFEF4444));

    fun siguiente(): EstadoPedido {
        return when (this) {
            PENDIENTE -> RECIBIDO
            RECIBIDO -> EN_PROCESO
            EN_PROCESO -> LISTO
            LISTO -> ENTREGADO
            ENTREGADO -> ENTREGADO
            CANCELADO -> CANCELADO
        }
    }
}
