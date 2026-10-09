# SaludPlusCitas

Aplicación móvil para la gestión y agendamiento de citas médicas de la Clínica SaludPlus, desarrollada como parte del Laboratorio 6 del curso de Programación en Móviles.

La aplicación permite registrar pacientes, iniciar y cerrar sesión, consultar especialidades y médicos, seleccionar fechas y horarios disponibles, agendar citas, consultar el historial de citas y cancelar reservas. También incorpora pantallas de resultados médicos, notificaciones, perfil y términos y condiciones.

## Información académica

- **Curso:** Programación en Móviles
- **Semana:** 06
- **Proyecto:** Clínica SaludPlus - App Paciente
- **Estudiante:** Noe Oswaldo Escobal
- **Package:** `com.saludplus.citas`
- **Lenguaje:** Kotlin
- **Interfaz:** Jetpack Compose
- **IDE:** Android Studio

---

## Objetivo

Desarrollar una aplicación móvil para pacientes de la Clínica SaludPlus que permita gestionar el proceso completo de reserva de citas médicas mediante una interfaz desarrollada con Jetpack Compose.

El proyecto implementa navegación entre pantallas, manejo de parámetros, componentes reutilizables, colecciones en memoria y diferentes componentes de listas de Compose.

---

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Navigation Compose
- Gradle
- Git y GitHub

Los datos de usuarios, médicos, especialidades y citas son gestionados mediante colecciones en memoria. Esta versión no utiliza Room, SQLite, Firebase ni una base de datos externa.

---

## Estructura principal del proyecto

```text
com.saludplus.citas
├── MainActivity.kt
├── data
│   ├── model
│   │   ├── Usuario.kt
│   │   ├── Especialidad.kt
│   │   ├── Medico.kt
│   │   └── Cita.kt
│   └── repository
│       └── Repositorio.kt
├── navigation
│   ├── Rutas.kt
│   └── AppNavigation.kt
└── ui
    ├── components
    │   └── Componentes.kt
    ├── screens
    │   ├── auth
    │   │   ├── SplashScreen.kt
    │   │   ├── RegistroScreen.kt
    │   │   ├── LoginScreen.kt
    │   │   └── TerminosScreen.kt
    │   ├── home
    │   │   └── HomeScreen.kt
    │   ├── agendamiento
    │   │   ├── EspecialidadesScreen.kt
    │   │   ├── MedicosScreen.kt
    │   │   ├── FechaHoraScreen.kt
    │   │   ├── ConfirmarCitaScreen.kt
    │   │   └── CitaExitosaScreen.kt
    │   ├── citas
    │   │   ├── MisCitasScreen.kt
    │   │   └── DetalleCitaScreen.kt
    │   ├── perfil
    │   │   └── PerfilScreen.kt
    │   ├── resultados
    │   │   └── ResultadosScreen.kt
    │   └── notificaciones
    │       └── NotificacionesScreen.kt
    └── theme
```

---

## Modelos de datos

### Usuario

Representa a un paciente registrado en la aplicación.

Contiene:

- ID
- Nombre
- Correo electrónico
- Contraseña

### Especialidad

Representa una especialidad médica disponible.

Contiene:

- ID
- Nombre
- Descripción

### Medico

Representa a un médico perteneciente a una especialidad.

Contiene:

- ID
- Nombre
- ID de especialidad
- CMP

### Cita

Representa una reserva realizada por un paciente.

Contiene:

- ID
- ID del usuario
- ID del médico
- Fecha
- Hora
- Estado

---

## Repositorio en memoria

`Repositorio.kt` centraliza la gestión de los datos de la aplicación.

Entre sus principales operaciones se encuentran:

- Registrar usuarios.
- Iniciar sesión.
- Cerrar sesión.
- Mantener el usuario actualmente autenticado.
- Buscar especialidades.
- Obtener especialidades destacadas.
- Obtener una especialidad por ID.
- Filtrar médicos por especialidad.
- Buscar médicos.
- Obtener un médico por ID.
- Consultar horarios disponibles.
- Agendar citas.
- Obtener una cita por ID.
- Consultar las citas de un paciente.
- Cancelar citas.
- Consultar las citas almacenadas.

Al registrar correctamente un nuevo paciente, este pasa a ser el usuario de la sesión actual, permitiendo continuar directamente hacia la pantalla principal y utilizar el flujo de agendamiento.

---

## Pantallas implementadas

El proyecto cuenta con 15 pantallas independientes.

### 1. Splash

Pantalla inicial de SaludPlus.

Permite:

- Crear una cuenta.
- Iniciar sesión.

### 2. Registro

Permite registrar un nuevo paciente.

Incluye validaciones para:

- Campos obligatorios.
- Formato básico del correo electrónico.
- Longitud mínima de contraseña.
- Coincidencia entre contraseña y confirmación.
- Correo previamente registrado.

También permite acceder a los términos y condiciones.

### 3. Login

Permite iniciar sesión utilizando el correo electrónico y contraseña registrados.

