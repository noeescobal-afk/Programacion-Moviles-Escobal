package com.tecsup.lavacampus.data.repository

import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.ClienteResumen
import com.tecsup.lavacampus.model.IngresoServicio
import kotlinx.coroutines.flow.Flow

interface LavaRepository {
    fun obtenerServicios(): Flow<List<Servicio>>
    fun obtenerServiciosDisponibles(): Flow<List<Servicio>>
    suspend fun obtenerServicioPorId(id: Long): Servicio?
    suspend fun guardarServicio(servicio: Servicio): Long
    suspend fun eliminarServicio(servicio: Servicio)

    fun obtenerClientesResumen(): Flow<List<ClienteResumen>>

    fun obtenerIngresos(): Flow<List<IngresoServicio>>
    suspend fun registrarIngreso(ingreso: IngresoServicio)
}
