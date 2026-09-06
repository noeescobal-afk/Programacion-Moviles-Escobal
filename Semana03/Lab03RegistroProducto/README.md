# Laboratorio 03 - Registro de Producto con Jetpack Compose

## Datos del estudiante

- **Estudiante:** Noe Oswaldo Escobal
- **Curso:** Programación en Móviles
- **Laboratorio:** 03
- **Proyecto:** Registro de Producto
- **Parte:** Sin IA

---

# Descripción del proyecto

Este proyecto consiste en una aplicación Android desarrollada utilizando Kotlin y Jetpack Compose.

La aplicación permite registrar la información de un producto mediante una interfaz gráfica, ingresando:

- Nombre del producto.
- Precio.
- Cantidad.

Además, permite calcular el importe total del producto mediante la operación:

```
Importe total = Precio × Cantidad
```

Finalmente, muestra un resumen con los datos ingresados utilizando un componente Card.

---

# Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3

---

# Componentes utilizados

Durante el desarrollo de la interfaz se utilizaron los siguientes componentes de Jetpack Compose:

## Text

Permite mostrar textos dentro de la interfaz, como títulos, etiquetas y mensajes de ayuda.

Ejemplo:

```kotlin
Text(
    text = "Nuevo producto"
)
```

---

## OutlinedTextField

Utilizado para permitir el ingreso de información por parte del usuario.

Se utilizó para:

- Nombre del producto.
- Precio.
- Cantidad.

Ejemplo:

```kotlin
OutlinedTextField(
    value = nombre,
    onValueChange = {
        nombre = it
    }
)
```

---

## Button

Permite ejecutar una acción cuando el usuario presiona el botón.

En este proyecto se utiliza para calcular el importe del producto.

Ejemplo:

```kotlin
Button(
    onClick = {
        importe = precio * cantidad
    }
)
```

---

## Card

Permite mostrar el resumen del producto registrado dentro de una tarjeta visual.

Muestra:

- Nombre.
- Precio.
- Cantidad.
- Importe total.

---

## Column

Permite organizar los elementos de la interfaz de manera vertical.

---

## Spacer

Permite agregar espacios entre componentes para mejorar la distribución visual.

---

# Manejo del estado en Compose

Para almacenar los valores ingresados por el usuario se utilizaron:

- `remember`
- `mutableStateOf`

## mutableStateOf

Permite crear un estado observable que almacena un valor y notifica a Compose cuando cambia.

Ejemplo:

```kotlin
var nombre by mutableStateOf("")
```

---

## remember

Permite conservar el valor del estado durante la recomposición de la interfaz.

Ejemplo:

```kotlin
var nombre by remember {
    mutableStateOf("")
}
```

---

# Variables de estado utilizadas

La aplicación utiliza los siguientes estados:

```kotlin
var nombre by remember {
    mutableStateOf("")
}

var precio by remember {
    mutableStateOf("")
}

var cantidad by remember {
    mutableStateOf("")
}

var importe by remember {
    mutableStateOf(0.0)
}
```

Estos valores se actualizan conforme el usuario ingresa información.

---

# Funcionalidades implementadas

## Registro de producto

El usuario puede ingresar:

- Nombre del producto.
- Precio.
- Cantidad.

---

## Cálculo del importe

Al presionar el botón:

```
AGREGAR PRODUCTO
```

se calcula:

```
Importe total = Precio × Cantidad
```

Ejemplo:

```
Precio: 2500
Cantidad: 2

Importe total: 5000
```

---

## Resumen del producto

La información ingresada se muestra mediante una Card:

```
Resumen del producto

Producto: Laptop HP
Precio unitario: S/ 2500
Cantidad: 2
Total: S/ 5000.00
```

---

# Estructura del proyecto

```
Lab03RegistroProducto

├── app
│   └── src
│       └── main
│           └── java
│               └── MainActivity.kt
│
├── evidencias
│   ├── C2-encabezado.png
│   ├── C3-campos.png
│   ├── C4-card-importe.png
│   └── C5-diseno.png
│
└── README.md
```

---

# Evidencias

Las capturas del desarrollo y funcionamiento de la aplicación se encuentran en la carpeta:

```
evidencias/
```

Incluyen:

- Creación del encabezado.
- Registro de campos.
- Cálculo del importe.
- Diseño final de la interfaz.

---

# Pregunta conceptual

## ¿Qué diferencia existe entre remember y mutableStateOf?

`mutableStateOf` permite crear un valor observable dentro de Compose. Cuando este valor cambia, Compose detecta el cambio y actualiza automáticamente los elementos relacionados de la interfaz.

Por otro lado, `remember` permite conservar ese valor durante las recomposiciones de la interfaz, evitando que el estado vuelva a su valor inicial.

Ambos trabajan juntos para mantener los datos ingresados por el usuario mientras utiliza la aplicación.

Ejemplo:

```kotlin
var nombre by remember {
    mutableStateOf("")
}
```

---

# Conclusión

En este laboratorio se desarrolló una aplicación utilizando Jetpack Compose, aplicando componentes básicos de interfaz, manejo de estados y eventos para construir una pantalla funcional de registro de productos.