Una autenticación correcta establece al paciente como usuario actual de la aplicación.

### 4. Inicio

Pantalla principal del paciente.

Muestra:

- Saludo personalizado.
- Acceso al agendamiento de citas.
- Especialidades destacadas.
- Acceso a notificaciones.
- Barra inferior de navegación.

### 5. Especialidades

Muestra las especialidades médicas disponibles.

Permite realizar búsquedas y seleccionar una especialidad para continuar con la elección del médico.

### 6. Médicos

Muestra los médicos pertenecientes a la especialidad seleccionada.

Permite:

- Buscar médicos.
- Filtrar por especialidad.
- Seleccionar un médico para continuar con la reserva.

### 7. Fecha y hora

Permite seleccionar la fecha y el horario de atención.

Utiliza una cuadrícula para mostrar los horarios disponibles.

Un horario previamente reservado para el mismo médico y fecha deja de estar disponible para nuevas reservas.

### 8. Confirmar cita

Presenta el resumen de la reserva antes de registrarla.

Muestra:

- Paciente.
- Especialidad.
- Médico.
- CMP.
- Fecha.
- Hora.

La cita solamente puede confirmarse cuando existe una sesión válida y los datos recibidos son correctos.

### 9. Cita exitosa

Confirma visualmente que la cita fue registrada.

Muestra:

- Especialidad.
- Médico.
- CMP.
- Fecha.
- Hora.
- Estado de la cita.

Permite regresar al inicio o acceder a Mis citas.

### 10. Mis citas

Muestra las citas pertenecientes al usuario autenticado.

Cada elemento presenta información como:

- Especialidad.
- Médico.
- Fecha.
- Hora.
- Estado.

También contempla un estado de lista vacía.

### 11. Perfil

Muestra información de la sesión actual:

- Nombre.
- Correo electrónico.
- Cantidad de citas registradas.

Permite cerrar la sesión.

### 12. Detalle de cita

Muestra información detallada de una cita.

Permite cancelar una cita mediante un cuadro de confirmación.

Una cita cancelada conserva su registro histórico con el estado `Cancelada`.

### 13. Resultados

Muestra una lista de resultados médicos de ejemplo mediante tarjetas.

### 14. Notificaciones

Genera información a partir de las citas del usuario.

Las notificaciones permiten identificar el estado de las citas y acceder a su detalle.

### 15. Términos y condiciones

Presenta las condiciones generales de uso de la aplicación mediante contenido desplazable.

---

## Flujo de agendamiento

El flujo principal implementado es:

```text
Inicio
  ↓
Especialidades
  ↓
Médicos
  ↓
Fecha y hora
  ↓
Confirmar cita
  ↓
Cita exitosa
```

También es posible iniciar el flujo directamente desde una especialidad destacada:

```text
Inicio
  ↓
Especialidad destacada
  ↓
Médicos
  ↓
Fecha y hora
  ↓
Confirmar cita
  ↓
Cita exitosa
```

Las rutas utilizan parámetros para transportar los datos necesarios entre pantallas.

Entre ellos:

- `especialidadId`
- `medicoId`
- `fecha`
- `hora`
- `citaId`

---

## Gestión del back stack

Después de registrar correctamente una cita se utiliza `popUpTo` para eliminar las pantallas intermedias del flujo de agendamiento.

El destino conservado es `Inicio`.

De esta manera, después de llegar a `CitaExitosa`, al utilizar la navegación Atrás de Android el usuario regresa directamente a Inicio y no vuelve a:

- Confirmar cita.
- Fecha y hora.
- Médicos.
- Especialidades.

Esta lógica funciona tanto cuando el flujo comienza mediante el botón Reservar cita como cuando comienza desde una especialidad destacada.

El cierre de sesión también limpia las pantallas correspondientes a la sesión autenticada antes de regresar al Splash.

---

## NavigationBar

Las principales pantallas utilizan una barra inferior con cuatro destinos:

1. Inicio
2. Mis citas
3. Resultados
4. Perfil

La opción correspondiente a la pantalla actual se muestra seleccionada.

---

## Componentes de Jetpack Compose

Durante el desarrollo se utilizaron distintos componentes y estructuras de Compose.

### LazyRow

Utilizado para mostrar las especialidades destacadas de forma horizontal.

### LazyColumn

Utilizado para diferentes listados, entre ellos:

- Especialidades.
- Médicos.
- Mis citas.
- Resultados.
- Notificaciones.

### LazyVerticalGrid

Utilizado para presentar los horarios disponibles durante el proceso de selección de fecha y hora.

### Scaffold

Utilizado para organizar diferentes pantallas con:

- Barra superior.
- Contenido.
- Barra inferior de navegación.

### NavigationBar

Utilizado para la navegación entre las cuatro secciones principales de la aplicación.

### AlertDialog

Utilizado para solicitar confirmación antes de cancelar una cita.

---

## Componentes reutilizables

En `ui/components/Componentes.kt` se encuentran componentes utilizados por diferentes pantallas, entre ellos:

