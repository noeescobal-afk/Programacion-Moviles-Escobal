\# Laboratorio 03 - Registro de Producto con IA



\## Datos del estudiante



\- \*\*Estudiante:\*\* Noe Oswaldo Escobal

\- \*\*Curso:\*\* Programación en Móviles

\- \*\*Laboratorio:\*\* 03

\- \*\*Proyecto:\*\* Registro de Producto con mejora mediante IA



\---



\# Descripción



Este proyecto corresponde a la versión mejorada del registro de productos utilizando Kotlin y Jetpack Compose.



La aplicación permite ingresar información de un producto y realizar validaciones automáticas antes de registrar los datos.



\---



\# Tecnologías utilizadas



\- Kotlin

\- Android Studio

\- Jetpack Compose

\- Material 3



\---



\# Mejoras implementadas con IA



A diferencia de la versión sin IA, esta versión incorpora validaciones inteligentes para mejorar la experiencia del usuario.



Las mejoras implementadas son:



\## Validación del nombre



Verifica que el usuario ingrese un nombre de producto válido.



Ejemplo:



```

⚠️ Ingrese el nombre del producto

```



\---



\## Validación del precio



Comprueba que el precio ingresado sea un valor numérico.



Ejemplo:



```

⚠️ El precio debe ser numérico

```



\---



\## Validación de cantidad



Verifica que la cantidad sea un número entero válido.



Ejemplo:



```

⚠️ La cantidad debe ser un número entero

```



\---



\# Manejo de estados



Se utilizaron estados de Compose mediante:



\- remember

\- mutableStateOf



Ejemplo:



```kotlin

var mensaje by remember {

&#x20;   mutableStateOf("")

}

```



El estado permite actualizar dinámicamente los mensajes mostrados al usuario.



\---



\# Funcionalidades



La aplicación permite:



\- Registrar nombre del producto.

\- Registrar precio.

\- Registrar cantidad.

\- Validar datos ingresados.

\- Calcular importe total.



La fórmula utilizada es:



```

Importe total = Precio × Cantidad

```



\---



\# Diferencia entre versión SIN IA y CON IA



\## Versión SIN IA



\- Registro básico.

\- Cálculo del importe.

\- Visualización mediante Card.



\## Versión CON IA



Incluye:



\- Validaciones automáticas.

\- Mensajes de error.

\- Mejor interacción con el usuario.



\---



\# Evidencias



Las evidencias del desarrollo se encuentran en:



```

evidencias/

```



Contiene:



\- Captura del proyecto base.

\- Captura de validaciones inteligentes.



\---



\# Conclusión



La versión con IA mejora la aplicación original incorporando validaciones que permiten detectar errores de ingreso de datos y ofrecer una experiencia más amigable para el usuario.

