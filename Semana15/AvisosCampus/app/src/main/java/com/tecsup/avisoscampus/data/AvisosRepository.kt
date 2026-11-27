package com.tecsup.avisoscampus.data

import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}

class AvisosRepositoryMemoria : AvisosRepository {
    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos = _avisos.asStateFlow()
    private var siguiente = 1

    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val aviso = Aviso(siguiente++, titulo, texto, origen, hora)
        _avisos.value = listOf(aviso) + _avisos.value
        return aviso
    }
}
