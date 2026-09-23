# TECSUP Fit

## Descripción

TECSUP Fit es una aplicación Android académica desarrollada con Kotlin y Jetpack Compose. 

La aplicación permite a los usuarios:
- Ver clases de fitness disponibles.
- Filtrar clases utilizando “Hoy” y “Esta semana”.
- Ver los detalles completos de cada clase.
- Seleccionar un horario disponible.
- Confirmar una reserva.
- Revisar las reservas actuales.
- Consultar rutinas de demostración.
- Ver el perfil de estudiante y estadísticas.

Todos los datos de la aplicación son ficticios y se utilizan con fines estrictamente académicos.

## Autor

- Noe Oswaldo Escobal

## Objetivo académico

El proyecto integra temas practicados durante los laboratorios anteriores del curso:
- Programación básica en Kotlin.
- Clases de datos en Kotlin.
- Jetpack Compose.
- Material 3.
- Gestión de estado.
- `LazyRow`.
- `LazyColumn`.
- Navegación secuencial.
- Navegación inferior (Bottom navigation).
- Argumentos de navegación.
- Controles de selección única.
- Funciones composables reutilizables.
- Separación en paquetes y archivos independientes.

## Base académica utilizada

La base funcional almacenada en la rama `main` fue creada integrando conocimientos y patrones de código practicados en laboratorios anteriores.

Conceptos y componentes utilizados:
- `Column`, `Row`, `Box` y `Scaffold`.
- `remember` y `rememberSaveable`.
- `mutableStateOf` y `mutableStateListOf`.
- `LazyRow` para filtros.
- `LazyColumn` para clases de fitness, reservas y rutinas.
- Navigation Compose.
- Navegación secuencial.
- Navegación inferior.
- Parámetros de navegación.
- Izado de estado (State hoisting).
- Comportamiento de selección única.
- Composables reutilizables.
- Separación de responsabilidades.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Gradle Kotlin DSL
- Android Studio
- Android API 24 o superior

## Requerimientos funcionales

1. Pantalla de inicio con filtros “Hoy” y “Esta semana”.
2. `LazyRow` para los filtros.
3. `LazyColumn` con al menos tres clases de fitness.
4. Filtrado de clases según la opción seleccionada.
5. Detalle de clase utilizando un identificador entero (`classId`).
6. Manejo seguro de identificadores de clase inválidos.
7. Visualización de instructor, sala, duración, cupos y descripción.
8. Visualización de tres horarios para cada clase.
9. Selección única de horario.
10. El botón de confirmación permanece deshabilitado hasta que se selecciona un horario.
11. Confirmación de reserva.
12. Prevención de reservas duplicadas.
13. Lista de reservas compartida en memoria.
14. Estado de reserva “Confirmada”.
15. Reserva de demostración con estado “Completada”.
16. Navegación inferior con Inicio, Reservas, Rutinas y Perfil.
17. Indicación visual del destino inferior seleccionado.
18. Rutinas de demostración.
19. Perfil de estudiante.
20. Estadísticas de reservas confirmadas.
21. Contenido desplazable (scrollable) en orientación vertical.
22. Contenido desplazable (scrollable) en orientación horizontal.
23. Relleno de contenido seguro (padding) por encima de la navegación inferior.

## Flujo principal

Inicio → Detalle de clase → Selección de horario → Confirmación → Reservas

- La clase seleccionada envía su `classId` a través de Navigation Compose.
- El horario seleccionado envía su `scheduleIndex`.
- La pantalla de confirmación recupera de forma segura la clase y el horario.
- La reserva se agrega a la lista compartida en memoria.
- La reserva aparece en la pantalla de Reservas.

## Navegación principal

Destinos de la barra de navegación inferior:
- Inicio
- Reservas
- Rutinas
- Perfil

Características de navegación:
- El destino actual se resalta visualmente.
- `launchSingleTop` evita destinos duplicados innecesarios.
- `restoreState` restaura el estado de los destinos principales.
- `popUpTo` gestiona la pila de navegación.
- Detalle y confirmación son destinos secuenciales.

## Organización del código

- `model`: contiene `FitnessClass`, `Reservation` y `Routine`.
- `data`: contiene clases de fitness y rutinas ficticias.
- `navigation`: contiene rutas, argumentos, grafo de navegación y estado compartido de reservas.
- `screens`: contiene las pantallas independientes de la aplicación.
- `components`: contiene tarjetas de clases reutilizables, tarjetas de reservas y la barra inferior.
- `ui/theme`: contiene colores y el tema básico de Material 3 en tonos verdes.
- `MainActivity.kt`: inicializa el tema e invoca el composable de navegación raíz.

