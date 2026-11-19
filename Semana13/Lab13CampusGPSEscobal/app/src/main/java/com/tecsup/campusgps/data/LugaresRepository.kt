package com.tecsup.campusgps.data

// ARCHIVO: LugaresRepository.kt
// Contrato para obtener los lugares ficticios del campus.
import com.tecsup.campusgps.model.Lugar
interface LugaresRepository { fun obtenerLugares(): List<Lugar> }
