package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GestionPedidoUiState(
    val pedidoConDetalle: PedidoConDetalle? = null,
    val casillerosOcupados: List<String> = emptyList(),
    val casilleroSeleccionado: String? = null,
    val cargando: Boolean = true,
    val error: String? = null,
    val mensajeExito: String? = null
)

class GestionPedidoViewModel(
    private val repository: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(GestionPedidoUiState())
    val uiState: StateFlow<GestionPedidoUiState> = _uiState.asStateFlow()

    init {
        observarDatos()
    }

    private fun observarDatos() {
        viewModelScope.launch {
            combine(
                repository.observarPedido(pedidoId),
                repository.observarCasillerosOcupados()
            ) { pedido, ocupados ->
                Pair(pedido, ocupados)
            }.catch { e ->
                _uiState.update { it.copy(cargando = false, error = e.message) }
            }.collect { (pedido, ocupados) ->
                _uiState.update {
                    it.copy(
                        pedidoConDetalle = pedido,
                        casillerosOcupados = ocupados,
                        casilleroSeleccionado = pedido?.pedido?.casillero ?: it.casilleroSeleccionado,
                        cargando = false
                    )
                }
            }
        }
    }

    fun seleccionarCasillero(casillero: String) {
        _uiState.update { it.copy(casilleroSeleccionado = casillero, error = null) }
    }

    fun avanzarEstado() {
        val estadoActual = _uiState.value.pedidoConDetalle?.pedido?.estado ?: return
        val casillero = _uiState.value.casilleroSeleccionado
        val ocupados = _uiState.value.casillerosOcupados
        val pagado = _uiState.value.pedidoConDetalle?.pedido?.pagado ?: false

        when (estadoActual) {
            EstadoPedido.RECIBIDO.name -> {
                cambiarEstadoSeguro(EstadoPedido.EN_PROCESO, null)
            }
            EstadoPedido.EN_PROCESO.name -> {
                if (casillero.isNullOrBlank()) {
                    _uiState.update { it.copy(error = "Selecciona un casillero disponible.") }
                    return
                }
                if (ocupados.contains(casillero)) {
                    _uiState.update { it.copy(error = "El casillero $casillero ya está ocupado.") }
                    return
                }
                cambiarEstadoSeguro(EstadoPedido.LISTO, casillero)
            }
            EstadoPedido.LISTO.name -> {
                if (!pagado) {
                    _uiState.update { it.copy(error = "El pedido debe estar pagado antes de entregarlo.") }
                    return
                }
                cambiarEstadoSeguro(EstadoPedido.ENTREGADO, _uiState.value.pedidoConDetalle?.pedido?.casillero)
            }
            else -> {
                _uiState.update { it.copy(error = "Este pedido ya no puede avanzar de estado.") }
            }
        }
    }

    fun marcarPagado(pagado: Boolean) {
        viewModelScope.launch {
            try {
                repository.marcarPagado(pedidoId, pagado)
                _uiState.update { it.copy(error = null, mensajeExito = if (pagado) "Pedido marcado como pagado" else "Pago pendiente") }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Error al actualizar pago") }
            }
        }
    }

    fun cancelarPedido(onSuccess: () -> Unit) {
        val estadoActual = _uiState.value.pedidoConDetalle?.pedido?.estado ?: return
        if (estadoActual != EstadoPedido.RECIBIDO.name && estadoActual != EstadoPedido.EN_PROCESO.name) {
            _uiState.update { it.copy(error = "El pedido ya no puede ser cancelado.") }
            return
        }

        viewModelScope.launch {
            try {
                repository.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Error al cancelar el pedido") }
            }
        }
    }

    private fun cambiarEstadoSeguro(nuevoEstado: EstadoPedido, casillero: String?) {
        viewModelScope.launch {
            try {
                repository.cambiarEstado(pedidoId, nuevoEstado, casillero)
                _uiState.update { it.copy(error = null, mensajeExito = "Estado actualizado a ${nuevoEstado.name}") }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Error al cambiar estado") }
            }
        }
    }
}
