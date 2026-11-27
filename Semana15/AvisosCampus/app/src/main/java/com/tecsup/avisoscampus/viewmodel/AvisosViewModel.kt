package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
    val avisos = repo.avisos
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos = _eventos.asSharedFlow()
    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso = _permiso.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan = _faltan.asStateFlow()
    private var trabajo: Job? = null
    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion = _escuchandoAvion.asStateFlow()
    private val _escuchandoCargador = MutableStateFlow(false)
    val escuchandoCargador = _escuchandoCargador.asStateFlow()
    private val _modoTema = MutableStateFlow(ModoTema.AUTOMATICO)
    val modoTema = _modoTema.asStateFlow()

    fun cambiarModoTema(modo: ModoTema) {
        _modoTema.value = modo
    }

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
        if (concedido) _mensaje.value = null
    }
    fun noSePudoMostrar() {
        _mensaje.value = "Sin permiso: el aviso se guardó en el historial, pero no se mostró"
    }
    private suspend fun emitir(titulo: String, texto: String, origen: Origen) {
        _eventos.emit(repo.registrar(titulo, texto, origen))
    }
    fun avisarAhora() { viewModelScope.launch { emitir("Tarea lista", "Tu entrega fue registrada", Origen.BOTON) } }
    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true) return
        trabajo = viewModelScope.launch {
            try {
                for (i in segundos downTo 1) { _faltan.value = i; delay(1000) }
                emitir("Recordatorio", "Revisa tu laboratorio pendiente. Comprueba las instrucciones, completa las actividades y prepara tus evidencias antes de realizar la entrega.", Origen.RECORDATORIO)
            } finally { _faltan.value = null }
        }
    }
    fun cancelarRecordatorio() { trabajo?.cancel() }
    fun cambiarEscucha(valor: Boolean) { _escuchandoAvion.value = valor }
    fun cambiarEscuchaCargador(valor: Boolean) { _escuchandoCargador.value = valor }
    fun alCambiarModoAvion(activo: Boolean) {
        if (_escuchandoAvion.value) viewModelScope.launch { emitir("Modo avión", if (activo) "Activado" else "Desactivado", Origen.MODO_AVION) }
    }
    fun alCambiarCargador(conectado: Boolean) {
        if (_escuchandoCargador.value) viewModelScope.launch { emitir("Cargador", if (conectado) "Conectado" else "Desconectado", Origen.CARGADOR) }
    }
}
