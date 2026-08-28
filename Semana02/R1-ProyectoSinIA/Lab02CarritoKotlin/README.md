# Laboratorio 02 - Carrito de Compras en Kotlin

## Datos del estudiante

- **Estudiante:** Noe Oswaldo Escobal
- **Curso:** Programación en Móviles
- **Institución:** TECSUP
- **Laboratorio:** 02
- **Proyecto:** R1 - Proyecto sin IA

## Descripción

Este proyecto implementa la lógica de un carrito de compras utilizando Kotlin.

El programa permite registrar productos con su nombre, precio y cantidad, calcular el subtotal de la compra, el IGV del 18%, el total a pagar, identificar el producto más caro y aplicar descuentos dependiendo del monto total de la compra.

También se utiliza formato de salida para mostrar los productos y montos de manera ordenada en consola.

## Conceptos utilizados

Durante el laboratorio se utilizaron los siguientes conceptos de Kotlin:

- `val` y `var`
- `String`
- `Int`
- `Double`
- `data class`
- `MutableList`
- `mutableListOf()`
- `fun`
- `for`
- `if`
- `when`
- `return`
- Plantillas de String
- `String.format()`
- `maxByOrNull()`
- `find()`

## Modelo de datos

Se utilizó una `data class` para representar cada producto:

```kotlin
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
