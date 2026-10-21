package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TarifasUiState(
    val servicios: List<Servicio> = emptyList(),
    val cargando: Boolean = true
)

class TarifasViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TarifasUiState())
    val uiState: StateFlow<TarifasUiState> = _uiState.asStateFlow()

    init {
        observarServicios()
    }

    private fun observarServicios() {
        viewModelScope.launch {
            repository.observarServiciosActivos()
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    _uiState.update {
                        it.copy(
                            servicios = lista,
                            cargando = false
                        )
                    }
                }
        }
    }
}
