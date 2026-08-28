# Prompt utilizado con Inteligencia Artificial

Actúa como asistente de programación especializado en Kotlin y Programación Orientada a Objetos.

Estoy desarrollando un proyecto académico de un carrito de compras en Kotlin. Ya tengo una primera versión básica desarrollada con funciones y colecciones, pero necesito crear una segunda versión orientada a objetos.

El proyecto debe demostrar claramente los cuatro pilares de la Programación Orientada a Objetos:

1. Abstracción mediante una clase abstracta `Producto`.
2. Herencia mediante las clases `ProductoFisico` y `ProductoDigital`.
3. Encapsulamiento mediante una clase `Carrito` que mantenga privada su colección de productos.
4. Polimorfismo utilizando objetos del tipo general `Producto`, pero con diferentes implementaciones de `calcularPrecioFinal()`.

La clase `Producto` debe contener:

- Nombre del producto.
- Precio base.
- Método abstracto `calcularPrecioFinal()`.
- Método abstracto `descripcion()`.

La clase `ProductoFisico` debe heredar de `Producto` y calcular su precio final sumando un costo de envío.

La clase `ProductoDigital` debe heredar de `Producto` y calcular su precio final aplicando un descuento al precio base.

La clase `Carrito` debe:

- Mantener privada la lista de productos.
- Permitir agregar productos.
- Permitir obtener los productos.
- Permitir conocer la cantidad de productos.
- Calcular el total del carrito.

Finalmente, crea una función `main()` que agregue productos físicos y digitales al mismo carrito y muestre:

- Descripción de cada producto.
- Precio final de cada producto.
- Cantidad total de productos.
- Total del carrito.

Utiliza Kotlin sencillo, nombres claros y código organizado que pueda explicar durante una sustentación académica.
