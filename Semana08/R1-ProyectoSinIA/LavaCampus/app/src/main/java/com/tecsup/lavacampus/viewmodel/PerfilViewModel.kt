package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Unidad
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PerfilUiState(
    val pedidosActivos: Int = 0,
    val pedidosTotales: Int = 0,
    val kilosLavados: Double = 0.0,
    val cargando: Boolean = true
)

class PerfilViewModel(
    private val repository: LavaRepository,
    private val usuarioId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        observarDatosPerfil()
    }

    private fun observarDatosPerfil() {
        viewModelScope.launch {
            repository.observarPedidosUsuario(usuarioId)
                .catch { _uiState.update { it.copy(cargando = false) } }
                .collect { lista ->
                    val total = lista.size
                    val activos = lista.count { p ->
                        val est = p.pedido.estado
                        est == EstadoPedido.RECIBIDO.name || est == EstadoPedido.EN_PROCESO.name || est == EstadoPedido.LISTO.name
                    }
                    var kilos = 0.0
                    for (pedido in lista) {
                        for (detalleConServicio in pedido.detalles) {
                            if (detalleConServicio.servicio.unidad == Unidad.KG.name) {
                                kilos += detalleConServicio.detalle.cantidad
                            }
                        }
                    }
                    _uiState.update {
                        it.copy(
                            pedidosActivos = activos,
                            pedidosTotales = total,
                            kilosLavados = kilos,
                            cargando = false
                        )
                    }
                }
        }
    }
}
