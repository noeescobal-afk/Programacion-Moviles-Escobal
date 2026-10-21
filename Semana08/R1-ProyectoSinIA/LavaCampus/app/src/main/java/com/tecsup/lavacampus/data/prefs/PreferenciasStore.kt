package com.tecsup.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_lavacampus")

class PreferenciasStore(
    context: Context
) {
    private val appContext = context.applicationContext

    companion object {
        private val KEY_MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        private val KEY_ROL = stringPreferencesKey("rol_usuario")
        private val KEY_AVISOS = booleanPreferencesKey("avisos")
        private val KEY_RECIBIR_PEDIDOS = booleanPreferencesKey("recibir_pedidos")
    }

    val modoOscuro: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[KEY_MODO_OSCURO] ?: false
        }

    suspend fun setModoOscuro(activo: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_MODO_OSCURO] = activo
        }
    }

    val rolSeleccionado: Flow<Rol?> = appContext.dataStore.data
        .map { preferences ->
            preferences[KEY_ROL]?.let { rolName ->
                try {
                    Rol.valueOf(rolName)
                } catch (e: Exception) {
                    null
                }
            }
        }

    suspend fun setRolSeleccionado(rol: Rol?) {
        appContext.dataStore.edit { preferences ->
            if (rol != null) {
                preferences[KEY_ROL] = rol.name
            } else {
                preferences.remove(KEY_ROL)
            }
        }
    }

    suspend fun limpiarRol() {
        setRolSeleccionado(null)
    }

    val avisos: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[KEY_AVISOS] ?: true
        }

    suspend fun setAvisos(activo: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_AVISOS] = activo
        }
    }

    val recibirPedidos: Flow<Boolean> = appContext.dataStore.data
        .map { preferences ->
            preferences[KEY_RECIBIR_PEDIDOS] ?: true
        }

    suspend fun setRecibirPedidos(activo: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_RECIBIR_PEDIDOS] = activo
        }
    }
}
