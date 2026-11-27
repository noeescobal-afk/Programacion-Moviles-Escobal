package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        if (concedido) {
            _mensaje.value = null
        }
        _permiso.value = if (concedido) {
            PermisoUi.Concedido
        } else {
            PermisoUi.Denegado
        }
    }
    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar(
                "Tarea lista",
                "Tu entrega fue registrada",
                Origen.BOTON
            )
            _eventos.emit(aviso)
        }
    }
    private var trabajo: Job? = null

    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true) return

        trabajo = viewModelScope.launch {
            try {
                for (i in segundos downTo 1) {
                    _faltan.value = i
                    delay(1000)
                }

                val aviso = repo.registrar(
                    "Recordatorio",
                    "Revisa tu laboratorio pendiente",
                    Origen.RECORDATORIO
                )

                _eventos.emit(aviso)

            } finally {
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }
    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion = _escuchandoAvion.asStateFlow()

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    fun alCambiarModoAvion(activo: Boolean) {
        if (!_escuchandoAvion.value) return

        viewModelScope.launch {
            val aviso = repo.registrar(
                "Modo avión",
                if (activo) "Activado" else "Desactivado",
                Origen.MODO_AVION
            )

            _eventos.emit(aviso)
        }
    }
    fun noSePudoMostrar() {
        _mensaje.value = "Sin permiso: el aviso se guardó en el historial, pero no se mostró"
    }
}
