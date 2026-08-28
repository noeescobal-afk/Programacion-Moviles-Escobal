package com.escobal.poo

class Carrito {

    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun cantidadProductos(): Int {
        return productos.size
    }

    fun obtenerProductos(): List<Producto> {
        return productos.toList()
    }
}