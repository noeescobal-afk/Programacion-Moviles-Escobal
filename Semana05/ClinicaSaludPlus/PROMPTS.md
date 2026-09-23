# Prompts utilizados en Clínica Salud+

- Autor: Noe Oswaldo Escobal
- Proyecto: Clínica Salud+
- Rama: `mejora-ia`
- Herramienta: Gemini integrado en Android Studio

Este archivo contiene los prompts completos utilizados para las mejoras realizadas en la rama `mejora-ia`.

La versión base almacenada en la rama `main` integra conocimientos y patrones de diseño practicados en laboratorios anteriores del curso y no forma parte de las mejoras documentadas en este archivo.

## Prompt 1 — Cancelación segura de citas

```text
You are working inside this existing Android Studio project only:

C:\Users\user\Programacion-Moviles-Escobal\Semana05\ClinicaSaludPlus

Implement one functional AI-assisted improvement in the existing Clínica Salud+ application: safe appointment cancellation.

This is an existing student project. Inspect the current implementation before editing it and preserve its current architecture, navigation, visual style, screens, data, and working reservation flow.

IMPORTANT SCOPE RULES

- Modify only the ClínicaSaludPlus project.
- Do not modify TecsupFit or any other project.
- Do not rewrite the application from scratch.
- Do not place the application logic in MainActivity.kt.
- Keep MainActivity.kt small.
- Preserve the existing separation between model, data, navigation, screens, components, and theme.
- Use Kotlin, Jetpack Compose, Material 3, and the existing Navigation Compose implementation.
- Continue using the existing in-memory state with remember, rememberSaveable, mutableStateOf, and mutableStateListOf.
- Do not introduce ViewModel, MVVM, repositories, use cases, dependency injection, Room, Retrofit, databases, network services, coroutines, Flow, or LiveData.
- Do not upgrade Gradle, Kotlin, Compose, Android Gradle Plugin, SDK versions, or the Gradle wrapper.
- Do not create Git commits, Git branches, README.md, or PROMPTS.md.
- All visible interface text must remain in Spanish.
- Keep the implementation simple and understandable for a student.

FUNCTIONAL IMPROVEMENT: CANCEL AN APPOINTMENT

1. Cancellation availability

In the existing “Mis citas” screen:

- Display a “Cancelar cita” action only for appointments whose current status is exactly “Confirmada”.
- Do not show or enable this action for appointments with status “Completada”.
- Do not show or enable this action for appointments with status “Cancelada”.
- Do not delete canceled appointments from the list.

2. Confirmation dialog

When the user presses “Cancelar cita”, display a Material 3 AlertDialog.

The dialog must contain:

- Title: “Cancelar cita”
- A message that includes the doctor name and appointment date or schedule.
- Confirmation button: “Sí, cancelar”
- Dismiss button: “Volver”

Closing the dialog with “Volver”, the system back action, or outside dismissal must leave the appointment unchanged.

3. State update

When cancellation is confirmed:

- Find the selected appointment safely in the shared in-memory appointment list.
- Verify again that its current status is “Confirmada”.
- Replace that appointment with a copied item whose status is “Cancelada”.
- Never remove the item.
- Do not modify completed appointments.
- Do not create duplicate appointments.
- Keep the canceled status while navigating between screens during the current application execution.

Use a stable appointment identifier if the model already contains one.

If the current Appointment model does not have a stable identifier, add a simple Int id property and update all existing demonstration and newly created appointments accordingly. Generate new identifiers safely from the current in-memory list.

4. Navigation and state ownership

- Keep the shared mutable appointment list in the existing root navigation composable.
- Pass an onCancelAppointment callback to AppointmentsScreen.
- Perform the safe list update from the state owner or through a simple callback.
- Do not use global mutable variables.
- Preserve the current appointment creation and confirmation behavior.

5. Visual feedback

After a successful cancellation:

- Close the dialog.
- Show a Snackbar or another simple Material 3 feedback message:
  “La cita fue cancelada”
- Display the status “Cancelada” using a simple Material 3 error or red-toned container.
- Preserve the existing visual treatment for “Confirmada” and “Completada”.
- Maintain readable contrast.

6. Reusable component

If the cancellation dialog or appointment card would make AppointmentsScreen unnecessarily long, place the dialog in a small independent component file such as:

components/CancelAppointmentDialog.kt

Do not create unnecessary abstraction layers.

7. Existing behavior that must remain working

Do not break any of these current features:

- Inicio screen
- Specialty filters
- Doctor list
- Doctor detail navigation with doctorId
- Date selection
- Time selection
- Single-selection behavior
- Appointment confirmation
- Mis citas
- Historial
- Modal navigation drawer
- “Regresar al inicio” actions
- Back navigation
- Scrollable content
- Safe invalid-route handling

8. Edge cases

Handle these cases safely:

- The selected appointment no longer exists.
- The appointment is already canceled.
- The appointment is completed.
- The appointment list changes while the dialog is visible.
- Recomposition occurs after cancellation.

The application must not crash and must not cancel the wrong appointment.

9. Verification

After applying the changes:

- Remove unused imports.
- Build the project using:
  .\gradlew.bat :app:assembleDebug
- Fix every compilation error before finishing.

FINAL RESPONSE

Respond in Spanish and briefly report:

- Files created
- Files modified
- How cancellation is handled
- Edge cases handled
- Exact Gradle build result

Do not only explain the solution. Apply the changes directly to the project.
```

