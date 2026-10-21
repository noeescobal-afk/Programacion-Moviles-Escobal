package com.tecsup.lavacampus.model

data class LineaPedido(
    val servicioId: Int,
    val cantidad: Double,
    val precioUnitario: Double,
    val subtotal: Double
)
