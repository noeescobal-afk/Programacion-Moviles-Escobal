package com.tecsup.lavacampus.data.prefs

import com.tecsup.lavacampus.model.Rol

data class UserPreferences(
    val usuarioId: String = "",
    val rol: Rol = Rol.ESTUDIANTE
)
