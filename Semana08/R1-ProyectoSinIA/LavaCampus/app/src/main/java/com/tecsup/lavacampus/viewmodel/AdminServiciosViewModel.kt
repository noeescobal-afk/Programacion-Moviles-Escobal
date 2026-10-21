package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminServiciosUiState(
    val servicios: List<Servicio> = emptyList(),
    val cargando: Boolean = true,
    val mensajeError: String? = null
)

class AdminServiciosViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminServiciosUiState())
    val uiState: StateFlow<AdminServiciosUiState> = _uiState.asStateFlow()

    init {
        observarServicios()
    }

    private fun observarServicios() {
        viewModelScope.launch {
            repository.observarServicios()
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    _uiState.update { it.copy(servicios = lista, cargando = false) }
                }
        }
    }

    fun guardarServicio(
        id: Int,
        nombre: String,
        descripcion: String,
        unidad: String,
        precioStr: String,
        horasStr: String,
        activo: Boolean,
        icono: String,
        onSuccess: () -> Unit
    ) {
        if (nombre.isBlank() || descripcion.isBlank()) {
            _uiState.update { it.copy(mensajeError = "El nombre y la descripción no pueden estar vacíos") }
            return
        }
        val precio = precioStr.toDoubleOrNull() ?: -1.0
        if (precio <= 0.0) {
            _uiState.update { it.copy(mensajeError = "El precio debe ser mayor a 0") }
            return
        }
        val horas = horasStr.toIntOrNull() ?: -1
        if (horas <= 0) {
            _uiState.update { it.copy(mensajeError = "Las horas de entrega deben ser mayores a 0") }
            return
        }

        viewModelScope.launch {
            try {
                val servicio = Servicio(
                    id = id,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    unidad = unidad,
                    precio = precio,
                    horasEntrega = horas,
                    activo = activo,
                    icono = icono.ifBlank { "wash" }
                )
                repository.guardarServicio(servicio)
                _uiState.update { it.copy(mensajeError = null) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = e.message ?: "Error al guardar servicio") }
            }
        }
    }

    fun eliminarOdesactivar(servicio: Servicio, onMensaje: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val tieneDetalles = repository.servicioTieneDetalles(servicio.id)
                if (tieneDetalles) {
                    repository.cambiarActivo(servicio.id, false)
                    onMensaje("El servicio ha sido utilizado en pedidos y se ha desactivado en lugar de eliminarse.")
                } else {
                    repository.eliminarServicio(servicio)
                    onMensaje("Servicio eliminado correctamente.")
                }
            } catch (e: Exception) {
                onMensaje(e.message ?: "Error al procesar el servicio")
            }
        }
    }

    fun cambiarActivo(id: Int, activo: Boolean) {
        viewModelScope.launch {
            repository.cambiarActivo(id, activo)
        }
    }
}
