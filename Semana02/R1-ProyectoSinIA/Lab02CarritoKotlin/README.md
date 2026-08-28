# Laboratorio 02 - Carrito de Compras en Kotlin

## Datos del estudiante

- **Estudiante:** Noe Oswaldo Escobal
- **Curso:** Programación en Móviles
- **Institución:** TECSUP
- **Laboratorio:** 02
- **Proyecto:** R1 - Proyecto sin IA

## Descripción

Este proyecto implementa la lógica de un carrito de compras utilizando Kotlin.

El programa permite registrar productos con su nombre, precio y cantidad, calcular el subtotal, el IGV del 18%, el total de la compra, identificar el producto más caro y aplicar un descuento según el monto total.

## Conceptos utilizados

- `val` y `var`
- `String`, `Int` y `Double`
- `data class`
- `MutableList`
- `mutableListOf()`
- `fun`
- `for`
- `if`
- `when`
- `return`
- `String.format()`
- `maxByOrNull()`
- `find()`

## Modelo de datos

El producto se representa mediante una `data class`:

```kotlin
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)