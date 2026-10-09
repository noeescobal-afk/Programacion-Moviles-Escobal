package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Usuario

object Repositorio {

    private val usuarios = mutableListOf<Usuario>()

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
}
