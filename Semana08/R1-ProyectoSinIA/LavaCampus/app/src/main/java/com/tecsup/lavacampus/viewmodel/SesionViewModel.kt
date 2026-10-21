package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SesionUiState(
    val rolSeleccionado: Rol = Rol.CLIENTE,
    val correo: String = "",
    val clave: String = "",
    val usuario: Usuario? = null,
    val cargando: Boolean = false,
    val error: String? = null,
    val perfilGuardado: Rol? = null
)

class SesionViewModel(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SesionUiState())
    val uiState: StateFlow<SesionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferenciasStore.rolSeleccionado.collect { rol ->
                if (rol != null) {
                    _uiState.update { it.copy(rolSeleccionado = rol, perfilGuardado = rol) }
                    if (_uiState.value.usuario == null) {
                        val defaultEmail = if (rol == Rol.ADMIN) "admin@lavacampus.test" else "ana@correo.test"
                        val defaultClave = if (rol == Rol.ADMIN) "admin" else "1234"
                        try {
                            val user = repository.login(defaultEmail, defaultClave)
                            if (user != null) {
                                _uiState.update { it.copy(usuario = user) }
                            }
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                }
            }
        }
    }

    fun seleccionarRol(rol: Rol) {
        _uiState.update { it.copy(rolSeleccionado = rol, error = null) }
        viewModelScope.launch {
            preferenciasStore.setRolSeleccionado(rol)
        }
    }

    fun onCorreoChanged(correo: String) {
        _uiState.update { it.copy(correo = correo, error = null) }
    }

    fun onClaveChanged(clave: String) {
        _uiState.update { it.copy(clave = clave, error = null) }
    }

    fun login() {
        val currentState = _uiState.value
        val correo = currentState.correo.trim()
        val clave = currentState.clave
        val rolSeleccionado = currentState.rolSeleccionado

        if (correo.isBlank() || clave.isBlank()) {
            _uiState.update { it.copy(error = "Por favor ingrese correo y contraseña") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            try {
                val usuario = repository.login(correo, clave)
                if (usuario == null) {
                    _uiState.update { it.copy(cargando = false, error = "Correo o contraseña incorrectos") }
                } else {
                    val rolUsuario = try {
                        Rol.valueOf(usuario.rol)
                    } catch (e: Exception) {
                        null
                    }
                    if (rolUsuario != rolSeleccionado) {
                        _uiState.update { 
                            it.copy(
                                cargando = false, 
                                error = "El rol seleccionado (${rolSeleccionado.name}) no coincide con el perfil del usuario"
                            ) 
                        }
                    } else {
                        preferenciasStore.setRolSeleccionado(rolSeleccionado)
                        _uiState.update { 
                            it.copy(
                                cargando = false, 
                                usuario = usuario, 
                                perfilGuardado = rolSeleccionado,
                                error = null
                            ) 
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(cargando = false, error = e.message ?: "Error al iniciar sesión") }
            }
        }
    }

    fun cambiarPerfil(nuevoRol: Rol) {
        viewModelScope.launch {
            preferenciasStore.setRolSeleccionado(nuevoRol)
            val defaultEmail = if (nuevoRol == Rol.ADMIN) "admin@lavacampus.test" else "ana@correo.test"
            val defaultClave = if (nuevoRol == Rol.ADMIN) "admin" else "1234"
            try {
                val user = repository.login(defaultEmail, defaultClave)
                _uiState.update { 
                    it.copy(
                        rolSeleccionado = nuevoRol,
                        perfilGuardado = nuevoRol,
                        usuario = user,
                        error = null
                    ) 
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferenciasStore.limpiarRol()
            _uiState.update { 
                it.copy(
                    usuario = null, 
                    perfilGuardado = null, 
                    correo = "", 
                    clave = "", 
                    error = null
                ) 
            }
        }
    }
}
