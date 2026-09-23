# Prompts de mejora con IA — TECSUP Fit

## Información general

- Proyecto: TECSUP Fit
- Autor: Noe Oswaldo Escobal
- Rama de mejora: mejora-ia
- Herramienta utilizada: Gemini en Android Studio

La aplicación base almacenada en la rama `main` fue creada integrando conceptos y ejercicios desarrollados en laboratorios anteriores del curso. Posteriormente, se utilizó inteligencia artificial en la fase de mejora (`mejora-ia`) para implementar y refinar mejoras adicionales. La versión base en `main` no fue generada como una mejora de IA.

## Prompt 1: cancelación segura de reservas

You are working only inside this existing Android Studio project:

C:\Users\user\Programacion-Moviles-Escobal\Semana05\TecsupFit

Implement one functional AI-assisted improvement in the existing TECSUP Fit application: safe reservation cancellation, together with small visual improvements related to reservation status.

This is an existing student project. Inspect the current implementation before editing it. Preserve its current architecture, navigation, screens, data, responsive behavior, and working reservation flow.

IMPORTANT SCOPE RULES

- Modify only the TecsupFit project.
- Do not modify ClinicaSaludPlus or any other project.
- Do not rewrite the application from scratch.
- Do not create Git commits or branches.
- Do not create or modify README.md or PROMPTS.md.
- Keep MainActivity.kt small.
- Do not place business logic in MainActivity.kt.
- Preserve the existing separation between model, data, navigation, screens, components, and theme.
- Use Kotlin, Jetpack Compose, Material 3, and the existing Navigation Compose implementation.
- Continue using the existing in-memory state with remember, rememberSaveable, mutableStateOf, and mutableStateListOf.
- Do not introduce ViewModel, MVVM, repositories, use cases, dependency injection, Room, Retrofit, databases, network services, coroutines, Flow, or LiveData.
- Do not upgrade Gradle, Kotlin, Compose, Android Gradle Plugin, SDK versions, or the Gradle wrapper.
- All visible interface text must remain in Spanish.
- Keep the code simple, readable, and appropriate for a student project.

FUNCTIONAL IMPROVEMENT: CANCEL A RESERVATION

1. Cancellation availability

In the existing “Reservas” screen:

- Show a visible “Cancelar reserva” action only for reservations whose current status is exactly “Confirmada”.
- Do not show or enable this action for reservations with status “Completada”.
- Do not show or enable this action for reservations with status “Cancelada”.
- Never delete canceled reservations from the list.
- Preserve the existing information shown in every reservation card.

2. Confirmation dialog

When the user presses “Cancelar reserva”, display a Material 3 AlertDialog.

The dialog must contain:

- Title: “Cancelar reserva”
- A message containing the class name and its selected schedule.
- Confirmation button: “Sí, cancelar”
- Dismiss button: “Volver”

Closing the dialog with “Volver”, the system back action, or outside dismissal must leave the reservation unchanged.

3. Safe state update

When cancellation is confirmed:

- Find the selected reservation safely in the shared in-memory reservation list.
- Verify again that its current status is exactly “Confirmada”.
- Replace the reservation with a copied item whose status is “Cancelada”.
- Never remove the reservation.
- Do not modify completed reservations.
- Do not create duplicate reservations.
- Preserve the canceled state while navigating between screens during the current application execution.

Use a stable reservation identifier if the Reservation model already contains one.

If Reservation does not have a stable identifier:

- Add a simple Int id property.
- Update existing demonstration reservations.
- Update newly created reservations.
- Generate new identifiers safely from the current in-memory list.
- Use the identifier to ensure that the correct reservation is canceled.

4. Navigation and state ownership

- Keep the shared mutable reservation list in the existing root navigation composable.
- Pass an onCancelReservation callback to ReservationsScreen.
- Perform the safe list update from the state owner or through the callback.
- Do not use global mutable variables.
- Preserve the current reservation creation and confirmation behavior.
- Preserve duplicate prevention.

5. Visual feedback

After a successful cancellation:

- Close the dialog.
- Show a Material 3 Snackbar with this exact message:
  “La reserva fue cancelada”
- Display “Cancelada” using a readable red or Material 3 error-toned container.
- Preserve the current visual appearance of “Confirmada” and “Completada”.
- Maintain sufficient text contrast.

6. Small interface improvements

Improve only the reservation-related interface without completely redesigning the application:

- Make the reservation status visually distinguishable.
- Keep cards, spacing, typography, colors, and buttons consistent.
- Reuse Material 3 colors from the current theme.
- Preserve the simple student-project appearance.
- Do not add images from the internet.
- Keep all content usable in portrait and landscape orientation.
- Ensure content does not become hidden behind the bottom navigation bar.

7. Reusable components

If ReservationsScreen becomes too long, create small reusable component files such as:

