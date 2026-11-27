package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.flow.*

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) {
            PermisoUi.Concedido
        } else {
            PermisoUi.Denegado
        }
    }
    // TODO Paso 3: mostrar aviso
    // TODO Paso 5: recordatorio
    // TODO Paso 6: receptor de modo avión
    // TODO Paso 8: manejo de errores
}
