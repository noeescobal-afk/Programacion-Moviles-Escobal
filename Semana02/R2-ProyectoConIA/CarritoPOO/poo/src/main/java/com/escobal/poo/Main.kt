package com.escobal.poo

fun main() {

    println("======================================")
    println(" CARRITO DE COMPRAS - VERSION POO ")
    println("======================================")

    val carrito = Carrito()

    val laptop: Producto = ProductoFisico(
        "Laptop HP",
        2500.0,
        30.0
    )

    val cursoKotlin: Producto = ProductoDigital(
        "Curso de Kotlin",
        200.0,
        0.20
    )

    val audifonos: Producto = ProductoFisico(
        "Audifonos Sony",
        120.0,
        15.0
    )

    carrito.agregarProducto(laptop)
    carrito.agregarProducto(cursoKotlin)
    carrito.agregarProducto(audifonos)

    println()
    println("---------- PRODUCTOS ----------")

    for (producto in carrito.obtenerProductos()) {

        println(producto.descripcion())

        println(
            String.format(
                "Precio final: S/ %.2f",
                producto.calcularPrecioFinal()
            )
        )

        println()
    }

    println("--------------------------------")
    println(
        "Cantidad de productos: ${carrito.cantidadProductos()}"
    )

    println(
        String.format(
            "TOTAL DEL CARRITO: S/ %.2f",
            carrito.calcularTotal()
        )
    )
}