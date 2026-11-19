package com.tecsup.campusgps.viewmodel

// ARCHIVO: CampusViewModelFactory.kt
// Inyección manual de los repositorios al crear el ViewModel.
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.campusgps.data.*
class CampusViewModelFactory(private val contexto: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T = MapaViewModel(
        UbicacionRepositoryGoogle(contexto.applicationContext), LugaresRepositoryLocal()
    ) as T
}
