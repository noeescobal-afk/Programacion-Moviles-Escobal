package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MisPedidosUiState(
    val pedidos: List<PedidoConDetalle> = emptyList(),
    val cargando: Boolean = true
)

class MisPedidosViewModel(
    private val repository: LavaRepository,
    private val usuarioId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(MisPedidosUiState())
    val uiState: StateFlow<MisPedidosUiState> = _uiState.asStateFlow()

    init {
        observarPedidos()
    }

    private fun observarPedidos() {
        viewModelScope.launch {
            repository.observarPedidosUsuario(usuarioId)
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    val ordenados = lista.sortedByDescending { it.pedido.fechaCreacion }
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
