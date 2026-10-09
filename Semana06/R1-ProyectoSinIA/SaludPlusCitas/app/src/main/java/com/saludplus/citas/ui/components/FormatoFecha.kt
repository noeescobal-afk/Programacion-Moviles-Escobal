package com.saludplus.citas.ui.components

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatearFechaEspanol(fecha: String): String {
    return try {
        val formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaLocalDate = LocalDate.parse(fecha, formatoEntrada)
        val localeEs = Locale.forLanguageTag("es-ES")

        val diaSemana = fechaLocalDate
            .format(DateTimeFormatter.ofPattern("EEEE", localeEs))
            .replaceFirstChar { it.uppercase() }

        val mes = fechaLocalDate
            .format(DateTimeFormatter.ofPattern("MMMM", localeEs))
            .replace("septiembre", "setiembre")

        "$diaSemana ${fechaLocalDate.dayOfMonth} de $mes ${fechaLocalDate.year}"
    } catch (_: Exception) {
        fecha
    }
}