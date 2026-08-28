package com.escobal.poo

abstract class Producto(
    val nombre: String,
    val precioBase: Double
) {

    abstract fun calcularPrecioFinal(): Double

    abstract fun descripcion(): String
}