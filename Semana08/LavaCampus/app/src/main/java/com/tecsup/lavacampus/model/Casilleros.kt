package com.tecsup.lavacampus.model

import java.util.Locale

object Casilleros {
    val LISTA: List<String> = (1..30).map { String.format(Locale.US, "C-%02d", it) }
    const val TOTAL = 30
}
