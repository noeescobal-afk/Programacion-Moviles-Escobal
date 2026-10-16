package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _rolActual = MutableStateFlow(Rol.ESTUDIANTE)
    val rolActual: StateFlow<Rol> = _rolActual.asStateFlow()

    fun cambiarRol(nuevoRol: Rol) {
        _rolActual.value = nuevoRol
    }
}
