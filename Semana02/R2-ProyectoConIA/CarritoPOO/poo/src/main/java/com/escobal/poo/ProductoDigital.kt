package com.escobal.poo

class ProductoDigital(
    nombre: String,
    precioBase: Double,
    private val descuento: Double
) : Producto(nombre, precioBase) {

    override fun calcularPrecioFinal(): Double {
        return precioBase * (1 - descuento)
    }

    override fun descripcion(): String {
        return "$nombre - Producto digital"
    }
}