components/ReservationStatusLabel.kt
components/CancelReservationDialog.kt

Reuse or update the existing ReservationCard when appropriate.

Do not create unnecessary abstraction layers.

8. Existing behavior that must remain working

Do not break any of these current features:

- Inicio screen
- “Hoy” and “Esta semana” filters
- Horizontal filter list
- Vertical class list
- Class detail navigation using the class identifier
- Display of three available schedules
- Single schedule selection
- Reservation confirmation
- Duplicate reservation prevention
- Reservas section
- Rutinas section
- Perfil section
- Bottom navigation between four destinations
- Back navigation
- Scrollable content
- Portrait and landscape orientations
- Safe invalid-route handling

9. Edge cases

Handle these cases safely:

- The selected reservation no longer exists.
- The reservation is already canceled.
- The reservation is completed.
- The reservation list changes while the dialog is visible.
- Recomposition occurs after cancellation.
- The user dismisses the dialog without confirming.
- The same callback is triggered more than once.

The application must not crash, cancel the wrong reservation, or create duplicates.

10. Verification

After applying the changes:

- Remove unused imports.
- Build the project using:

.\gradlew.bat :app:assembleDebug

- Fix every compilation error before finishing.

FINAL RESPONSE

Respond in Spanish and briefly report:

- Files created
- Files modified
- How safe cancellation is handled
- Visual improvements applied
- Edge cases handled
- Exact Gradle build result

Do not only explain the solution. Apply the changes directly to the existing project.

## Prompt 2: mejora visual moderada

11. MODERATE VISUAL IMPROVEMENT

Apply a moderate and consistent visual improvement across the TECSUP Fit application while preserving its simple student-project appearance.

- Preserve every existing functional requirement and navigation flow.
- Keep the current green fitness identity, but improve the color hierarchy and readable contrast.
- Improve spacing, alignment, typography, cards, buttons, filter chips, and selected states.
- Give the top section of each screen a clearer visual hierarchy.
- Improve the visual presentation of class cards, reservation cards, routine cards, profile information, and statistics.
- Use small Material icons only where they improve understanding.
- Clearly distinguish selected and unselected schedules.
- Clearly distinguish reservation statuses: “Confirmada”, “Completada”, and “Cancelada”.
- Keep the bottom navigation readable and consistent.
- Reuse small independent Compose components when appropriate.
- Do not add animations, gradients, external images, complex custom drawings, or an excessive number of components.
- Do not change the functional behavior simply to improve appearance.
- Keep all content scrollable and usable in portrait and landscape orientation.
- Ensure no content is hidden behind system bars or the bottom navigation bar.

## Resultado obtenido

Los prompts aplicados permitieron implementar y refinar las siguientes características:
- Cancelación segura exclusivamente para reservas con estado `"Confirmada"`.
- Cuadro de diálogo de confirmación (`AlertDialog`) para la cancelación.
- Protección integral para reservas completadas y ya canceladas.
- Preservación de las reservas canceladas en el listado.
- Notificación visual mediante `Snackbar` con el mensaje `"La reserva fue cancelada"`.
- Distinción visual clara entre los estados de reserva (`"Confirmada"`, `"Completada"` y `"Cancelada"`).
- Mejoras visuales moderadas en tarjetas, espaciado, tipografía, colores, selección de horarios, perfil y jerarquía visual.
- Preservación de todos los requerimientos funcionales originales y flujos de navegación.
- Compatibilidad completa con las orientaciones vertical y horizontal.
- Mantenimiento estricto de la arquitectura por paquetes (modelos, datos, navegación, pantallas, componentes y tema) con un `MainActivity` reducido.
- Compilación exitosa en Gradle.

## Verificación manual

| Prueba | Resultado esperado | Resultado |
| :--- | :--- | :--- |
| Canceling a confirmed reservation | Cancela la reserva confirmada de forma segura | Correcto |
| Dismissing the dialog with “Volver” | Cierra el diálogo sin alterar la reserva | Correcto |
| Protecting completed reservations | No permite cancelar reservas completadas | Correcto |
| Protecting already canceled reservations | No permite cancelar reservas ya canceladas | Correcto |
| Preserving the canceled item in the list | Mantiene el elemento cancelado visible | Correcto |
| Showing “La reserva fue cancelada” | Muestra el mensaje de Snackbar esperado | Correcto |
| Navigating between the four bottom destinations | Navega correctamente entre los 4 destinos | Correcto |
| Duplicate prevention | Evita reservas duplicadas para la misma clase y horario | Correcto |
| Portrait orientation | Se visualiza y navega correctamente en vertical | Correcto |
| Landscape orientation | Contenido desplazable y accesible en horizontal | Correcto |
| Gradle compilation | Compilación limpia sin errores | BUILD SUCCESSFUL |
