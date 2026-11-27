package com.tecsup.avisoscampus.model

enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión"),
    CARGADOR("Cargador")
}

enum class ModoTema(val texto: String) {
    AUTOMATICO("Automático"),
    CLARO("Claro"),
    OSCURO("Oscuro")
}

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String
)

sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}
