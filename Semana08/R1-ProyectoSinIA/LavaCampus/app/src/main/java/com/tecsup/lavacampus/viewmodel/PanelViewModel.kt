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
    val conteoAtrasados: Int = 0,
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
            val f8 = repository.observarCantidadAtrasados()

            val flows = listOf(f1, f2, f3, f4, f5, f6, f7, f8)
            combine(flows) { args ->
                val r = args[0] as Int
                val e = args[1] as Int
                val l = args[2] as Int
                val ent = args[3] as Int
                val can = args[4] as Int
                val ing = args[5] as Double
                @Suppress("UNCHECKED_CAST")
                val servs = args[6] as List<IngresoServicio>
                val atrasados = args[7] as Int

                PanelUiState(
                    conteoRecibido = r,
                    conteoEnProceso = e,
                    conteoListo = l,
                    conteoEntregado = ent,
                    conteoCancelado = can,
                    conteoAtrasados = atrasados,
                    ingresosTotales = ing,
                    ingresosPorServicio = servs,
                    cargando = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
