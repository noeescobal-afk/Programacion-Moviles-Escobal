# TECSUP Fit

## Autor

- Noe Oswaldo Escobal

## Descripción

TECSUP Fit es una aplicación Android desarrollada para estudiantes que permite visualizar clases de fitness, seleccionar un horario, confirmar reservas, revisar el historial de reservas, consultar rutinas de demostración y visualizar información de perfil y estadísticas.

## Origen académico de la aplicación base

La versión base almacenada en la rama `main` fue desarrollada integrando conceptos, ejercicios y patrones de código practicados en laboratorios anteriores del curso:
- Kotlin
- Jetpack Compose
- Material 3
- Estado en Compose (`remember`, `rememberSaveable`, `mutableStateOf`, `mutableStateListOf`)
- `LazyRow` y `LazyColumn`
- Navigation Compose
- Navegación secuencial y navegación secundaria (Bottom navigation)
- Funciones composables reutilizables
- Datos y estado en memoria
- Adaptación a orientaciones vertical (portrait) y horizontal (landscape)

*Nota: La versión base en `main` no fue generada como una mejora de IA.*

## Requerimientos funcionales de la aplicación base

### Inicio y filtros
- Filtros “Hoy” y “Esta semana”.
- Lista de filtros horizontal.
- Lista de clases de fitness vertical.
- Clases predeterminadas: Yoga funcional, Cross Training y Spinning.

### Detalle y horarios
- Navegación hacia el detalle usando el identificador de clase (`classId`).
- Detalles completos de la clase (instructor, descripción, sala, duración, cupos).
- Tres horarios disponibles para selección.
- Comportamiento de selección única (tipo radio button).

### Confirmación y reservas
- Confirmación de reservas.
- Almacenamiento en memoria compartida.
- Prevención de reservas duplicadas para la misma clase y horario.
- Visualización de reservas confirmadas y completadas.
- Preservación del estado durante la navegación en la ejecución actual.

### Navegación secundaria
- Destinos de la barra inferior: Inicio, Reservas, Rutinas y Perfil.
- Navegación con `launchSingleTop`, `restoreState` y `popUpTo`.

### Rutinas y perfil
- Tres rutinas de demostración.
- Información del estudiante con avatar de iniciales ("NE").
- Estadísticas de clases tomadas, racha semanal y contador de reservas confirmadas.

### Adaptación y seguridad
- Contenido desplazable (scrollable).
- Soporte para orientación vertical y horizontal.
- Manejo seguro de rutas o identificadores inválidos.
- Navegación hacia atrás y relleno de contenido seguro sobre la barra inferior.

## Mejora asistida por IA

Esta sección aplica específicamente a la rama `mejora-ia`, donde se implementaron mejoras adicionales con asistencia de inteligencia artificial.

### Cancelación segura de reservas
- La acción “Cancelar reserva” aparece únicamente para reservas con estado exactamente igual a “Confirmada”.
- Diálogo de confirmación de Material 3 con los botones “Sí, cancelar” y “Volver”.
- El mensaje del diálogo identifica claramente la clase y el horario.
- La reserva se actualiza al estado “Cancelada” sin ser eliminada del listado.
- Las reservas completadas y ya canceladas están protegidas contra modificaciones.
- Se verifica nuevamente el estado actual antes de aplicar la actualización en memoria.
- Identificador de reserva estable para evitar cancelar elementos erróneos.
- El estado cancelado se preserva al navegar entre pantallas durante la ejecución.
- Al completarse con éxito, se muestra un `Snackbar` con el mensaje “La reserva fue cancelada”.
- Se previene la creación de reservas duplicadas.

### Mejora visual moderada
- Etiquetas de estado claras (`ReservationStatusLabel`).
- Tratamiento de color en contenedor de error para las reservas canceladas.
- Mejora en espaciado, tipografía, tarjetas, botones y jerarquía visual general.
- Mejora en el estado seleccionado de los selectores de horarios.
- Presentación refinada en la sección de perfil y estadísticas.
- Diseño coherente con Material 3 manteniendo la identidad verde de la aplicación.
- Soporte continuo para orientaciones vertical y horizontal.
- Conservación de la apariencia simple de proyecto estudiantil.

## Organización del código

