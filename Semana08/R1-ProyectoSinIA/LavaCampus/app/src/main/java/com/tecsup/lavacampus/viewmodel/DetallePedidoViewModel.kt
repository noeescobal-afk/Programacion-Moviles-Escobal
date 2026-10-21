package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DetallePedidoUiState(
    val pedidoConDetalle: PedidoConDetalle? = null,
    val cargando: Boolean = true,
    val error: String? = null,
    val cancelando: Boolean = false
)

class DetallePedidoViewModel(
    private val repository: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePedidoUiState())
    val uiState: StateFlow<DetallePedidoUiState> = _uiState.asStateFlow()

    init {
        observarPedido()
    }

    private fun observarPedido() {
        viewModelScope.launch {
            repository.observarPedido(pedidoId)
                .catch { e -> _uiState.update { it.copy(cargando = false, error = e.message) } }
                .collect { pedido ->
                    _uiState.update {
                        it.copy(
                            pedidoConDetalle = pedido,
                            cargando = false
                        )
                    }
                }
        }
    }

    fun cancelarPedido(onSuccess: () -> Unit) {
        val currentPedido = _uiState.value.pedidoConDetalle?.pedido
        if (currentPedido == null) {
            _uiState.update { it.copy(error = "Pedido no encontrado") }
            return
        }

        if (currentPedido.estado != EstadoPedido.RECIBIDO.name) {
            _uiState.update { it.copy(error = "El pedido ya no puede ser cancelado.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cancelando = true, error = null) }
            try {
                repository.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
                _uiState.update { it.copy(cancelando = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cancelando = false,
                        error = e.message ?: "Error al cancelar el pedido"
                    )
                }
            }
        }
    }
}
