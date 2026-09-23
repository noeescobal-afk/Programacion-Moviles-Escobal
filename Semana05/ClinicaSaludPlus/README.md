# Clínica Salud+

## Descripción

Clínica Salud+ es una aplicación académica desarrollada para Android utilizando Kotlin y Jetpack Compose. Permite a los usuarios gestionar citas médicas de manera intuitiva y sencilla.

Permite a los usuarios:
- Filtrar especialistas médicos.
- Ver detalles de cada doctor.
- Seleccionar fecha y hora para una cita.
- Confirmar una cita médica.
- Revisar el listado de citas programadas.
- Visualizar un historial médico de demostración.
- Cancelar de forma segura citas confirmadas.

*Nota: Todos los datos mostrados en la aplicación son ficticios y de demostración.*

## Autor

- Noe Oswaldo Escobal

## Objetivo académico

El proyecto integra los temas y conceptos practicados durante los laboratorios previos del curso:
- Programación en Kotlin.
- Clases de datos (*Data classes*).
- Diseños y layouts en Jetpack Compose.
- Controles de Material 3.
- Gestión de estado.
- Listas eficientes con Lazy.
- Navegación secuencial.
- Navegación secundaria.
- Paso de argumentos por navegación.
- Controles de selección única.
- Componibles reutilizables.
- Separación de código en archivos independientes.

## Base académica utilizada

La versión funcional base almacenada en la rama `main` fue creada integrando conocimientos y patrones de código practicados en laboratorios anteriores:
- Uso de `Column`, `Row`, `Box` y `Scaffold`.
- Manejo de estado con `remember` y `rememberSaveable`.
- Uso de `mutableStateOf` y `mutableStateListOf`.
- `LazyRow` para especialidades.
- `LazyColumn` para médicos, citas e historial.
- Navigation Compose y navegación secuencial.
- `ModalNavigationDrawer`.
- Paso de parámetros en rutas de navegación.
- Comportamiento de selección única.
- Componibles reutilizables independientes.

*La implementación en `main` representa exclusivamente los conceptos de los laboratorios previos y no debe considerarse como una mejora generada por IA.*

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Gradle Kotlin DSL
- Android Studio
- Android API 24 o superior

## Requerimientos funcionales

1. Filtros horizontales de especialidades.
2. Listado vertical de especialistas médicos.
3. Filtrado de médicos según la especialidad seleccionada.
4. Navegación al detalle del médico utilizando un identificador entero (`doctorId`).
5. Manejo seguro de identificadores inválidos.
6. Selección única de fecha.
7. Selección única de horario.
8. Botón de confirmación deshabilitado hasta que existan ambas selecciones.
9. Confirmación de cita.
10. Prevención de citas duplicadas.
11. Listado de citas con estados diferenciados.
12. Cajón de navegación lateral (*Modal drawer*) con Inicio, Mis citas e Historial.
13. Historial médico de demostración.
14. Acciones para retornar al Inicio.
15. Cancelación segura de citas confirmadas.
16. Cuadro de diálogo de confirmación antes de cancelar.
17. Protección de citas completadas (no se pueden cancelar).
18. Permanencia visible de citas canceladas.
19. Contenido desplazable y adaptable a pantallas pequeñas.

## Flujo principal

Inicio → Detalle del médico → Selección de fecha y hora → Confirmación → Mis citas

El identificador del médico se transmite a través de la ruta de navegación y las citas se almacenan en una lista mutable en memoria a nivel raíz. El cajón de navegación proporciona acceso rápido a las secciones secundarias.

## Organización del código

- `model`: Clases de datos de la aplicación.
- `data`: Datos de demostración y registros estáticos.
- `navigation`: Rutas, argumentos, grafo de navegación y estado compartido de citas.
- `screens`: Pantallas independientes de la aplicación.
- `components`: Componibles reutilizables de interfaz.
- `ui/theme`: Definición de colores y tema Material 3.
- `MainActivity.kt`: Inicializa únicamente el tema y la invocación de la navegación raíz.

El código se encuentra debidamente separado y no se concentra en `MainActivity.kt`.

## Componentes reutilizables

