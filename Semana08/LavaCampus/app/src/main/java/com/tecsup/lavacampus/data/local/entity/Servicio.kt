package com.tecsup.lavacampus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tecsup.lavacampus.model.Unidad

@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val precioUnitario: Double,
    val unidad: Unidad,
    val disponible: Boolean = true,
    val imagenUrl: String? = null
)
