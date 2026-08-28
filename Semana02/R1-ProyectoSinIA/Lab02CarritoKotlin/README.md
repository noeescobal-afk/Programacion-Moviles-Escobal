```

## Diferencia entre val y var

En Kotlin, `val` se utiliza cuando una referencia no debe ser reasignada después de inicializarse.

`var` permite que su valor pueda cambiar posteriormente.

En la clase `Producto`, `nombre` y `precio` utilizan `val` porque no deberían cambiar después de crear el producto.

La propiedad `cantidad` utiliza `var` porque la cantidad de unidades sí puede aumentar o disminuir.

Si intentamos modificar el precio después de crear un producto, Kotlin mostrará un error porque `precio` fue declarado como `val`.

## Funciones implementadas

- `calcularSubtotal()`: calcula la suma de precio por cantidad de todos los productos.
- `calcularIGV()`: calcula el 18% del subtotal.
- `calcularTotal()`: suma el subtotal y el IGV.
- `mostrarDetalle()`: muestra el carrito con columnas alineadas.
- `calcularDescuento()`: aplica descuentos utilizando `when`.
- `buscarProducto()`: busca un producto mediante su nombre.

## Resultado final

- **Cantidad de productos:** 4
- **Subtotal:** S/ 2786.00
- **IGV (18%):** S/ 501.48
- **Total a pagar:** S/ 3287.48
- **Producto más caro:** Laptop HP
- **Descuento aplicado:** 5%
- **Total con descuento:** S/ 3123.11

## Evidencia de ejecución

![Resultado final del carrito](evidencias/resultadofinal.png)