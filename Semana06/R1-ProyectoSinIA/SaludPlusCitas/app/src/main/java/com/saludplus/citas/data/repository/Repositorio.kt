package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    private val usuarios = mutableListOf(
        Usuario(
            id = 1,
            nombre = "Noe Escobal",
            correo = "Noe.escobal@tecsup.edu.pe",
            password = "123456"
        )
    )

    private val sedes = listOf(
        Sede(
            id = 1,
            nombre = "SaludPlus San Isidro",
            direccion = "Av. Javier Prado Este 1230, San Isidro",
            telefono = "(01) 619-0001",
            horarioAtencion = "Lun - Sáb: 07:00 - 20:00"
        ),
        Sede(
            id = 2,
            nombre = "SaludPlus Miraflores",
            direccion = "Av. José Pardo 450, Miraflores",
            telefono = "(01) 619-0002",
            horarioAtencion = "Lun - Sáb: 08:00 - 20:00"
        ),
        Sede(
            id = 3,
            nombre = "SaludPlus San Borja",
            direccion = "Av. Guardia Civil 280, San Borja",
            telefono = "(01) 619-0003",
            horarioAtencion = "Lun - Sáb: 07:00 - 19:00"
        )
    )

    private val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención médica primaria y preventiva para la salud integral."),
        Especialidad(2, "Pediatría", "Atención especializada en el cuidado de la salud de niños y adolescentes."),
        Especialidad(3, "Cardiología", "Diagnóstico y tratamiento de afecciones cardíacas y circulatorias."),
        Especialidad(4, "Dermatología", "Especialidad dedicada al cuidado, diagnóstico y tratamiento de la piel."),
        Especialidad(5, "Oftalmología", "Prevención y tratamiento de enfermedades oculares y visión."),
        Especialidad(6, "Ginecología", "Atención médica integral para la salud de la mujer."),
        Especialidad(7, "Traumatología", "Evaluación y tratamiento de lesiones del sistema musculoesquelético.")
    )

    private val medicos = listOf(
        Medico(1, "Dr. Carlos Mendoza", 1, "CMP-12345", 1, "(01) 619-0001 ext. 101"),
        Medico(2, "Dra. Ana Torres", 1, "CMP-12346", 2, "(01) 619-0002 ext. 102"),
        Medico(3, "Dr. Roberto Gómez", 2, "CMP-22345", 1, "(01) 619-0001 ext. 103"),
        Medico(4, "Dra. Lucía Paredes", 2, "CMP-22346", 3, "(01) 619-0003 ext. 104"),
        Medico(5, "Dr. Fernando Ríos", 3, "CMP-32345", 2, "(01) 619-0002 ext. 105"),
        Medico(6, "Dra. Elena Vargas", 3, "CMP-32346", 1, "(01) 619-0001 ext. 106"),
        Medico(7, "Dr. Jorge Benítez", 4, "CMP-42345", 3, "(01) 619-0003 ext. 107"),
        Medico(8, "Dra. María Flores", 5, "CMP-52345", 2, "(01) 619-0002 ext. 108"),
        Medico(9, "Dr. Ricardo Silva", 6, "CMP-62345", 1, "(01) 619-0001 ext. 109"),
        Medico(10, "Dra. Patricia Morales", 7, "CMP-72345", 3, "(01) 619-0003 ext. 110")
    )

    private val citas = mutableListOf<Cita>()

    private val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00",
        "14:00", "15:00", "16:00", "17:00"
    )

    private var _usuarioActual: Usuario? = null

    val usuarioActual: Usuario?
        get() = _usuarioActual

    fun obtenerUsuarios(): List<Usuario> = usuarios.toList()

    fun obtenerSedes(): List<Sede> = sedes.toList()

    fun obtenerSede(id: Int): Sede? = sedes.find { it.id == id }

    fun buscarSedes(texto: String): List<Sede> {
        if (texto.isBlank()) return sedes.toList()
        val query = texto.trim()
        return sedes.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.direccion.contains(query, ignoreCase = true)
        }
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        password: String
    ): Boolean {
        val nombreLimpio = nombre.trim().replace(Regex("\\s+"), " ")
        val correoLimpio = correo.trim().lowercase()

        val nombreValido = Regex(
            "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+(?:[ '-][A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)+$"
        )

        val correoValido = Regex(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )

        if (!nombreValido.matches(nombreLimpio)) return false
        if (!correoValido.matches(correoLimpio)) return false

        if (
            password.length < 6 ||
            password.any { it.isWhitespace() } ||
            password.none { it.isLetter() } ||
            password.none { it.isDigit() }
        ) {
            return false
        }

        val existeCorreo = usuarios.any {
            it.correo.equals(correoLimpio, ignoreCase = true)
        }

        if (existeCorreo) return false

        val nuevoId = (usuarios.maxOfOrNull { it.id } ?: 0) + 1

        val nuevoUsuario = Usuario(
            id = nuevoId,
            nombre = nombreLimpio,
            correo = correoLimpio,
            password = password
        )

        usuarios.add(nuevoUsuario)
        _usuarioActual = nuevoUsuario
        return true
    }

    fun validarCredenciales(correo: String, password: String): Usuario? {
        val correoLimpio = correo.trim()

        if (correoLimpio.isBlank() || password.isBlank()) {
            return null
        }

        return usuarios.find {
            it.correo.equals(correoLimpio, ignoreCase = true) &&
                    it.password == password
        }
    }

    fun establecerUsuarioActual(usuario: Usuario) {
        _usuarioActual = usuario
    }

    fun iniciarSesion(correo: String, password: String): Boolean {
        val usuario = validarCredenciales(correo, password)
        if (usuario != null) {
            _usuarioActual = usuario
            return true
        }
        return false
    }

    fun cerrarSesion() {
        _usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        if (texto.isBlank()) return especialidades.toList()

        return especialidades.filter {
            it.nombre.contains(texto.trim(), ignoreCase = true)
        }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(4)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    fun obtenerEspecialidades(): List<Especialidad> = especialidades.toList()

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun todosLosMedicos(): List<Medico> = medicos.toList()

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedBy { it.nombre }
    }

    fun medicosPorSede(sedeId: Int): List<Medico> {
        return medicos
            .filter { it.sedeId == sedeId }
            .sortedBy { it.nombre }
    }

    fun medicosPorSedeYEspecialidad(sedeId: Int, especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.sedeId == sedeId && it.especialidadId == especialidadId }
            .sortedBy { it.nombre }
    }

    fun buscarMedicos(
        especialidadId: Int,
        texto: String
    ): List<Medico> {
        val medicosEspecialidad = medicos.filter {
            it.especialidadId == especialidadId
        }

        if (texto.isBlank()) {
            return medicosEspecialidad.sortedBy { it.nombre }
        }

        return medicosEspecialidad
            .filter {
                it.nombre.contains(texto.trim(), ignoreCase = true)
            }
            .sortedBy { it.nombre }
    }

    fun buscarTodosLosMedicos(texto: String): List<Medico> {
        if (texto.isBlank()) return medicos.sortedBy { it.nombre }
        val query = texto.trim()
        return medicos.filter { medico ->
            val especialidad = obtenerEspecialidad(medico.especialidadId)
            val sede = obtenerSede(medico.sedeId)
            medico.nombre.contains(query, ignoreCase = true) ||
                    (especialidad?.nombre?.contains(query, ignoreCase = true) == true) ||
                    (sede?.nombre?.contains(query, ignoreCase = true) == true)
        }.sortedBy { it.nombre }
    }

    fun horariosDisponibles(
        medicoId: Int,
        fecha: String
    ): List<String> {
        if (medicos.none { it.id == medicoId } || fecha.isBlank()) {
            return emptyList()
        }

        val fechaLimpia = fecha.trim()

        val horasOcupadas = citas
            .filter {
                it.medicoId == medicoId &&
                        it.fecha == fechaLimpia &&
                        it.estado != "Cancelada"
            }
            .map { it.hora }

        return horariosBase.filter { it !in horasOcupadas }
    }

    fun agendarCita(
        usuarioId: Int,
        medicoId: Int,
        fecha: String,
        hora: String,
        sedeId: Int = 1
    ): Cita? {
        val fechaLimpia = fecha.trim()
        val horaLimpia = hora.trim()

        if (
            usuarioId <= 0 ||
            medicoId <= 0 ||
            fechaLimpia.isBlank() ||
            horaLimpia.isBlank()
        ) {
            return null
        }

        val existeUsuario = usuarios.any { it.id == usuarioId }
        val medico = obtenerMedico(medicoId) ?: return null

        if (!existeUsuario) return null
        if (horaLimpia !in horariosBase) return null

        val disponibles = horariosDisponibles(medicoId, fechaLimpia)
        if (horaLimpia !in disponibles) return null

        val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1
        val sedeRealId = if (sedeId > 0) sedeId else medico.sedeId

        val nuevaCita = Cita(
            id = nuevoId,
            usuarioId = usuarioId,
            medicoId = medicoId,
            sedeId = sedeRealId,
            fecha = fechaLimpia,
            hora = horaLimpia,
            estado = "Programada"
        )

        citas.add(nuevaCita)
        return nuevaCita
    }

    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    fun citasDelUsuario(usuarioId: Int): List<Cita> {
        return citas
            .filter { it.usuarioId == usuarioId }
            .sortedWith(
                compareBy<Cita> { convertirFechaAOrden(it.fecha) }
                    .thenBy { it.hora }
            )
    }

    fun cancelarCita(citaId: Int): Boolean {
        val indice = citas.indexOfFirst { it.id == citaId }
        if (indice == -1) return false

        val cita = citas[indice]

        if (cita.estado == "Cancelada") {
            return false
        }

        citas[indice] = cita.copy(estado = "Cancelada")
        return true
    }

    fun obtenerCitas(): List<Cita> = citas.toList()

    private fun convertirFechaAOrden(fecha: String): String {
        val partes = fecha.split("/")
        return if (partes.size == 3) {
            "${partes[2]}${partes[1]}${partes[0]}"
        } else {
            fecha
        }
    }
}
