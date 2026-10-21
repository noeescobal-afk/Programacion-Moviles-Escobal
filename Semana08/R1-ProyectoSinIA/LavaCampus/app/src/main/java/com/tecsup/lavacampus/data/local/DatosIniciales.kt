package com.tecsup.lavacampus.data.local

import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario

object DatosIniciales {

    suspend fun sembrar(database: LavaDatabase) {
        val servicioDao = database.servicioDao()
        val usuarioDao = database.usuarioDao()
        val pedidoDao = database.pedidoDao()
        val detalleDao = database.detalleDao()

        if (servicioDao.contar() > 0 || usuarioDao.contar() > 0) {
            return
        }

        val servicios = listOf(
            Servicio(
                nombre = "Lavado y secado",
                descripcion = "Lavado y secado de ropa por kilogramo",
                unidad = "KG",
                precio = 8.00,
                horasEntrega = 24,
                activo = true,
                icono = "wash"
            ),
            Servicio(
                nombre = "Planchado",
                descripcion = "Planchado de prendas",
                unidad = "PRENDA",
                precio = 2.50,
                horasEntrega = 48,
                activo = true,
                icono = "iron"
            ),
            Servicio(
                nombre = "Lavado en seco",
                descripcion = "Lavado especializado en seco",
                unidad = "PRENDA",
                precio = 12.00,
                horasEntrega = 48,
                activo = true,
                icono = "shirt"
            ),
            Servicio(
                nombre = "Edredón / frazada",
                descripcion = "Lavado de edredones y frazadas",
                unidad = "UNIDAD",
                precio = 20.00,
                horasEntrega = 48,
                activo = false,
                icono = "wash"
            )
        )

        val servicioIds = mutableListOf<Long>()
        for (s in servicios) {
            val id = servicioDao.insertar(s)
            servicioIds.add(id)
        }
        val idLavadoSecado = servicioIds[0].toInt()
        val idPlanchado = servicioIds[1].toInt()
        val idLavadoSeco = servicioIds[2].toInt()
        val idEdredon = servicioIds[3].toInt()

        val usuarios = listOf(
            Usuario(nombre = "Ana Gómez", codigo = "EST-001", correo = "ana@correo.test", clave = "1234", rol = "CLIENTE"),
            Usuario(nombre = "Luis Pérez", codigo = "EST-002", correo = "luis@correo.test", clave = "1234", rol = "CLIENTE"),
            Usuario(nombre = "Marta Torres", codigo = "EST-003", correo = "marta@correo.test", clave = "1234", rol = "CLIENTE"),
            Usuario(nombre = "Administrador", codigo = "", correo = "admin@lavacampus.test", clave = "admin", rol = "ADMIN")
        )

        val usuarioIds = mutableListOf<Long>()
        for (u in usuarios) {
            val id = usuarioDao.insertar(u)
            usuarioIds.add(id)
        }
        val idAna = usuarioIds[0].toInt()
        val idLuis = usuarioIds[1].toInt()
        val idMarta = usuarioIds[2].toInt()

        data class PedidoEjemplo(
            val usuarioId: Int,
            val fechaRegistro: String,
            val fechaEntrega: String,
            val estado: String,
            val casillero: String?,
            val pagado: Boolean,
            val nota: String,
            val detalles: List<Pair<Int, Double>>
        )

        val pedidosEjemplo = listOf(
            PedidoEjemplo(
                usuarioId = idAna,
                fechaRegistro = "2026-10-15",
                fechaEntrega = "2026-10-16",
                estado = "RECIBIDO",
                casillero = null,
                pagado = false,
                nota = "Ropa blanca separada",
                detalles = listOf(Pair(idLavadoSecado, 3.0))
            ),
            PedidoEjemplo(
                usuarioId = idAna,
                fechaRegistro = "2026-10-14",
                fechaEntrega = "2026-10-16",
                estado = "EN_PROCESO",
                casillero = null,
                pagado = true,
                nota = "Uso de suavizante",
                detalles = listOf(Pair(idPlanchado, 5.0))
            ),
            PedidoEjemplo(
                usuarioId = idAna,
                fechaRegistro = "2026-10-12",
                fechaEntrega = "2026-10-14",
                estado = "LISTO",
                casillero = "C-03",
                pagado = true,
                nota = "",
                detalles = listOf(Pair(idLavadoSeco, 1.0))
            ),
            PedidoEjemplo(
                usuarioId = idLuis,
                fechaRegistro = "2026-10-15",
                fechaEntrega = "2026-10-17",
                estado = "RECIBIDO",
                casillero = null,
                pagado = false,
                nota = "Cuidado con camisas",
                detalles = listOf(Pair(idPlanchado, 4.0))
            ),
            PedidoEjemplo(
                usuarioId = idLuis,
                fechaRegistro = "2026-10-13",
                fechaEntrega = "2026-10-15",
                estado = "EN_PROCESO",
                casillero = null,
                pagado = false,
                nota = "",
                detalles = listOf(Pair(idLavadoSecado, 4.5))
            ),
            PedidoEjemplo(
                usuarioId = idLuis,
                fechaRegistro = "2026-10-11",
                fechaEntrega = "2026-10-13",
                estado = "LISTO",
                casillero = "C-08",
                pagado = true,
                nota = "Pedido histórico con edredón",
                detalles = listOf(Pair(idEdredon, 1.0))
            ),
            PedidoEjemplo(
                usuarioId = idLuis,
                fechaRegistro = "2026-10-01",
                fechaEntrega = "2026-10-03",
                estado = "ENTREGADO",
                casillero = null,
                pagado = true,
                nota = "",
                detalles = listOf(Pair(idLavadoSecado, 2.0))
            ),
            PedidoEjemplo(
                usuarioId = idMarta,
                fechaRegistro = "2026-10-05",
                fechaEntrega = "2026-10-07",
                estado = "ENTREGADO",
                casillero = null,
                pagado = true,
                nota = "",
                detalles = listOf(Pair(idLavadoSeco, 2.0))
            ),
            PedidoEjemplo(
                usuarioId = idMarta,
                fechaRegistro = "2026-10-08",
                fechaEntrega = "2026-10-10",
                estado = "CANCELADO",
                casillero = null,
                pagado = false,
                nota = "Cancelado por el cliente",
                detalles = listOf(Pair(idPlanchado, 3.0))
            ),
            PedidoEjemplo(
                usuarioId = idMarta,
                fechaRegistro = "2026-10-15",
                fechaEntrega = "2026-10-16",
                estado = "RECIBIDO",
                casillero = null,
                pagado = true,
                nota = "Pago adelantado",
                detalles = listOf(Pair(idLavadoSecado, 5.0))
            )
        )

        val preciosServicio = mapOf(
            idLavadoSecado to 8.00,
            idPlanchado to 2.50,
            idLavadoSeco to 12.00,
            idEdredon to 20.00
        )

        for (pe in pedidosEjemplo) {
            var totalPedido = 0.0
            val detallesCalculados = pe.detalles.map { (servId, cant) ->
                val precioU = preciosServicio[servId] ?: 0.0
                val sub = cant * precioU
                totalPedido += sub
                Triple(servId, cant, precioU)
            }

            val pedido = Pedido(
                usuarioId = pe.usuarioId,
                fechaRegistro = pe.fechaRegistro,
                fechaEntrega = pe.fechaEntrega,
                estado = pe.estado,
                total = totalPedido,
                casillero = pe.casillero,
                pagado = pe.pagado,
                nota = pe.nota,
                fechaCreacion = System.currentTimeMillis()
            )

            val pedidoId = pedidoDao.insertar(pedido).toInt()

            val listaDetalles = detallesCalculados.map { (servId, cant, precioU) ->
                DetallePedido(
                    pedidoId = pedidoId,
                    servicioId = servId,
                    cantidad = cant,
                    precioUnitario = precioU,
                    subtotal = cant * precioU
                )
            }
            detalleDao.insertarTodos(listaDetalles)
        }
    }
}
