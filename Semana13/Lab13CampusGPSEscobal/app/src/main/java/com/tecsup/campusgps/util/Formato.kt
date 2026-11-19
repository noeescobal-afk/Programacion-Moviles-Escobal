package com.tecsup.campusgps.util

// ARCHIVO: Formato.kt
// Utilidad para presentar distancias en metros o kilómetros.
import java.util.Locale
fun formatearDistancia(metros: Float): String = if(metros<1000f) "${metros.toInt()} m" else String.format(Locale.US,"%.1f km", metros/1000f)