### Resultado obtenido

- Solo las citas confirmadas muestran la acción de cancelación.
- Se muestra un `AlertDialog` de Material 3 solicitando confirmación.
- Al presionar “Volver”, la cita se conserva sin modificaciones.
- Al confirmar, el estado cambia a “Cancelada”.
- Las citas canceladas permanecen visibles en el listado.
- Las citas completadas no se pueden cancelar.
- Las citas ya canceladas no se pueden cancelar nuevamente.
- Un Snackbar muestra el mensaje “La cita fue cancelada”.
- Se valida de forma segura la cita seleccionada antes de actualizar el estado.
- Las recomposiciones no cancelan la cita incorrecta.
- El estado se preserva al navegar entre pantallas durante la ejecución.
- No se introdujeron bases de datos, redes, ViewModel, MVVM ni arquitecturas avanzadas.

## Prompt 2 — Mejora visual y de usabilidad

```text
You are working inside this existing Android Studio project only:

C:\Users\user\Programacion-Moviles-Escobal\Semana05\ClinicaSaludPlus

Improve the visual interface and usability of the existing Clínica Salud+ application while preserving all current functionality.

This is the improvement branch of a student assignment. The result should look cleaner and more consistent than the base version, but it must remain simple, understandable, and realistic for a student Jetpack Compose project.

IMPORTANT RULES

- Modify only the ClínicaSaludPlus project.
- Inspect the existing implementation before editing.
- Preserve the current package structure.
- Keep MainActivity.kt small.
- Keep models, data, navigation, screens, components, and theme in separate files.
- Do not rewrite the application from scratch.
- Do not change navigation routes or navigation arguments unless required to fix a real error.
- Do not remove or break appointment cancellation.
- Do not use ViewModel, MVVM, repositories, dependency injection, Room, Retrofit, databases, networking, coroutines, Flow, or LiveData.
- Do not upgrade Gradle, Kotlin, Compose, SDK versions, or the Gradle wrapper.
- Do not create Git commits, Git branches, README.md, or PROMPTS.md.
- Keep all visible interface text in Spanish.
- Do not add external images, gradients, animations, custom fonts, or complex visual effects.

VISUAL STYLE

Create a simple medical identity using Material 3:

- Dark blue as the primary color
- Teal as the secondary/accent color
- Very light blue-gray background
- White or lightly tinted cards
- Rounded corners between 12.dp and 16.dp
- Consistent spacing using mainly 8.dp and 16.dp
- Clear text hierarchy
- Accessible color contrast
- Simple card elevation
- Material icons when useful

Disable dynamic colors so the application keeps the same visual identity on every emulator.

SCREEN IMPROVEMENTS

1. Inicio

Improve the Home screen with:

- A clearer TopAppBar titled “Clínica Salud+”
- A short welcome section
- A subtitle explaining that the user can select a specialist
- Better selected and unselected specialty filter states
- Improved doctor cards showing:
  - A circular initials avatar
  - Doctor name
  - Specialty
  - Rating with a star icon
  - A clear navigation arrow
- Keep the current LazyRow and LazyColumn behavior.

2. Detalle del médico

Improve the doctor detail screen with:

- Back arrow
- Larger initials avatar
- Doctor name and specialty
- Rating
- Information separated into simple cards
- A prominent “Reservar cita” button
- Preserve safe doctorId handling.

3. Selección de cita

Improve the scheduling screen with:

- A clear title and short instruction
- Separate sections for “Selecciona una fecha” and “Selecciona un horario”
- Clearly visible selected and unselected states
- Only one selected date and one selected time
- A full-width confirmation button
- The button must remain disabled until both selections exist.

4. Confirmación

Improve the confirmation screen with:

- A simple success icon or check icon
- A clear confirmation title
- A summary card containing doctor, specialty, date, and time
- Clearly separated buttons for:
  - “Ver mis citas”
  - “Regresar al inicio”
- Preserve the current behavior that prevents duplicate appointments.

5. Mis citas

Keep the current appointment cancellation functionality working.

Improve each appointment card with:

- Doctor name as the main text
- Specialty, date, and time with simple icons when appropriate
- A clear status label:
  - Confirmada: blue or teal
  - Completada: neutral gray or green
  - Cancelada: red or error color
- Keep “Cancelar cita” visible only for confirmed appointments.
- Keep the cancellation confirmation dialog.
- Keep the cancellation Snackbar.
- Keep the “Regresar al inicio” button.
- Ensure long information does not overlap the status label.

6. Historial

Improve the medical history cards with:

- Date
- Specialty or consultation type
- Doctor
- Short result or observation
- A simple leading medical icon
- Clear separation between records
- Keep “Regresar al inicio”.

7. Navigation drawer

Improve the existing ModalNavigationDrawer with:

- A simple header containing “Clínica Salud+”
- A subtitle such as “Portal del paciente”
- The existing destinations:
  - Inicio
  - Mis citas
  - Historial
- Clear selected destination indication
- Simple icons
- Preserve drawer closing and navigation behavior.
- Avoid duplicate destinations in the back stack.

REUSABLE COMPONENTS

Create or reuse small independent components when useful, such as:

- DoctorInitialsAvatar
- DoctorCard
- AppointmentStatusLabel
- SectionTitle

Place them inside the existing components package.

Do not create unnecessary abstraction layers.

RESPONSIVE BEHAVIOR

- Apply Scaffold inner padding correctly.
- Preserve vertical and horizontal scrolling.
- Ensure buttons and cards remain visible on small screens.
- Avoid fixed heights that could cut off content.
- Ensure text does not overlap in portrait or landscape orientation.
- Use Modifier.fillMaxWidth where appropriate.

FUNCTIONALITY THAT MUST REMAIN WORKING

Verify all of the following:

- Specialty filters
- Doctor list
- Doctor detail navigation
- Invalid doctor handling
- Date selection
- Time selection
- Single-selection behavior
- Confirmation
- Duplicate prevention
- In-memory appointments
- Appointment cancellation
- Cancellation dialog
- Completed appointments cannot be canceled
- Canceled appointments remain visible
- Mis citas
- Historial
- Navigation drawer
- Regresar al inicio
- Back navigation

VERIFICATION

After applying the changes:

- Remove unused imports.
- Build the project using:
  .\gradlew.bat :app:assembleDebug
- Fix every compilation error before finishing.

FINAL RESPONSE

Respond in Spanish and briefly report:

- Files created
- Files modified
- Visual improvements applied
- Confirmation that appointment cancellation still works
- Exact Gradle build result

Apply the changes directly. Do not only provide code suggestions.
```

