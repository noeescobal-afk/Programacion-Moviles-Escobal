package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class InicioUiState(
    val pedidoActivo: PedidoConDetalle? = null,
    val todosLosPedidos: List<PedidoConDetalle> = emptyList(),
    val cargando: Boolean = true
)

class InicioViewModel(
    private val repository: LavaRepository,
    private val usuarioId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarPedidos()
    }

    private fun cargarPedidos() {
        viewModelScope.launch {
            repository.observarPedidosUsuario(usuarioId)
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    val activos = lista.filter { p ->
                        val estado = p.pedido.estado
                        estado == EstadoPedido.RECIBIDO.name ||
                        estado == EstadoPedido.EN_PROCESO.name ||
                        estado == EstadoPedido.LISTO.name
                    }
                    val pedidoMasRelevante = activos.maxByOrNull { it.pedido.fechaCreacion }

                    _uiState.update {
                        it.copy(
                            pedidoActivo = pedidoMasRelevante,
                            todosLosPedidos = lista,
                            cargando = false
                        )
                    }
                }
        }
    }
}
