\# R2 - Carrito de Compras con POO e IA



\## Datos del proyecto



\- \*\*Curso:\*\* Programación en Móviles

\- \*\*Semana:\*\* 02

\- \*\*Proyecto:\*\* R2 - Proyecto con Inteligencia Artificial

\- \*\*Lenguaje:\*\* Kotlin

\- \*\*Enfoque:\*\* Programación Orientada a Objetos



\## Descripción



Este proyecto implementa una segunda versión de un carrito de compras en Kotlin utilizando Programación Orientada a Objetos y apoyo de Inteligencia Artificial.



A diferencia del proyecto R1, esta versión organiza la solución mediante clases y aplica los cuatro pilares principales de la Programación Orientada a Objetos: abstracción, herencia, encapsulamiento y polimorfismo.



\## Abstracción



La abstracción se implementa mediante la clase abstracta `Producto`.



```kotlin

abstract class Producto(

&#x20;   val nombre: String,

&#x20;   val precioBase: Double

) {

&#x20;   abstract fun calcularPrecioFinal(): Double

&#x20;   abstract fun descripcion(): String

}

```



Esta clase define las características y operaciones comunes que deben tener todos los productos.



\## Herencia



Las clases `ProductoFisico` y `ProductoDigital` heredan de la clase `Producto`.



Ejemplo:



```kotlin

class ProductoFisico(...) : Producto(nombre, precioBase)

```



```kotlin

class ProductoDigital(...) : Producto(nombre, precioBase)

```



Cada clase hija implementa los métodos definidos por la clase abstracta utilizando `override`.



\## Encapsulamiento



El encapsulamiento se aplica en la clase `Carrito`.



```kotlin

private val productos = mutableListOf<Producto>()

```



La lista de productos es privada, por lo que no puede modificarse directamente desde fuera de la clase.



Para interactuar con ella se utilizan métodos públicos como:



\- `agregarProducto()`

\- `cantidadProductos()`

\- `obtenerProductos()`

\- `calcularTotal()`



\## Polimorfismo



El polimorfismo permite trabajar con diferentes tipos de productos utilizando el tipo general `Producto`.



Ejemplo:



```kotlin

val laptop: Producto = ProductoFisico(

&#x20;   "Laptop HP",

&#x20;   2500.0,

&#x20;   30.0

)



val cursoKotlin: Producto = ProductoDigital(

&#x20;   "Curso de Kotlin",

&#x20;   200.0,

&#x20;   0.20

)

```



Aunque ambos objetos son tratados como `Producto`, cada uno ejecuta su propia versión de:



```kotlin

calcularPrecioFinal()

```



El producto físico suma un costo de envío, mientras que el producto digital aplica un descuento.



\## Clases desarrolladas



\- `Producto`

\- `ProductoFisico`

\- `ProductoDigital`

\- `Carrito`

\- `Main`



\## Resultado del programa



El programa agrega productos físicos y digitales al mismo carrito.



Los resultados obtenidos son:



\- Laptop HP: S/ 2530.00

\- Curso de Kotlin: S/ 160.00

\- Audifonos Sony: S/ 135.00

\- Cantidad de productos: 3

\- Total del carrito: S/ 2825.00



\## Uso de Inteligencia Artificial



La Inteligencia Artificial fue utilizada como apoyo para diseñar y estructurar la versión orientada a objetos del proyecto.



La estructura utilizada para formular el prompt se encuentra en:



`PROMPT\_ESTRUCTURA.md`



El prompt utilizado se encuentra en:



`PROMPT\_USADO.md`



\## Evidencias



\### Ejecución final



!\[Resultado final R2](evidencias/resultado-final-r2.png)



\### Estructura del proyecto



!\[Estructura final R2](evidencias/Estructura-final-r2.png)

