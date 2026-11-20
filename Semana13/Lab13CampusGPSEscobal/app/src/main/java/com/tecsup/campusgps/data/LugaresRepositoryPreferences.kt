package com.tecsup.campusgps.data

// ARCHIVO: LugaresRepositoryPreferences.kt
// Decorador de LugaresRepository que persiste las coordenadas editadas, favoritos y recientes usando SharedPreferences.

import android.content.Context
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.ModoCampus

class LugaresRepositoryPreferences(
    context: Context,
    private val localRepo: LugaresRepository
) : LugaresRepository {

    private val prefs = context.applicationContext.getSharedPreferences("tecsup_ubicaciones_prefs", Context.MODE_PRIVATE)

    override fun obtenerLugares(): List<Lugar> {
        val base = localRepo.obtenerLugares()
        return base.map { lugar ->
            if (lugar.modo == ModoCampus.TECSUP_LIMA) {
                val latKey = "lat_${lugar.id}"
                val lngKey = "lng_${lugar.id}"
                val ubicadaKey = "ubicada_${lugar.id}"
                
                if (prefs.contains(latKey) && prefs.contains(lngKey)) {
                    lugar.copy(
                        latitud = prefs.getFloat(latKey, lugar.latitud.toFloat()).toDouble(),
                        longitud = prefs.getFloat(lngKey, lugar.longitud.toFloat()).toDouble(),
                        ubicada = prefs.getBoolean(ubicadaKey, true)
                    )
                } else lugar
            } else lugar
        }
    }

    fun guardarUbicacion(id: Int, lat: Double, lng: Double) {
        prefs.edit()
            .putFloat("lat_$id", lat.toFloat())
            .putFloat("lng_$id", lng.toFloat())
            .putBoolean("ubicada_$id", true)
            .apply()
    }

    fun restaurarPredeterminadas() {
        val editor = prefs.edit()
        localRepo.obtenerLugares()
            .filter { it.modo == ModoCampus.TECSUP_LIMA }
            .forEach { lugar ->
                editor.remove("lat_${lugar.id}")
                editor.remove("lng_${lugar.id}")
                editor.remove("ubicada_${lugar.id}")
            }
        editor.apply()
    }

    fun exportarJson(): String {
        val lugaresTecsup = obtenerLugares().filter { it.modo == ModoCampus.TECSUP_LIMA && it.ubicada }
        val sb = StringBuilder()
        sb.append("[\n")
        lugaresTecsup.forEachIndexed { index, l ->
            sb.append("  {\n")
            sb.append("    \"id\": ${l.id},\n")
            sb.append("    \"nombre\": \"${l.nombre}\",\n")
            sb.append("    \"categoria\": \"${l.categoria}\",\n")
            sb.append("    \"latitud\": ${l.latitud},\n")
            sb.append("    \"longitud\": ${l.longitud}\n")
            sb.append("  }${if (index < lugaresTecsup.size - 1) "," else ""}\n")
        }
        sb.append("]")
        return sb.toString()
    }

    fun obtenerFavoritos(): Set<Int> {
        return prefs.getStringSet("favoritos", emptySet())?.map { it.toInt() }?.toSet() ?: emptySet()
    }

    fun toggleFavorito(id: Int): Set<Int> {
        val actuales = obtenerFavoritos().toMutableSet()
        if (actuales.contains(id)) actuales.remove(id) else actuales.add(id)
        prefs.edit().putStringSet("favoritos", actuales.map { it.toString() }.toSet()).apply()
        return actuales
    }

    fun obtenerRecientes(): List<Int> {
        val str = prefs.getString("recientes", "") ?: ""
        if (str.isEmpty()) return emptyList()
        return str.split(",").mapNotNull { it.toIntOrNull() }
    }

    fun agregarReciente(id: Int) {
        val lista = obtenerRecientes().toMutableList()
        lista.remove(id)
        lista.add(0, id)
        if (lista.size > 10) lista.removeAt(lista.size - 1)
        prefs.edit().putString("recientes", lista.joinToString(",")).apply()
    }
}
