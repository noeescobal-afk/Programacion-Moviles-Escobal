package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

object Repositorio {

    private val usuarios = mutableListOf<Usuario>()

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
        Medico(1, "Dr. Carlos Mendoza", 1, "CMP-12345"),
        Medico(2, "Dra. Ana Torres", 1, "CMP-12346"),
        Medico(3, "Dr. Roberto Gómez", 2, "CMP-22345"),
        Medico(4, "Dra. Lucía Paredes", 2, "CMP-22346"),
        Medico(5, "Dr. Fernando Ríos", 3, "CMP-32345"),
        Medico(6, "Dra. Elena Vargas", 3, "CMP-32346"),
        Medico(7, "Dr. Jorge Benítez", 4, "CMP-42345"),
        Medico(8, "Dra. María Flores", 5, "CMP-52345"),
        Medico(9, "Dr. Ricardo Silva", 6, "CMP-62345"),
        Medico(10, "Dra. Patricia Morales", 7, "CMP-72345")
    )

    private var _usuarioActual: Usuario? = null

    val usuarioActual: Usuario?
        get() = _usuarioActual

    fun obtenerUsuarios(): List<Usuario> {
        return usuarios.toList()
    }

    fun registrarUsuario(nombre: String, correo: String, password: String): Boolean {
        if (nombre.isBlank() || correo.isBlank() || password.isBlank()) {
            return false
        }

        val existeCorreo = usuarios.any { it.correo.equals(correo.trim(), ignoreCase = true) }
        if (existeCorreo) {
            return false
        }

        val nuevoId = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
        val nuevoUsuario = Usuario(
            id = nuevoId,
            nombre = nombre.trim(),
            correo = correo.trim(),
            password = password
        )

        usuarios.add(nuevoUsuario)
        return true
    }

    fun iniciarSesion(correo: String, password: String): Boolean {
        if (correo.isBlank() || password.isBlank()) {
            return false
        }

        val usuarioEncontrado = usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.password == password
        }

        if (usuarioEncontrado != null) {
            _usuarioActual = usuarioEncontrado
            return true
        }

        return false
    }

    fun cerrarSesion() {
        _usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        if (texto.isBlank()) {
            return especialidades.toList()
        }
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

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.nombre }
    }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        val medicosEspecialidad = medicos.filter { it.especialidadId == especialidadId }
        if (texto.isBlank()) {
            return medicosEspecialidad.sortedBy { it.nombre }
        }
        return medicosEspecialidad
            .filter { it.nombre.contains(texto.trim(), ignoreCase = true) }
            .sortedBy { it.nombre }
    }
}
