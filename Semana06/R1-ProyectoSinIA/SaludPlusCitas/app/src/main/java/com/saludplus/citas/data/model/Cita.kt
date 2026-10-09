package com.saludplus.citas.data.model

data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val sedeId: Int = 1,
    val fecha: String,
    val hora: String,
    val estado: String
)
