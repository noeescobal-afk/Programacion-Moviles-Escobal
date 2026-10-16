package com.tecsup.lavacampus.model

data class LineaPedido(
    val id: Long = 0,
    val pedidoId: Long = 0,
    val servicioId: Long,
    val servicioNombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val unidad: Unidad = Unidad.UNIDAD
) {
    val subtotal: Double
        get() = cantidad * precioUnitario
}
