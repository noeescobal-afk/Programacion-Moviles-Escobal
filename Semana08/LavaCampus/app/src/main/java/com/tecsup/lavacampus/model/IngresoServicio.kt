package com.tecsup.lavacampus.model

import java.util.Date

data class IngresoServicio(
    val id: Long = 0,
    val servicioId: Long,
    val servicioNombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val montoTotal: Double,
    val fecha: Date = Date(),
    val registradoPor: String = ""
)
