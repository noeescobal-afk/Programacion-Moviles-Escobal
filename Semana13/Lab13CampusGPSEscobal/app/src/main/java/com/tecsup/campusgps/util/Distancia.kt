package com.tecsup.campusgps.util

// ARCHIVO: Distancia.kt
// Utilidad para obtener metros entre dos coordenadas geográficas.
import android.location.Location
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.Posicion
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    // La API de Android escribe la distancia calculada en el primer índice.
    Location.distanceBetween(desde.latitud,desde.longitud,hasta.latitud,hasta.longitud,resultado)
    return resultado[0]
}