El código de la aplicación completa no se encuentra implementado dentro de `MainActivity.kt`.

## Componentes reutilizables

- `FitBottomBar`: muestra los cuatro destinos principales.
- `FitnessClassCard`: muestra la información de la clase.
- `ReservationCard`: muestra la información de la reserva y su estado.

## Gestión del estado

La aplicación utiliza:
- `remember`
- `rememberSaveable`
- `mutableStateOf`
- `mutableStateListOf`
- Callbacks simples
- Izado de estado (State hoisting)

Detalles del estado:
- Las reservas existen únicamente durante la ejecución actual de la aplicación.
- La navegación preserva la lista de reservas compartida.
- Reiniciar la aplicación restablece los datos de demostración.
- No se utiliza persistencia en base de datos.

## Diseño adaptable

- El contenido completo de Inicio tiene desplazamiento vertical.
- Los filtros mantienen desplazamiento horizontal.
- Todas las tarjetas de clases son accesibles en orientación vertical y horizontal.
- El relleno interior del `Scaffold` evita que el contenido quede oculto.
- El `TopAppBar` permanece visible.
- El `NavigationBar` inferior permanece visible.
- La tarjeta final se muestra completamente por encima de la barra inferior.
- Las demás pantallas permiten desplazamiento cuando la altura disponible es limitada.
- Se evitaron alturas fijas que pudieran recortar contenido.

## Ejecución

```powershell
Set-Location C:\Users\user\Programacion-Moviles-Escobal\Semana05\TecsupFit
.\gradlew.bat :app:assembleDebug
```

Pasos de ejecución:
1. Abrir la carpeta `TecsupFit` en Android Studio.
2. Esperar a que finalice la sincronización de Gradle.
3. Seleccionar un emulador o dispositivo Android físico.
4. Ejecutar la configuración `app`.
5. Probar el flujo completo de reserva.
6. Probar las cuatro opciones de navegación inferior.
7. Probar las orientaciones vertical y horizontal.

## Pruebas realizadas

| Prueba | Resultado esperado | Resultado |
| :--- | :--- | :--- |
| Compilación Gradle | El proyecto compila sin errores | BUILD SUCCESSFUL |
| Filtro Hoy | Muestra las clases de hoy | Correcto |
| Filtro Esta semana | Muestra las clases semanales | Correcto |
| Detalle de clase | Abre la clase seleccionada | Correcto |
| Identificador inválido | Muestra un mensaje sin cerrar la aplicación | Correcto |
| Selección de horario | Permite solamente un horario | Correcto |
| Botón de confirmación | Se habilita después de seleccionar un horario | Correcto |
| Confirmación | Registra una reserva | Correcto |
| Prevención de duplicados | No registra dos veces la misma reserva | Correcto |
| Reservas | Muestra reservas confirmadas y completadas | Correcto |
| Rutinas | Muestra tres rutinas | Correcto |
| Perfil | Muestra información y estadísticas | Correcto |
| Barra inferior | Navega entre cuatro destinos | Correcto |
| Destino seleccionado | Resalta la opción activa | Correcto |
| Orientación vertical | Todo el contenido es accesible | Correcto |
| Orientación horizontal | Todo el contenido puede desplazarse | Correcto |
| Última tarjeta | No queda oculta por la barra inferior | Correcto |

## Verificación de compilación

- Comando: `.\gradlew.bat :app:assembleDebug`
- Resultado: `BUILD SUCCESSFUL`
- Duración observada: 852 ms
- 36 tareas accionables
- 36 tareas al día (up-to-date)
- Caché de configuración reutilizada

## Limitaciones

- Todos los datos de fitness son ficticios.
- Las reservas se almacenan únicamente en memoria.
- Reiniciar la aplicación restaura los datos de demostración.
- No cuenta con autenticación.
- No utiliza base de datos.
- No realiza conexiones de red.
- No incluye notificaciones.
- La cancelación no está implementada en esta versión base.
- La aplicación es una demostración académica.

## Conclusión

TECSUP Fit demuestra la integración de conceptos practicados en laboratorios anteriores mediante una aplicación desarrollada en Jetpack Compose que es simple, funcional, responsiva y organizada.
