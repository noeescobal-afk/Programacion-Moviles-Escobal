package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class NuevoPedidoUiState(
    val servicios: List<Servicio> = emptyList(),
    val cantidades: Map<Int, Double> = emptyMap(),
    val nota: String = "",
    val cargando: Boolean = true,
    val error: String? = null,
    val exito: Boolean = false,
    val guardando: Boolean = false,
    val recibirPedidos: Boolean = true
)

class NuevoPedidoViewModel(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(NuevoPedidoUiState())
    val uiState: StateFlow<NuevoPedidoUiState> = _uiState.asStateFlow()

    init {
        observarServicios()
        viewModelScope.launch {
            preferenciasStore.recibirPedidos.collect { activo ->
                _uiState.update { it.copy(recibirPedidos = activo) }
            }
        }
    }

    private fun observarServicios() {
        viewModelScope.launch {
            repository.observarServiciosActivos()
                .catch { e -> _uiState.update { it.copy(cargando = false, error = e.message) } }
                .collect { lista ->
                    _uiState.update { it.copy(servicios = lista, cargando = false) }
                }
        }
    }

    fun actualizarCantidad(servicioId: Int, cantidad: Double) {
        _uiState.update { state ->
            val nuevasCantidades = state.cantidades.toMutableMap()
            if (cantidad <= 0.0) {
                nuevasCantidades.remove(servicioId)
            } else {
                nuevasCantidades[servicioId] = cantidad
            }
            state.copy(cantidades = nuevasCantidades, error = null)
        }
    }

    fun actualizarNota(nota: String) {
        _uiState.update { it.copy(nota = nota, error = null) }
    }

    val totalEstimado: Double
        get() {
            val state = _uiState.value
            return state.cantidades.entries.sumOf { (servId, cant) ->
                val servicio = state.servicios.find { it.id == servId }
                val precio = servicio?.precio ?: 0.0
                cant * precio
            }
        }

    val fechaEstimadaEntrega: String
        get() {
            val state = _uiState.value
            var maxHoras = 0
            for ((servId, cant) in state.cantidades) {
                if (cant > 0.0) {
                    val servicio = state.servicios.find { it.id == servId }
                    if (servicio != null && servicio.horasEntrega > maxHoras) {
                        maxHoras = servicio.horasEntrega
                    }
                }
            }
            val horas = if (maxHoras > 0) maxHoras else 24
            val dias = horas / 24
            var fecha = LocalDate.now().plusDays(dias.toLong())
            if (fecha.dayOfWeek == DayOfWeek.SUNDAY) {
                fecha = fecha.plusDays(1)
            }
            return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

    val tieneSeleccionValida: Boolean
        get() {
            val state = _uiState.value
            return state.cantidades.values.any { it > 0.0 }
        }

    fun confirmarPedido(usuarioId: Int, onComplete: () -> Unit) {
        val state = _uiState.value
        if (!state.recibirPedidos) {
            _uiState.update { it.copy(error = "La lavandería no está recibiendo pedidos") }
            return
        }

        val lineas = state.cantidades.mapNotNull { (servId, cant) ->
            if (cant > 0.0) {
                val servicio = state.servicios.find { it.id == servId }
                val precio = servicio?.precio ?: 0.0
                LineaPedido(
                    servicioId = servId,
                    cantidad = cant,
                    precioUnitario = precio,
                    subtotal = cant * precio
                )
            } else null
        }

        if (lineas.isEmpty()) {
            _uiState.update { it.copy(error = "Debe seleccionar al menos un servicio con cantidad mayor a 0") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, error = null) }
            try {
                repository.crearPedido(usuarioId, lineas, state.nota)
                _uiState.update { it.copy(guardando = false, exito = true) }
                onComplete()
            } catch (e: Exception) {
                val mensajeError = if (e.message?.contains("3 pedidos activos") == true || e.message?.contains("activos") == true) {
                    "Ya tienes 3 pedidos activos."
                } else {
                    e.message ?: "Error al crear el pedido"
                }
                _uiState.update { it.copy(guardando = false, error = mensajeError) }
            }
        }
    }
}
