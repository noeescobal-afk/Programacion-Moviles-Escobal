\# Laboratorio 02 - Carrito de Compras en Kotlin



\## Datos del estudiante



\- \*\*Estudiante:\*\* Noe Oswaldo Escobal

\- \*\*Curso:\*\* Programación en Móviles

\- \*\*Institución:\*\* TECSUP

\- \*\*Laboratorio:\*\* 02

\- \*\*Proyecto:\*\* R1 - Proyecto sin IA



\## Descripción



Este proyecto implementa un carrito de compras en Kotlin utilizando variables, funciones, colecciones y estructuras de decisión.



El programa permite registrar productos, calcular el subtotal, el IGV del 18%, el total de la compra, identificar el producto más caro y aplicar un descuento dependiendo del monto total.



\## Conceptos utilizados



\- `val` y `var`

\- `String`, `Int` y `Double`

\- `data class`

\- `MutableList`

\- `fun`

\- `for`

\- `when`

\- `return`

\- `String.format`

\- `maxByOrNull`



\## Funciones implementadas



\- `calcularSubtotal()`: calcula el subtotal de todos los productos.

\- `calcularIGV()`: calcula el 18% del subtotal.

\- `calcularTotal()`: obtiene el total incluyendo IGV.

\- `mostrarDetalle()`: muestra el detalle del carrito con columnas alineadas.

\- `calcularDescuento()`: aplica un descuento según el monto total.

\- `buscarProducto()`: permite buscar un producto por nombre.



\## Diferencia entre val y var



`val` se utiliza para valores que no deben ser reasignados después de su creación.



`var` permite modificar su valor posteriormente.



En la clase `Producto`:



```kotlin

data class Producto(

&#x20;   val nombre: String,

&#x20;   val precio: Double,

&#x20;   var cantidad: Int

)

