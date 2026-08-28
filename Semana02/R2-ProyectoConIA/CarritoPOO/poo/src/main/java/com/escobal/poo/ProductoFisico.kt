package com.escobal.poo

class ProductoFisico(
    nombre: String,
    precioBase: Double,
    private val costoEnvio: Double
) : Producto(nombre, precioBase) {

    override fun calcularPrecioFinal(): Double {
        return precioBase + costoEnvio
    }

    override fun descripcion(): String {
        return "$nombre - Producto fisico"
    }
}