- `BotonPrincipal`
- `BarraSuperior`
- `CampoTexto`
- `MensajeListaVacia`
- `IndicadorCargaSimple`

Esto permite evitar duplicación innecesaria y mantener una interfaz consistente.

---

## Control de horarios

La aplicación posee una lista base de horarios de atención.

Cuando se solicita la disponibilidad de un médico para una fecha determinada:

1. Se buscan las citas existentes del médico.
2. Se consideran únicamente las citas de la fecha seleccionada.
3. Las citas canceladas no bloquean horarios.
4. Se obtienen las horas ocupadas.
5. Se eliminan dichas horas de la lista de horarios disponibles.

Por ello, una hora reservada no puede seleccionarse nuevamente para el mismo médico y fecha.

Si la cita es cancelada, el horario vuelve a estar disponible.

---

## Pruebas funcionales realizadas

Durante la validación del proyecto se comprobaron los siguientes escenarios:

- Registro de un nuevo paciente.
- Inicio automático de sesión después del registro.
- Visualización del nombre del paciente en Inicio.
- Cierre de sesión.
- Inicio de sesión con una cuenta previamente registrada.
- Selección de especialidad.
- Selección de médico.
- Selección de fecha.
- Selección de horario.
- Confirmación de cita.
- Creación correcta de una cita.
- Visualización de la pantalla Cita exitosa.
- Visualización de citas del paciente.
- Bloqueo de un horario previamente reservado.
- Acceso al detalle de una cita.
- Cancelación de una cita.
- Actualización del estado a Cancelada.
- Liberación del horario después de cancelar la cita.
- Navegación mediante Inicio, Mis citas, Resultados y Perfil.
- Visualización de notificaciones.
- Limpieza del back stack después del agendamiento.
- Limpieza de navegación después del cierre de sesión.
- Flujo de reserva iniciado desde Especialidades.
- Flujo de reserva iniciado desde una especialidad destacada.

---

## Ejecución del proyecto

1. Clonar o descargar el repositorio.
2. Abrir Android Studio.
3. Abrir el proyecto ubicado en:

```text
Semana06/R1-ProyectoSinIA/SaludPlusCitas
```

4. Esperar la sincronización de Gradle.
5. Seleccionar un emulador o dispositivo Android.
6. Ejecutar la aplicación mediante **Run app**.

También puede comprobarse la compilación desde terminal:

```powershell
cd Semana06\R1-ProyectoSinIA\SaludPlusCitas
.\gradlew.bat assembleDebug
```

---

## Control de versiones

El proyecto utiliza Git para registrar de forma progresiva el desarrollo.

La implementación de la primera fase se encuentra en la rama:

```text
main
```

El historial incluye commits independientes para:

- Creación del proyecto.
- Modelos.
- Repositorio.
- Gestión de usuarios.
- Especialidades y médicos.
- Gestión de citas.
- Componentes reutilizables.
- Rutas.
- Pantallas.
- Navigation Compose.
- Integración completa de navegación.
- Correcciones funcionales encontradas durante las pruebas.

La segunda fase incorpora mejoras adicionales en una rama de trabajo independiente.

---

## Observaciones

1. El uso de colecciones en memoria permite implementar y comprobar de manera sencilla la lógica de usuarios, especialidades, médicos y citas. Sin embargo, la información se pierde cuando finaliza el proceso de la aplicación, debido a que no existe persistencia mediante una base de datos.

2. La navegación requiere considerar todos los posibles puntos de entrada a un mismo flujo. Durante las pruebas se comprobó que limpiar el back stack tomando como referencia una pantalla intermedia no era suficiente cuando el agendamiento comenzaba desde una especialidad destacada. Utilizar Inicio como referencia permitió obtener un comportamiento consistente.

3. La disponibilidad de horarios depende tanto del médico como de la fecha seleccionada. Al excluir las citas canceladas de las horas ocupadas, el horario puede ser reutilizado correctamente después de una cancelación.

---

## Conclusiones

1. Se implementó el flujo completo de agendamiento de citas médicas utilizando Kotlin y Jetpack Compose, integrando registro, autenticación, especialidades, médicos, selección de fecha y hora, confirmación y consulta de citas.

2. Las colecciones y operaciones del repositorio permiten controlar correctamente la sesión del paciente, las búsquedas, el registro de citas, la prevención de reservas duplicadas y la cancelación de citas sin necesidad de una base de datos.

3. Navigation Compose permite transportar parámetros entre las diferentes pantallas y controlar el historial de navegación mediante `popUpTo`, evitando que el usuario regrese a etapas ya finalizadas del proceso de reserva.

4. Las pruebas funcionales permitieron detectar y corregir situaciones que no eran evidentes únicamente mediante compilación, como la sesión posterior al registro y el comportamiento del back stack al iniciar una reserva desde una especialidad destacada.

---

## Autor

**Noe Oswaldo Escobal**

Programación en Móviles - Semana 06