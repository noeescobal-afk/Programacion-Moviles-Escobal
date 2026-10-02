# Laboratorio 06 - Menú y Navegación (TECSUP Store)

**Alumno:** Noe Oswaldo Escobal
**Curso:** Programación en Móviles - 4to ciclo - TECSUP
**Rama:** `lab06SinIA` (Fase 1)

## Descripción
App en Jetpack Compose (Kotlin) que simula una tienda con categorías y productos. En este laboratorio se agregaron dos componentes de menú:

- **DropdownMenu** contextual (⋮) en cada tarjeta de producto.
- **NavigationDrawer** como navegación principal de la app.

## Funcionalidades
- LazyRow de categorías con filtro y LazyColumn de productos agrupados por sección.
- Ícono de 3 puntos en cada producto con opciones: Favoritos, Compartir y Reportar, cada una con su ícono y un divisor.
- Ícono ☰ en la topBar que abre el drawer.
- Drawer con encabezado de usuario (iniciales, nombre y correo).
- Destinos: Inicio, Mis pedidos, Favoritos, Perfil y Cerrar sesión.
- Navegación real entre pantallas y destino activo resaltado.

## Estructura




## Tecnologías
Kotlin, Jetpack Compose, Material 3, Navigation Compose.

## Cómo ejecutar
1. Clonar el repositorio y cambiar a la rama `lab06SinIA`.
2. Abrir `Semana06/TecsupStore` en Android Studio.
3. Esperar el Gradle Sync y ejecutar en un emulador o dispositivo.

## Control de versiones
- Fase 1: rama `lab06SinIA` con los commits de cada hito del laboratorio.
- Fase 2: rama `lab06ConIA` (badge de Favoritos en el drawer).
