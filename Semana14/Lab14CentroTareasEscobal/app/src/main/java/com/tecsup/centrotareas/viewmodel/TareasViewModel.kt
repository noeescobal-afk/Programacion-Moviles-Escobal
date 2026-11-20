package com.tecsup.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()
    private val _progreso = MutableStateFlow(0f)
    val progreso = _progreso.asStateFlow()
    private val _corriendo = MutableStateFlow(false)
    val corriendo = _corriendo.asStateFlow()
    private var trabajo: Job? = null

    // El Paso 1 con Thread.sleep fue únicamente una demostración temporal de clase;
    // el código final no bloquea el hilo principal.
    fun tareaLarga() {
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            try {
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                withContext(Dispatchers.Default) {
                    Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                    for (i in 1..100) { delay(40); _progreso.value = i / 100f }
                }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                throw e
            } finally { _corriendo.value = false }
        }
    }
    fun cancelar() { if (_corriendo.value) { _mensaje.value = "Cancelado"; trabajo?.cancel() } }

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null
    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value == PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }
    fun cancelarPrimos() { if (_primos.value == PrimosUi.Calculando) { _primos.value = PrimosUi.Cancelado; trabajoPrimos?.cancel() } }

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga = _carga.asStateFlow()
    private val _simularError = MutableStateFlow(false)
    val simularError = _simularError.asStateFlow()
    fun cambiarSimularError(valor: Boolean) { _simularError.value = valor }
    fun cargarUnaPorUna() = iniciarCarga(paralelo = false)
    fun cargarALaVez() = iniciarCarga(paralelo = true)
    private fun iniciarCarga(paralelo: Boolean) {
        if (_carga.value == CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = if (paralelo) {
                    coroutineScope {
                        val a = async { repo.cargarPerfil() }
                        val b = async { repo.cargarPedidos(_simularError.value) }
                        Pair(a.await(), b.await())
                    }
                } else {
                    Pair(repo.cargarPerfil(), repo.cargarPedidos(_simularError.value))
                }
                _carga.value = CargaUi.Exito(if (paralelo) "A la vez" else "Una por una", perfil, pedidos.size, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _carga.value = CargaUi.Error("No se pudo cargar: ${e.message ?: "Error desconocido"}") }
        }
    }
}
