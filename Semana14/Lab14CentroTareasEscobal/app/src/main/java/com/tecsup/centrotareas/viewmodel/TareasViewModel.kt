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
import kotlinx.coroutines.flow.collect

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
                // Mejora 1: la fuente de progreso vive en el Repository.
                Log.d("HILO", "Dentro: Repository / Dispatchers.Default (flowOn)")
                repo.progresoTarea().collect { avance -> _progreso.value = avance }
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

    // Mejora 3: historial comparativo en memoria de la sesión (hasta 5 por modo).
    private val _tiemposSecuenciales = MutableStateFlow<List<Long>>(emptyList())
    val tiemposSecuenciales = _tiemposSecuenciales.asStateFlow()
    private val _tiemposParalelos = MutableStateFlow<List<Long>>(emptyList())
    val tiemposParalelos = _tiemposParalelos.asStateFlow()
    private val _intento = MutableStateFlow(0)
    val intento = _intento.asStateFlow()

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
                _intento.value = 0
                val (perfil, pedidos) = if (paralelo) {
                    coroutineScope {
                        val a = async { repo.cargarPerfil() }
                        val b = async { pedidosConReintento(_simularError.value) }
                        Pair(a.await(), b.await())
                    }
                } else {
                    Pair(repo.cargarPerfil(), pedidosConReintento(_simularError.value))
                }
                val ms = System.currentTimeMillis() - inicio
                if (paralelo) _tiemposParalelos.value = (_tiemposParalelos.value + ms).takeLast(5)
                else _tiemposSecuenciales.value = (_tiemposSecuenciales.value + ms).takeLast(5)
                _carga.value = CargaUi.Exito(if (paralelo) "A la vez" else "Una por una", perfil, pedidos.size, ms)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _carga.value = CargaUi.Error("No se pudo cargar: ${e.message ?: "Error desconocido"}") }
        }
    }

    // Mejora 2: reintento automático hasta 3 intentos, 500 ms y 1000 ms de espera.
    // La cancelación siempre se propaga; jamás se trata como error de red.
    private suspend fun pedidosConReintento(error: Boolean): List<String> {
        repeat(3) { indice ->
            _intento.value = indice + 1
            try { return repo.cargarPedidos(error) }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                if (indice == 2) throw e
                delay(500L * (indice + 1))
            }
        }
        error("Reintentos agotados")
    }
}
