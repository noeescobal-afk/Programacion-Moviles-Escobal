package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ClientesUiState(
    val clientes: List<ClienteResumen> = emptyList(),
    val searchQuery: String = "",
    val cargando: Boolean = true
)

class ClientesViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientesUiState())
    val uiState: StateFlow<ClientesUiState> = _uiState.asStateFlow()

    init {
        observarClientes()
    }

    private fun observarClientes() {
        viewModelScope.launch {
            repository.observarClientes()
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    _uiState.update {
                        it.copy(
                            clientes = lista,
                            cargando = false
                        )
                    }
                }
        }
    }

    fun actualizarBusqueda(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
