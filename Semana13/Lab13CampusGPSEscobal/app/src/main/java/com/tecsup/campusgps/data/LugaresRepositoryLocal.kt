package com.tecsup.campusgps.data

// ARCHIVO: LugaresRepositoryLocal.kt
// Repositorio local que provee los lugares para TECSUP Lima y el Modo Demo Académico.
// Incluye los nuevos 6 puntos de interés en el modo TECSUP Lima configurados inicialmente como pendientes de ubicación (ubicada = false).

import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.ModoCampus
import com.tecsup.campusgps.model.TipoLugar

class LugaresRepositoryLocal : LugaresRepository {
    override fun obtenerLugares() = listOf(
        // ==========================================
        // A. TECSUP LIMA (Modo Principal - Santa Anita)
        // Coordenada base del campus: -12.0453, -76.9533
        // ==========================================
        Lugar(
            id = 201,
            nombre = "Biblioteca",
            categoria = "Estudio",
            latitud = -12.0451,
            longitud = -76.9537,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector izquierdo, zona media-superior [Aprox. según plano]",
        ),
        Lugar(
            id = 202,
            nombre = "Servicios Educativos",
            categoria = "Servicios",
            latitud = -12.0455,
            longitud = -76.9536,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector izquierdo, debajo de la biblioteca [Aprox. según plano]",
        ),
        Lugar(
            id = 203,
            nombre = "Auditorio",
            categoria = "Eventos",
            latitud = -12.0449,
            longitud = -76.9533,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector superior-central [Aprox. según plano]",
        ),
        Lugar(
            id = 204,
            nombre = "Loza de deportes",
            categoria = "Servicios",
            latitud = -12.0450,
            longitud = -76.9528,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector superior-derecho [Aprox. según plano]",
        ),
        Lugar(
            id = 205,
            nombre = "Cafetería",
            categoria = "Comida",
            latitud = -12.0457,
            longitud = -76.9533,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector inferior-central [Aprox. según plano]",
        ),
        Lugar(
            id = 206,
            nombre = "Oficina de Atención",
            categoria = "Servicios",
            latitud = -12.0457,
            longitud = -76.9536,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector inferior-izquierdo, cerca de cafetería [Aprox. según plano]",
        ),
        Lugar(
            id = 207,
            nombre = "Estacionamiento",
            categoria = "Servicios",
            latitud = -12.0456,
            longitud = -76.9530,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Sector inferior-derecho, zona de acceso [Aprox. según plano]",
        ),
        Lugar(
            id = 208,
            nombre = "Laboratorios",
            categoria = "Estudio",
            latitud = -12.0452,
            longitud = -76.9534,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Laboratorios especializados [Verificado]",
        ),

        // Nuevos 6 puntos de interés solicitados (Pendientes de ubicación inicial)
        Lugar(
            id = 209,
            nombre = "Laboratorio 2",
            categoria = "Estudio",
            latitud = 0.0,
            longitud = 0.0,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Pendiente de ubicación",
            ubicada = false,
        ),
        Lugar(
            id = 210,
            nombre = "Laboratorio 3",
            categoria = "Estudio",
            latitud = 0.0,
            longitud = 0.0,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Pendiente de ubicación",
            ubicada = false,
        ),
        Lugar(
            id = 211,
            nombre = "Cafetería 2",
            categoria = "Comida",
            latitud = 0.0,
            longitud = 0.0,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Pendiente de ubicación",
            ubicada = false,
        ),
        Lugar(
            id = 212,
            nombre = "Aula Teórica 1",
            categoria = "Estudio",
            latitud = 0.0,
            longitud = 0.0,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Pendiente de ubicación",
            ubicada = false,
        ),
        Lugar(
            id = 213,
            nombre = "Aula Teórica 2",
            categoria = "Estudio",
            latitud = 0.0,
            longitud = 0.0,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Pendiente de ubicación",
            ubicada = false,
        ),
        Lugar(
            id = 214,
            nombre = "Aula Teórica 3",
            categoria = "Estudio",
            latitud = 0.0,
            longitud = 0.0,
            modo = ModoCampus.TECSUP_LIMA,
            tipo = TipoLugar.TECSUP_REAL,
            descripcion = "Pendiente de ubicación",
            ubicada = false,
        ),

        // ==========================================
        // B. DEMO CAMPUS (Modo Académico - Lab 13)
        // Punto base académico: -12.0464, -77.0428
        // Coordenadas exactas y originales de la guía:
        // ==========================================
        Lugar(
            id = 1,
            nombre = "Biblioteca Central",
            categoria = "Estudio",
            latitud = -12.0460,
            longitud = -77.0435,
            modo = ModoCampus.DEMO_ACADEMICO,
            tipo = TipoLugar.DEMO_ACADEMICO,
            descripcion = "Biblioteca Central - Guía Académica Lab 13",
        ),
        Lugar(
            id = 2,
            nombre = "Cafetería Campus",
            categoria = "Comida",
            latitud = -12.0470,
            longitud = -77.0420,
            modo = ModoCampus.DEMO_ACADEMICO,
            tipo = TipoLugar.DEMO_ACADEMICO,
            descripcion = "Cafetería Campus - Guía Académica Lab 13",
        ),
        Lugar(
            id = 3,
            nombre = "Laboratorio de Cómputo",
            categoria = "Estudio",
            latitud = -12.0455,
            longitud = -77.0415,
            modo = ModoCampus.DEMO_ACADEMICO,
            tipo = TipoLugar.DEMO_ACADEMICO,
            descripcion = "Laboratorio de Cómputo - Guía Académica Lab 13",
        ),
        Lugar(
            id = 4,
            nombre = "Auditorio Principal",
            categoria = "Eventos",
            latitud = -12.0480,
            longitud = -77.0440,
            modo = ModoCampus.DEMO_ACADEMICO,
            tipo = TipoLugar.DEMO_ACADEMICO,
            descripcion = "Auditorio Principal - Guía Académica Lab 13",
        ),
        Lugar(
            id = 5,
            nombre = "Estacionamiento Norte",
            categoria = "Servicios",
            latitud = -12.0440,
            longitud = -77.0430,
            modo = ModoCampus.DEMO_ACADEMICO,
            tipo = TipoLugar.DEMO_ACADEMICO,
            descripcion = "Estacionamiento Norte - Guía Académica Lab 13",
        ),
    )
}