### Resultado obtenido

- Se aplicó una identidad visual médica con azul oscuro como color primario y teal como secundario.
- Se desactivaron los colores dinámicos (`dynamicColor = false`).
- Se crearon componentes visuales reutilizables (`DoctorInitialsAvatar`, `DoctorCard`, `AppointmentStatusLabel`, `SectionTitle`).
- Las tarjetas de médicos muestran iniciales, nombre, especialidad, calificación con estrella y flecha de navegación.
- Los estados de las citas son visualmente distinguibles.
- Las citas canceladas utilizan un contenedor de error/tono rojo.
- Se mejoraron las pantallas de Inicio, Detalle, Selección de cita, Confirmación, Mis citas, Historial y el cajón de navegación lateral.
- Se preservó la navegación y la funcionalidad de cancelación.
- El contenido es completamente desplazable en pantallas pequeñas.
- No se añadieron imágenes externas, degradados, animaciones ni fuentes personalizadas.

## Verificación de las mejoras

- Comando: `.\gradlew.bat :app:assembleDebug`
- Resultado: `BUILD SUCCESSFUL`
- Creación de citas verificada manualmente.
- Selección única de fecha verificada manualmente.
- Selección única de hora verificada manualmente.
- Confirmación de cancelación verificada manualmente.
- Las citas completadas no se pueden cancelar.
- Las citas canceladas permanecen visibles.
- Retroalimentación por Snackbar verificada.
- Navegación del cajón verificada.
- Acción “Regresar al inicio” verificada.
