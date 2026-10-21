package com.tecsup.lavacampus.data.repository

import com.tecsup.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

interface LavaRepository {
    // Servicios
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(servicio: Servicio)
    suspend fun eliminarServicio(servicio: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneDetalles(servicioId: Int): Boolean

    /* TODO: Operaciones de Usuarios (Pasos futuros)
    suspend fun login(email: String, clave: String): Any?
    fun observarClientes(): Flow<List<com.tecsup.lavacampus.model.ClienteResumen>>
    */

    /* TODO: Operaciones de Pedidos (Pasos futuros)
    suspend fun crearPedido(pedido: Any, detalles: List<Any>): Long
    suspend fun contarActivos(): Int
    fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<Any>>
    fun observarPedidos(): Flow<List<Any>>
    suspend fun observarPedido(pedidoId: Int): Any?
    suspend fun cambiarEstado(pedidoId: Int, estado: com.tecsup.lavacampus.model.EstadoPedido)
    suspend fun marcarPagado(pedidoId: Int, pagado: Boolean)
    fun observarCasillerosOcupados(): Flow<List<String>>
    */

    /* TODO: Operaciones de Panel (Pasos futuros)
    fun observarPedidosDeFecha(fecha: String): Flow<List<Any>>
    fun observarCantidadPorEstado(): Flow<Map<com.tecsup.lavacampus.model.EstadoPedido, Int>>
    fun observarIngresosDesde(fecha: String): Flow<Double>
    fun observarIngresosPorServicio(): Flow<List<com.tecsup.lavacampus.model.IngresoServicio>>
    */
}