La aplicación se estructura en los siguientes paquetes y archivos principales:
- `model`: contiene los modelos de datos (`FitnessClass`, `Reservation`, `Routine`).
- `data`: contiene las clases de fitness, rutinas y datos iniciales de demostración (`FitnessData.kt`).
- `navigation`: contiene las rutas (`Screen.kt`), grafo y estado compartido de reservas (`FitNavigation.kt`).
- `screens`: contiene las pantallas independientes (`HomeScreen`, `ClassDetailScreen`, `ConfirmationScreen`, `ReservationsScreen`, `RoutinesScreen`, `ProfileScreen`).
- `components`: contiene componentes reutilizables como `FitBottomBar`, `FitnessClassCard`, `ReservationCard`, `ReservationStatusLabel` y `CancelReservationDialog`.
- `ui.theme`: contiene los colores, tipografía y el tema Material 3.
- `MainActivity.kt`: archivo compacto que inicializa el tema y llama al composable de navegación raíz.

## Estado y flujo de datos

- La lista mutable de reservas es compartida y administrada en el composable de navegación raíz mediante `remember` y `mutableStateListOf`.
- El estado reside únicamente en memoria.
- `ReservationsScreen` recibe callbacks (como `onCancelReservation`) siguiendo el principio de izado de estado (state hoisting).
- No se utilizan bases de datos, APIs, ViewModel, repositorios, inyección de dependencias ni servicios de red.
- La recomposición preserva las reservas existentes sin duplicarlas ni eliminarlas.

## Flujo principal

Inicio → seleccionar clase → elegir horario → confirmar → revisar Reservas → cancelar una reserva confirmada.

## Ejecución

```powershell
Set-Location C:\Users\user\Programacion-Moviles-Escobal\Semana05\TecsupFit
.\gradlew.bat :app:assembleDebug
```

Pasos para ejecutar en Android Studio:
1. Abrir la carpeta `TecsupFit` en Android Studio.
2. Esperar a que finalice la sincronización de Gradle.
3. Seleccionar un emulador o dispositivo físico Android.
4. Ejecutar la configuración de ejecución `app`.
5. Probar el flujo completo de reserva, navegación inferior y cancelación segura.

## Pruebas realizadas

| Prueba | Resultado esperado | Resultado |
| :--- | :--- | :--- |
| Compilación Gradle | El proyecto compila sin errores | BUILD SUCCESSFUL |
| Filtro Hoy | Muestra las clases de hoy | Correcto |
| Filtro Esta semana | Muestra las clases semanales | Correcto |
| Detalle de clase | Abre la clase seleccionada | Correcto |
| Selección de horario | Permite solamente un horario | Correcto |
| Confirmación | Registra una reserva | Correcto |
| Prevención de duplicados | No registra dos veces la misma reserva | Correcto |
| Navegación inferior | Navega entre los cuatro destinos | Correcto |
| Rutinas | Muestra tres rutinas | Correcto |
| Perfil | Muestra información y estadísticas | Correcto |
| Cancelación de reserva | Cancela una reserva confirmada de forma segura | Correcto |
| Diálogo "Volver" | Cierra el diálogo sin alterar la reserva | Correcto |
| Protección de completadas | No permite cancelar reservas completadas | Correcto |
| Protección de canceladas | No permite cancelar reservas ya canceladas | Correcto |
| Preservación en lista | Mantiene el elemento cancelado visible | Correcto |
| Mensaje Snackbar | Muestra “La reserva fue cancelada” | Correcto |
| Orientación vertical | Todo el contenido es accesible | Correcto |
| Orientación horizontal | Todo el contenido puede desplazarse | Correcto |

## Prompts utilizados

Para la implementación y refinamiento de las mejoras asistidas por inteligencia artificial en la rama `mejora-ia`, se utilizaron dos prompts principales mediante Gemini en Android Studio:

1. Cancelación segura de reservas y retroalimentación visual de estados.
2. Mejora visual moderada y consistente de la interfaz.

[Ver prompts completos](PROMPTS.md)

## Limitaciones

- Todos los datos de fitness son ficticios y de demostración.
- El estado y las reservas se almacenan exclusivamente en memoria volátil.
- Al cerrar o reiniciar la aplicación, el estado se restablece a los datos iniciales.
- No incluye autenticación de usuarios, bases de datos ni servicios de red.
- La versión base no incluye cancelación (añadida en la mejora asistida por IA).

## Conclusión

TECSUP Fit demuestra de manera efectiva la integración de conceptos de desarrollo móvil nativo practicados en laboratorios previos, complementada con una mejora funcional y visual robusta implementada de forma estructurada con asistencia de IA en la rama `mejora-ia`.
