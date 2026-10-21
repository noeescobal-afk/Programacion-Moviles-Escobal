package com.tecsup.lavacampus.data.repository

import androidx.room.withTransaction
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.ClienteResumen
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.IngresoServicio
import com.tecsup.lavacampus.model.LineaPedido
import com.tecsup.lavacampus.model.Unidad
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LavaRepositoryRoom(
    private val database: LavaDatabase
) : LavaRepository {

    private val usuarioDao = database.usuarioDao()
    private val servicioDao = database.servicioDao()
    private val pedidoDao = database.pedidoDao()
    private val detalleDao = database.detalleDao()

    // USUARIOS
    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    override fun observarClientes(): Flow<List<ClienteResumen>> {
        return usuarioDao.observarClientes()
    }

    // SERVICIOS
    override fun observarServicios(): Flow<List<Servicio>> {
        return servicioDao.observarTodos()
    }

    override fun observarServiciosActivos(): Flow<List<Servicio>> {
        return servicioDao.observarActivos()
    }

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) {
            servicioDao.insertar(s)
        } else {
            servicioDao.actualizar(s)
        }
    }

    override suspend fun eliminarServicio(s: Servicio) {
        servicioDao.eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        servicioDao.cambiarActivo(id, activo)
    }

    override suspend fun servicioTieneDetalles(id: Int): Boolean {
        return detalleDao.contarPorServicio(id) > 0
    }

    // PEDIDOS
    override suspend fun crearPedido(usuarioId: Int, lineas: List<LineaPedido>, nota: String): Long {
        if (lineas.isEmpty()) {
            throw IllegalArgumentException("El pedido debe tener al menos una línea")
        }
        if (lineas.none { it.cantidad > 0 }) {
            throw IllegalArgumentException("Debe existir al menos una línea con cantidad mayor a 0")
        }

        val activosCount = pedidoDao.contarActivos(usuarioId)
        if (activosCount >= 3) {
            throw IllegalStateException("El usuario ya tiene 3 pedidos activos")
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fechaRegistroStr = LocalDate.now().format(formatter)

        var maxHoras = 0
        var subtotalTotal = 0.0
        var subtotalLavado = 0.0
        val detallesData = mutableListOf<Triple<Int, Double, Double>>().apply {
            for (linea in lineas) {
                val servicio = servicioDao.obtener(linea.servicioId)
                    ?: throw IllegalArgumentException("Servicio no encontrado: ${linea.servicioId}")
                if (!servicio.activo) {
                    throw IllegalArgumentException("El servicio '${servicio.nombre}' no está activo")
                }
                validarCantidad(servicio.unidad, linea.cantidad)

                if (servicio.horasEntrega > maxHoras) {
                    maxHoras = servicio.horasEntrega
                }

                val precioActual = servicio.precio
                val sub = linea.cantidad * precioActual
                subtotalTotal += sub
                if (servicio.unidad == Unidad.KG.name) {
                    subtotalLavado += sub
                }

                add(Triple(servicio.id, linea.cantidad, precioActual))
            }
        }

        val diasEntrega = if (maxHoras > 0) maxHoras / 24 else 1
        var fechaEntregaDate = LocalDate.now().plusDays(diasEntrega.toLong())
        if (fechaEntregaDate.dayOfWeek == DayOfWeek.SUNDAY) {
            fechaEntregaDate = fechaEntregaDate.plusDays(1)
        }
        val fechaEntregaStr = fechaEntregaDate.format(formatter)

        val kgLavadoTotal = lineas.sumOf { linea ->
            val servicio = servicioDao.obtener(linea.servicioId)
            if (servicio?.unidad == Unidad.KG.name) linea.cantidad else 0.0
        }

        val descuento = if (kgLavadoTotal > 5.0) subtotalLavado * 0.10 else 0.0
        val totalFinal = (subtotalTotal - descuento).coerceAtLeast(0.0)

        return database.withTransaction {
            val pedido = Pedido(
                usuarioId = usuarioId,
                fechaRegistro = fechaRegistroStr,
                fechaEntrega = fechaEntregaStr,
                estado = EstadoPedido.RECIBIDO.name,
                total = totalFinal,
                descuento = descuento,
                casillero = null,
                pagado = false,
                nota = nota,
                fechaCreacion = System.currentTimeMillis()
            )
            val pedidoId = pedidoDao.insertar(pedido)

            val detalles = detallesData.map { (servId, cant, precio) ->
                DetallePedido(
                    pedidoId = pedidoId.toInt(),
                    servicioId = servId,
                    cantidad = cant,
                    precioUnitario = precio,
                    subtotal = cant * precio
                )
            }
            detalleDao.insertarTodos(detalles)
            pedidoId
        }
    }

    override fun observarPedidosUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorUsuario(usuarioId)
    }

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarTodos()
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?) {
        pedidoDao.actualizarEstado(id, estado.name, casillero)
    }

    override suspend fun marcarPagado(id: Int, pagado: Boolean) {
        pedidoDao.actualizarPagado(id, pagado)
    }

    override fun observarCasillerosOcupados(): Flow<List<String>> {
        return pedidoDao.observarCasillerosOcupados()
    }

    // PANEL
    override fun observarPedidosDeFecha(fecha: String): Flow<Int> {
        return pedidoDao.contarPorFecha(fecha)
    }

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> {
        return pedidoDao.contarPorEstado(estado.name)
    }

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> {
        return pedidoDao.ingresosDesde(fechaInicio)
    }

    override fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>> {
        return detalleDao.observarIngresosPorServicio(fechaInicio)
    }

    private fun validarCantidad(unidadStr: String, cantidad: Double) {
        val unidad = try {
            Unidad.valueOf(unidadStr)
        } catch (e: Exception) {
            throw IllegalArgumentException("Unidad desconocida: $unidadStr")
        }
        when (unidad) {
            Unidad.KG -> {
                if (cantidad < 1.0 || cantidad > 10.0) {
                    throw IllegalArgumentException("La cantidad para KG debe estar entre 1.0 y 10.0")
                }
                if ((cantidad * 2) % 1.0 != 0.0) {
                    throw IllegalArgumentException("La cantidad para KG debe ser en incrementos de 0.5")
                }
            }
            Unidad.PRENDA, Unidad.UNIDAD -> {
                if (cantidad < 1.0 || cantidad > 30.0) {
                    throw IllegalArgumentException("La cantidad para ${unidad.name} debe estar entre 1 y 30")
                }
                if (cantidad % 1.0 != 0.0) {
                    throw IllegalArgumentException("La cantidad para ${unidad.name} debe ser un valor entero")
                }
            }
        }
    }
}
