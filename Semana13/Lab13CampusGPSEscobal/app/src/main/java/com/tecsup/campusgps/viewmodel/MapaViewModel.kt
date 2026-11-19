package com.tecsup.campusgps.viewmodel

// ARCHIVO: MapaViewModel.kt
// Coordina los repositorios, publica StateFlow y procesa eventos sin depender de Android Context.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.*
import com.tecsup.campusgps.model.*
import com.tecsup.campusgps.util.distanciaMetros
import android.util.Log
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class MapaViewModel(private val ubicacionRepo: UbicacionRepository, private val lugaresRepo: LugaresRepository): ViewModel() {
    // Estado mutable privado; se expone únicamente como flujo de lectura.
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()
    private val _lugares = MutableStateFlow(lugaresRepo.obtenerLugares().map { LugarConDistancia(it,null) })
    val lugares = _lugares.asStateFlow()
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado = _seleccionado.asStateFlow()
    // Solicita una ubicación y publica estados observables.
    // Solicita una ubicación y publica estados observables.
    private val _categoria = MutableStateFlow("Todos")
    val categoria = _categoria.asStateFlow()
    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> = combine(_lugares,_categoria) { lista,cat ->
        if(cat=="Todos") lista else lista.filter { it.lugar.categoria==cat }
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),_lugares.value)
    val masCercano: StateFlow<Lugar?> = lugaresFiltrados.map { lista -> lista.firstOrNull { it.metros!=null }?.lugar }
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
    // Filtro derivado en el ViewModel para mantener la UI sin lógica de negocio.
    fun cambiarCategoria(valor: String) { _categoria.value=valor; _seleccionado.value=null }
    // Solicita una ubicación y publica estados observables.
    fun buscar() {
        if(!ubicacionRepo.playServicesDisponible()) {
            _estado.value=UbicacionUi.Error("Este dispositivo no tiene Google Play Services",TipoError.SIN_PLAY_SERVICES)
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch { ubicacionRepo.obtenerPosicion().onSuccess { _estado.value = UbicacionUi.Lista(it); actualizarDistancias(it) }
          .onFailure { _estado.value = UbicacionUi.Error(if(it is SecurityException) "Falta el permiso de ubicación" else "Activa el GPS e intenta de nuevo", if(it is SecurityException) TipoError.SIN_PERMISO else TipoError.GPS_APAGADO) } }
    }
    // El permiso negado se refleja en UI sin cerrar la actividad.
    // Calcula distancias fuera de Compose y ordena por proximidad.
    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares().map { LugarConDistancia(it, distanciaMetros(posicion,it)) }.sortedBy { it.metros }
        Log.d("CampusGPS", "Distancias: ${_lugares.value}")
    }
    // El permiso negado se refleja en UI sin cerrar la actividad.
    // La selección se comparte con el mapa y la lista de sitios.
    fun seleccionar(id: Int?) { _seleccionado.value=id }
    // El permiso negado se refleja en UI sin cerrar la actividad.
    fun permisoDenegado() { _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás",TipoError.SIN_PERMISO) }
}
