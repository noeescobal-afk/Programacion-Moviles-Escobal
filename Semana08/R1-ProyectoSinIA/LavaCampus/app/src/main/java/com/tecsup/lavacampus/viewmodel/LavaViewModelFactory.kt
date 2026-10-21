package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository

class LavaViewModelFactory(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore,
    private val usuarioId: Int = 0,
    private val pedidoId: Int = 0
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(repository, preferenciasStore) as T
        } else if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            return InicioViewModel(repository, usuarioId) as T
        } else if (modelClass.isAssignableFrom(NuevoPedidoViewModel::class.java)) {
            return NuevoPedidoViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetallePedidoViewModel::class.java)) {
            return DetallePedidoViewModel(repository, pedidoId) as T
        } else if (modelClass.isAssignableFrom(MisPedidosViewModel::class.java)) {
            return MisPedidosViewModel(repository, usuarioId) as T
        } else if (modelClass.isAssignableFrom(TarifasViewModel::class.java)) {
            return TarifasViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(PanelViewModel::class.java)) {
            return PanelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
