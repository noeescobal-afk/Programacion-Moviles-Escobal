package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.IngresoServicio
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PanelUiState(
    val conteoRecibido: Int = 0,
    val conteoEnProceso: Int = 0,
    val conteoListo: Int = 0,
    val conteoEntregado: Int = 0,
    val conteoCancelado: Int = 0,
    val ingresosTotales: Double = 0.0,
    val ingresosPorServicio: List<IngresoServicio> = emptyList(),
    val cargando: Boolean = true
)

class PanelViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanelUiState())
    val uiState: StateFlow<PanelUiState> = _uiState.asStateFlow()

    init {
        cargarDatosPanel()
    }

    private fun cargarDatosPanel() {
        viewModelScope.launch {
            val fechaInicio = "2020-01-01"

            val f1 = repository.observarCantidadPorEstado(EstadoPedido.RECIBIDO)
            val f2 = repository.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
            val f3 = repository.observarCantidadPorEstado(EstadoPedido.LISTO)
            val f4 = repository.observarCantidadPorEstado(EstadoPedido.ENTREGADO)
            val f5 = repository.observarCantidadPorEstado(EstadoPedido.CANCELADO)
            val f6 = repository.observarIngresosDesde(fechaInicio)
            val f7 = repository.observarIngresosPorServicio(fechaInicio)

            combine(f1, f2, f3) { r, e, l -> Triple(r, e, l) }
                .combine(combine(f4, f5, f6) { ent, can, ing -> Triple(ent, can, ing) }) { t1, t2 ->
                    t1 to t2
                }
                .combine(f7) { pair, servs ->
                    val (t1, t2) = pair
                    PanelUiState(
                        conteoRecibido = t1.first,
                        conteoEnProceso = t1.second,
                        conteoListo = t1.third,
                        conteoEntregado = t2.first,
                        conteoCancelado = t2.second,
                        ingresosTotales = t2.third,
                        ingresosPorServicio = servs,
                        cargando = false
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }
}
