package com.tecsup.campusgps.data

// ARCHIVO: UbicacionRepository.kt
// Contrato de acceso a ubicación; evita acoplar el ViewModel con Google Play Services.
import com.tecsup.campusgps.model.Posicion
interface UbicacionRepository { suspend fun obtenerPosicion(): Result<Posicion> }