- `DoctorCard`: Tarjeta de presentación de un especialista.
- `DoctorInitialsAvatar`: Avatar circular con las iniciales del médico.
- `AppointmentStatusLabel`: Etiqueta distintiva del estado de la cita.
- `CancelAppointmentDialog`: Cuadro de diálogo para confirmar la cancelación.
- `SectionTitle`: Título de sección estandarizado.

## Gestión del estado

El proyecto gestiona el estado local y compartido utilizando exclusivamente:
- `remember`
- `rememberSaveable`
- `mutableStateOf`
- `mutableStateListOf`

Las citas residen en memoria durante la ejecución actual; al reiniciar la aplicación se restauran los datos de demostración iniciales.

## Ramas del proyecto

| Rama | Contenido |
|---|---|
| `main` | Versión funcional base construida mediante la integración de los temas desarrollados en laboratorios anteriores. |
| `mejora-ia` | Versión que incorpora cancelación segura de citas y mejoras visuales y de usabilidad realizadas con apoyo de Gemini. |

## Mejoras realizadas con IA

Con la asistencia de Gemini en la rama `mejora-ia`, se implementó:
- Cancelación segura de citas.
- Diálogo de confirmación de cancelación.
- Validación defensiva de citas.
- Retroalimentación mediante `Snackbar`.
- Estados visuales claros para las citas.
- Componentes de interfaz reutilizables.
- Identidad visual coherente con Material 3.
- Mejoras de diseño responsivo.

[Ver prompts completos](PROMPTS.md)

1. Prompt de cancelación segura de citas.
2. Prompt de mejora visual y de usabilidad.

## Ejecución

```powershell
Set-Location C:\Users\user\Programacion-Moviles-Escobal\Semana05\ClinicaSaludPlus
.\gradlew.bat :app:assembleDebug
```

1. Abrir la carpeta `ClinicaSaludPlus` en Android Studio.
2. Esperar la sincronización de Gradle.
3. Seleccionar un emulador o dispositivo físico.
4. Ejecutar la configuración de la app `app`.
5. Probar el flujo completo de agendamiento y navegación por el menú lateral.

## Pruebas realizadas

| Prueba | Resultado esperado | Resultado |
|---|---|---|
| Compilación Gradle | El proyecto compila sin errores | BUILD SUCCESSFUL |
| Filtros de especialidad | Muestran los especialistas correspondientes | Correcto |
| Detalle del médico | Abre el especialista seleccionado | Correcto |
| Identificador inválido | Muestra un mensaje sin cerrar la aplicación | Correcto |
| Selección de fecha | Permite solamente una fecha | Correcto |
| Selección de hora | Permite solamente una hora | Correcto |
| Confirmación | Registra la cita | Correcto |
| Prevención de duplicados | No registra dos veces la misma confirmación | Correcto |
| Cancelación descartada | “Volver” conserva la cita | Correcto |
| Cancelación confirmada | Cambia el estado a “Cancelada” | Correcto |
| Cita completada | No permite cancelarla | Correcto |
| Cita cancelada | Continúa visible | Correcto |
| Snackbar | Informa la cancelación | Correcto |
| Navegación lateral | Abre Inicio, Mis citas e Historial | Correcto |
| Regresar al inicio | Retorna a Inicio | Correcto |
| Contenido desplazable | Funciona en pantallas pequeñas | Correcto |

## Verificación de compilación

- Comando: `.\gradlew.bat :app:assembleDebug`
- Resultado: `BUILD SUCCESSFUL`
- Duración aproximada: ~3 segundos.
- Tareas ejecutadas y actualizadas correctamente.

## Limitaciones

- Los datos médicos son ficticios.
- Las citas se almacenan únicamente en memoria.
- No cuenta con autenticación de usuarios.
- No incluye base de datos persistente.
- No realiza peticiones de red.
- No incluye notificaciones push.
- Es una aplicación con fines académicos.

## Conclusión

El proyecto demuestra la exitosa integración de los contenidos desarrollados en los laboratorios prácticos del curso, complementada con mejoras funcionales y de diseño gestionadas de manera controlada en la rama `mejora-ia` con asistencia de IA.
