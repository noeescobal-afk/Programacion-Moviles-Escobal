package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val cmp: String,
    val sedeId: Int,
    val telefono: String
)
