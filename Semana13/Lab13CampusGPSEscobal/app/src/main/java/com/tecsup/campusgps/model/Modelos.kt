package com.tecsup.campusgps.model

// ARCHIVO: Modelos.kt
// Modelos del dominio y estados de pantalla para CampusGPS (TECSUP Lima & Demo Académico).

data class Posicion(val latitud: Double, val longitud: Double)

enum class ModoCampus { TECSUP_LIMA, DEMO_ACADEMICO }

enum class TipoLugar { TECSUP_REAL, DEMO_ACADEMICO }

data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double,
    val modo: ModoCampus,
    val tipo: TipoLugar,
    val descripcion: String = "",
    val ubicada: Boolean = true
)

data class LugarConDistancia(
    val lugar: Lugar,
    val metros: Float?
)

enum class TipoError { SIN_PERMISO, GPS_APAGADO, SIN_PLAY_SERVICES, OTRO }

sealed interface UbicacionUi {
    data object Inicial : UbicacionUi
    data object Buscando : UbicacionUi
    data class Lista(val posicion: Posicion) : UbicacionUi
    data class Error(val mensaje: String, val tipo: TipoError) : UbicacionUi
}

// Acciones de control de cámara hacia el mapa
sealed interface AccionMapa {
    data object Ninguna : AccionMapa
    data class CentrarUbicacion(val posicion: Posicion) : AccionMapa
    data class CentrarCampus(val centro: Posicion) : AccionMapa
    data class SeleccionarLugar(val lugar: Lugar) : AccionMapa
    data class AjustarVista(val posiciones: List<Posicion>) : AccionMapa
}
