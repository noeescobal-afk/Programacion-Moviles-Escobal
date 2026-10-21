package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BandejaUiState(
    val pedidos: List<PedidoConDetalle> = emptyList(),
    val cargando: Boolean = true
)

class BandejaViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BandejaUiState())
    val uiState: StateFlow<BandejaUiState> = _uiState.asStateFlow()

    init {
        observarPedidos()
    }

    private fun observarPedidos() {
        viewModelScope.launch {
            repository.observarPedidos()
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    val prioridad = mapOf(
                        EstadoPedido.RECIBIDO.name to 1,
                        EstadoPedido.EN_PROCESO.name to 2,
                        EstadoPedido.LISTO.name to 3,
                        EstadoPedido.ENTREGADO.name to 4,
                        EstadoPedido.CANCELADO.name to 5
                    )
                    val ordenados = lista.sortedWith(
                        compareBy<PedidoConDetalle> { prioridad[it.pedido.estado] ?: 99 }
                            .thenByDescending { it.pedido.fechaCreacion }
                    )
                    _uiState.update {
                        it.copy(
                            pedidos = ordenados,
                            cargando = false
                        )
                    }
                }
        }
    }
}
