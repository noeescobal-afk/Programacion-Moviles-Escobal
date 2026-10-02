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

## VI. Preguntas de reflexión

**1. ¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa, y no en cualquier parte de la pantalla?**
Porque el DropdownMenu se posiciona relativo a su contenedor padre. Al ponerlo dentro del mismo Box que el ícono ⋮, el menú se ancla justo debajo de ese ícono. Además, el estado `expanded` queda dentro de cada tarjeta, así que cada producto abre su propio menú sin afectar a los demás. En otro lugar de la pantalla aparecería en una posición arbitraria y no se vería asociado al producto.

**2. ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu y las del NavigationDrawer?**
Las opciones del DropdownMenu son acciones contextuales que afectan a un solo producto (marcarlo como favorito, compartirlo o reportarlo). Las del NavigationDrawer son de navegación global: llevan a otras pantallas (Inicio, Mis pedidos, Favoritos, Perfil) y están disponibles desde cualquier parte de la app.

**3. ¿Cómo tuve que estructurar mi código para que el contador de favoritos del drawer "se entere" de lo que pasa en el DropdownMenu?**
Apliqué state hoisting. La lista de favoritos (`mutableStateListOf<Int>`) vive en `AppNavegacion`, que es el ancestro común del drawer y de las tarjetas. Esa lista baja a `PantallaInicio` y a `TarjetaProducto`, y la acción de marcar favorito sube mediante la lambda `onToggleFavorito`. El drawer recibe `favoritos.size` como `cantidadFavoritos`. Como el estado es único y observable, Compose recompone el badge solo cuando cambia.

**4. ¿Qué tuve que corregir del código que me generó la IA para la mejora del badge de favoritos?**
COMPLETAR (escribe aquí lo que realmente corregiste del código del agente).

## VII. Observaciones y conclusiones

### Observaciones
1. Al compilar aparecieron errores en rojo en `AppNavegacion.kt`, `AppDrawer.kt` y `TarjetaProducto.kt`. Las causas fueron la dependencia de íconos extendidos, el uso de `HorizontalDivider` según la versión de Material 3 y el ícono `ExitToApp` de la versión AutoMirrored. Se resolvieron agregando la librería de íconos y ajustando esos componentes.
2. Tuve un obstáculo con Git: el repositorio se había clonado dos veces y la carpeta del proyecto quedó duplicada, por lo que hubo que ordenar las carpetas y recrear el proyecto en `Semana06`. También agregué un `.gitignore` para no subir `build/`, `.gradle/`, `.idea/` ni `local.properties`.
3. Dudé de dónde crear la rama `lab06ConIA`: la guía indica crearla desde `main`, pero mi Fase 1 estaba en `lab06SinIA`, así que la creé a partir de esa rama para conservar el código.

### Conclusiones
1. En la Fase 1 (sin IA) avancé más lento, pero entendí cómo se conectan las piezas: el estado `expanded` de cada tarjeta, el `DrawerState` con corrutinas y la ruta actual con `currentBackStackEntryAsState`. Eso me sirvió para explicar el funcionamiento en las preguntas de reflexión.
2. En la Fase 2 (con IA) el agente avanzó mucho más rápido, sobre todo en el diseño y en el badge, pero el resultado dependió de lo claro que fuera el prompt y de revisar el diff con cuidado. Aprendí que un prompt detallado, con nombres de archivos, parámetros y comportamiento esperado, evita muchas correcciones.
3. Comparando ambas fases, la IA acelera el trabajo repetitivo y el diseño, pero entender el state hoisting y la estructura de la app sigue siendo necesario para detectar errores y corregirlos.
