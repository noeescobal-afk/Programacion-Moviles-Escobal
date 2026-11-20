package com.tecsup.campusgps.viewmodel

// ARCHIVO: MapaViewModel.kt
// Gestor de estado avanzado para CampusGPS (TECSUP Lima & Demo Académico) con diseño premium e institucional.

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.LugaresRepositoryPreferences
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.AccionMapa
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.ModoCampus
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    init {
        Log.d("CampusGPS_VM", "MapaViewModel premium inicializado.")
    }

    // Coordenadas base de los campus
    val centroTecsupLima = Posicion(-12.0453, -76.9533)
    val centroDemoAcademico = Posicion(-12.0464, -77.0428)

    // Modo de campus activo (TECSUP Lima o Demo Académico)
    private val _modoCampus = MutableStateFlow(ModoCampus.TECSUP_LIMA)
    val modoCampus = _modoCampus.asStateFlow()

    // Pestaña de navegación inferior (0: MAPA, 1: EXPLORAR, 2: FAVORITOS)
    private val _pestanaNavegacion = MutableStateFlow(0)
    val pestanaNavegacion = _pestanaNavegacion.asStateFlow()

    // Estado de la ubicación GPS del usuario
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()

    // Lugares con distancias calculadas
    private val _lugaresConDistancia = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { lugar ->
            val dist = if (lugar.modo == ModoCampus.TECSUP_LIMA && !lugar.ubicada) null else null
            LugarConDistancia(lugar, dist)
        }
    )
    val lugares = _lugaresConDistancia.asStateFlow()

    // Pestaña interna del panel ("Todos", "Favoritos", "Recientes")
    private val _pestanaActiva = MutableStateFlow("Todos")
    val pestanaActiva = _pestanaActiva.asStateFlow()

    // Categoría seleccionada para filtros
    private val _categoria = MutableStateFlow("Todos")
    val categoria = _categoria.asStateFlow()

    // Consulta de búsqueda por texto o necesidades
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    // Favoritos y Recientes (IDs)
    private val _favoritos = MutableStateFlow(
        if (lugaresRepo is LugaresRepositoryPreferences) lugaresRepo.obtenerFavoritos() else emptySet()
    )
    val favoritos = _favoritos.asStateFlow()

    private val _recientes = MutableStateFlow(
        if (lugaresRepo is LugaresRepositoryPreferences) lugaresRepo.obtenerRecientes() else emptyList()
    )
    val recientes = _recientes.asStateFlow()

    // Destino para "Cómo llegar" (Ruta guía)
    private val _rutaDestino = MutableStateFlow<Lugar?>(null)
    val rutaDestino = _rutaDestino.asStateFlow()

    // Lugar seleccionado actualmente
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado = _seleccionado.asStateFlow()

    // Filtro "Cerca de mí" (Activado/Desactivado)
    private val _cercaDeMi = MutableStateFlow(false)
    val cercaDeMi = _cercaDeMi.asStateFlow()

    // Valor del radio ("Cerca de mí": 100, 200, 500 m)
    private val _radioVal = MutableStateFlow(200)
    val radioVal = _radioVal.asStateFlow()

    // Visibilidad del círculo en el mapa ("Mostrar área")
    private val _mostrarRadio = MutableStateFlow(false)
    val mostrarRadio = _mostrarRadio.asStateFlow()

    // Canal de eventos para controlar la cámara del mapa
    private val _accionMapa = MutableStateFlow<AccionMapa>(AccionMapa.Ninguna)
    val accionMapa = _accionMapa.asStateFlow()

    // Estados de edición de ubicaciones TECSUP Lima
    private val _modoEdicion = MutableStateFlow(false)
    val modoEdicion = _modoEdicion.asStateFlow()

    private val _lugarAEditar = MutableStateFlow<Lugar?>(null)
    val lugarAEditar = _lugarAEditar.asStateFlow()

    private val _posicionTemporalEdicion = MutableStateFlow<Posicion?>(null)
    val posicionTemporalEdicion = _posicionTemporalEdicion.asStateFlow()

    // Catálogo general para Explorar y Favoritos (Inmutable y sin filtro de radio)
    val lugaresExplorar: StateFlow<List<LugarConDistancia>> = combine(
        _lugaresConDistancia,
        _modoCampus,
        _categoria,
        _query
    ) { lista, modo, cat, texto ->
        val favs = _favoritos.value
        val recs = _recientes.value
        val pest = _pestanaActiva.value

        lista.filter { it.lugar.modo == modo }.filter { item ->
            val matchPestana = when (pest) {
                "Favoritos" -> favs.contains(item.lugar.id)
                "Recientes" -> recs.contains(item.lugar.id)
                else -> true
            }
            val matchCategoria = cat == "Todos" || item.lugar.categoria == cat
            val textoMin = texto.lowercase().trim()
            val matchTexto = textoMin.isBlank() || when {
                textoMin.contains("estudiar") || textoMin.contains("computadora") || textoMin.contains("laboratorio") ->
                    item.lugar.categoria == "Estudio" || item.lugar.nombre.contains("laboratorio", true) || item.lugar.nombre.contains("biblioteca", true)
                textoMin.contains("comer") || textoMin.contains("hambre") || textoMin.contains("cafeteria") ->
                    item.lugar.categoria == "Comida" || item.lugar.nombre.contains("cafeteria", true)
                textoMin.contains("deporte") || textoMin.contains("futbol") || textoMin.contains("ejercicio") ->
                    item.lugar.nombre.contains("loza", true) || item.lugar.nombre.contains("deporte", true)
                textoMin.contains("atencion") || textoMin.contains("tramite") || textoMin.contains("secretaria") ->
                    item.lugar.nombre.contains("atencion", true) || item.lugar.nombre.contains("servicios", true)
                else ->
                    item.lugar.nombre.contains(textoMin, true) || item.lugar.descripcion.contains(textoMin, true)
            }
            matchPestana && matchCategoria && matchTexto
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Lugares filtrados para el MAPA (Aplica filtro por distancia si "Cerca de mí" está activo y hay GPS válido)
    val lugaresMapa: StateFlow<List<LugarConDistancia>> = combine(
        lugaresExplorar,
        _cercaDeMi,
        _radioVal,
        _estado
    ) { listaExplorar, cerca, rad, est ->
        listaExplorar.filter { item ->
            val matchRadio = !cerca || est !is UbicacionUi.Lista || !item.lugar.ubicada || (item.metros != null && item.metros <= rad.toFloat())
            matchRadio
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> = lugaresExplorar

    val masCercano: StateFlow<Lugar?> = lugaresFiltrados.map { lista ->
        lista.firstOrNull { it.metros != null && it.lugar.ubicada }?.lugar
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun cambiarModo(modo: ModoCampus) {
        _modoCampus.value = modo
        _seleccionado.value = null
        _categoria.value = "Todos"
        _query.value = ""
        _pestanaActiva.value = "Todos"
        _rutaDestino.value = null
        activarModoEdicion(false)

        val centro = if (modo == ModoCampus.TECSUP_LIMA) centroTecsupLima else centroDemoAcademico
        _accionMapa.value = AccionMapa.CentrarCampus(centro)

        val ref = if (_estado.value is UbicacionUi.Lista) (_estado.value as UbicacionUi.Lista).posicion else centro
        actualizarDistancias(ref)
    }

    fun cambiarPestanaNavegacion(index: Int) {
        _pestanaNavegacion.value = index
    }

    fun cambiarPestana(pestana: String) {
        _pestanaActiva.value = pestana
    }

    fun cambiarCategoria(valor: String) {
        _categoria.value = valor
        _seleccionado.value = null
    }

    fun actualizarQuery(texto: String) {
        _query.value = texto
    }

    fun toggleCercaDeMi() {
        _cercaDeMi.value = !_cercaDeMi.value
    }

    fun elegirRadioVal(n: Int) {
        require(n in listOf(100, 200, 500))
        _radioVal.value = n
        _cercaDeMi.value = true
    }

    fun toggleMostrarRadio() {
        _mostrarRadio.value = !_mostrarRadio.value
    }

    fun toggleFavorito(id: Int) {
        if (lugaresRepo is LugaresRepositoryPreferences) {
            _favoritos.value = lugaresRepo.toggleFavorito(id)
        }
    }

    fun agregarReciente(id: Int) {
        if (lugaresRepo is LugaresRepositoryPreferences) {
            lugaresRepo.agregarReciente(id)
            _recientes.value = lugaresRepo.obtenerRecientes()
        }
    }

    fun establecerRuta(lugar: Lugar?) {
        _rutaDestino.value = lugar
        if (lugar != null) {
            agregarReciente(lugar.id)
            _accionMapa.value = AccionMapa.SeleccionarLugar(lugar)
        }
    }

    fun limpiarRuta() {
        _rutaDestino.value = null
    }

    fun activarModoEdicion(activar: Boolean) {
        if (_modoCampus.value == ModoCampus.TECSUP_LIMA) {
            _modoEdicion.value = activar
            if (!activar) {
                _lugarAEditar.value = null
                _posicionTemporalEdicion.value = null
            }
        } else {
            _modoEdicion.value = false
        }
    }

    fun seleccionarLugarParaEditar(lugar: Lugar?) {
        if (_modoEdicion.value) {
            _lugarAEditar.value = lugar
            _posicionTemporalEdicion.value = lugar?.let { if (it.ubicada) Posicion(it.latitud, it.longitud) else centroTecsupLima }
            if (lugar != null) {
                val targetPos = if (lugar.ubicada) Posicion(lugar.latitud, lugar.longitud) else centroTecsupLima
                _accionMapa.value = AccionMapa.CentrarCampus(targetPos)
            }
        }
    }

    fun establecerPosicionTemporal(pos: Posicion) {
        if (_modoEdicion.value && _lugarAEditar.value != null) {
            _posicionTemporalEdicion.value = pos
        }
    }

    fun guardarEdicion() {
        val lugar = _lugarAEditar.value
        val pos = _posicionTemporalEdicion.value
        if (lugar != null && pos != null && lugaresRepo is LugaresRepositoryPreferences) {
            lugaresRepo.guardarUbicacion(lugar.id, pos.latitud, pos.longitud)
            val ref = if (_estado.value is UbicacionUi.Lista) (_estado.value as UbicacionUi.Lista).posicion else centroTecsupLima
            actualizarDistancias(ref)
            _lugarAEditar.value = null
            _posicionTemporalEdicion.value = null
            _modoEdicion.value = false
        }
    }

    fun cancelarEdicion() {
        _lugarAEditar.value = null
        _posicionTemporalEdicion.value = null
        _modoEdicion.value = false
    }

    fun restaurarUbicaciones() {
        if (lugaresRepo is LugaresRepositoryPreferences) {
            lugaresRepo.restaurarPredeterminadas()
            val ref = if (_estado.value is UbicacionUi.Lista) (_estado.value as UbicacionUi.Lista).posicion else centroTecsupLima
            actualizarDistancias(ref)
            _lugarAEditar.value = null
            _posicionTemporalEdicion.value = null
            _modoEdicion.value = false
        }
    }

    fun exportarJsonCoordenadas(): String {
        return if (lugaresRepo is LugaresRepositoryPreferences) {
            lugaresRepo.exportarJson()
        } else "[]"
    }

    // Solicita la ubicación GPS real del dispositivo
    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error("Este dispositivo no tiene Google Play Services", TipoError.SIN_PLAY_SERVICES)
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    Log.d("CampusGPS_VM", "GPS obtenido: Lat=${posicion.latitud}, Lng=${posicion.longitud}")
                    _estado.value = UbicacionUi.Lista(posicion)
                    actualizarDistancias(posicion)
                    _accionMapa.value = AccionMapa.CentrarUbicacion(posicion)
                }
                .onFailure { error ->
                    Log.e("CampusGPS_VM", "Error obteniendo GPS", error)
                    _estado.value = UbicacionUi.Error(
                        if (error is SecurityException) "Falta el permiso de ubicación" else "Activa el GPS e intenta de nuevo",
                        if (error is SecurityException) TipoError.SIN_PERMISO else TipoError.GPS_APAGADO
                    )
                }
        }
    }

    fun verCampus() {
        val centro = if (_modoCampus.value == ModoCampus.TECSUP_LIMA) centroTecsupLima else centroDemoAcademico
        _accionMapa.value = AccionMapa.CentrarCampus(centro)
    }

    fun ajustarVista() {
        val actuales = lugaresFiltrados.value.filter { it.lugar.ubicada }.map { Posicion(it.lugar.latitud, it.lugar.longitud) }
        if (actuales.isNotEmpty()) {
            _accionMapa.value = AccionMapa.AjustarVista(actuales)
        }
    }

    private fun actualizarDistancias(referencia: Posicion) {
        val nuevos = lugaresRepo.obtenerLugares().map { lugar ->
            val dist = if (lugar.modo == ModoCampus.TECSUP_LIMA && !lugar.ubicada) {
                null
            } else {
                distanciaMetros(referencia, lugar)
            }
            LugarConDistancia(lugar, dist)
        }.sortedBy { it.metros ?: Float.MAX_VALUE }
        _lugaresConDistancia.value = nuevos
    }

    fun seleccionar(id: Int?) {
        if (_modoEdicion.value) {
            val lugar = _lugaresConDistancia.value.firstOrNull { it.lugar.id == id }?.lugar
            seleccionarLugarParaEditar(lugar)
            return
        }

        _seleccionado.value = id
        if (id != null) {
            val lugar = _lugaresConDistancia.value.firstOrNull { it.lugar.id == id }?.lugar
            if (lugar != null) {
                agregarReciente(id)
                _accionMapa.value = AccionMapa.SeleccionarLugar(lugar)
            }
        }
    }

    fun limpiarAccionMapa() {
        _accionMapa.value = AccionMapa.Ninguna
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás", TipoError.SIN_PERMISO)
    }
}
