package com.tecsup.lavacampus.model

data class ClienteResumen(
    val id: Long,
    val nombreCompleto: String,
    val codigoEstudiante: String,
    val email: String,
    val totalPedidos: Int = 0,
    val pedidosActivos: Int = 0
)
