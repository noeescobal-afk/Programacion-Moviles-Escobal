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
    fun buscar() {
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch { ubicacionRepo.obtenerPosicion().onSuccess { _estado.value = UbicacionUi.Lista(it); actualizarDistancias(it) }
          .onFailure { _estado.value = UbicacionUi.Error(it.message ?: "No se pudo obtener la ubicación") } }
    }
    // El permiso negado se refleja en UI sin cerrar la actividad.
    // Calcula distancias fuera de Compose y ordena por proximidad.
    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares().map { LugarConDistancia(it, distanciaMetros(posicion,it)) }.sortedBy { it.metros }
        Log.d("CampusGPS", "Distancias: ${_lugares.value}")
    }
    // El permiso negado se refleja en UI sin cerrar la actividad.
    fun permisoDenegado() { _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás") }
}
