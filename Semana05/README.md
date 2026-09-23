# Semana 05 — Campus Connect

**Autor:** Noe Oswaldo Escobal  
**Curso:** Programación de Móviles  
**Aplicativo:** Campus Connect  
**Paquete:** `com.tuapp.navlab`  
**Rama de mejoras:** `semana5-mejoras`

## 1. Descripción y objetivo

Campus Connect es una aplicación académica desarrollada en Kotlin con Jetpack Compose. Permite iniciar sesión con una cuenta de demostración, consultar un directorio de ocho estudiantes ficticios, visualizar sus detalles y acceder al perfil del usuario.

El laboratorio practica la navegación entre pantallas, el envío de argumentos y el manejo de la pila de navegación con Navigation Compose. La mejora incorpora validaciones y una presentación propia con azul marino, acentos turquesa, tarjetas e iniciales como avatares.

La aplicación contiene cinco pantallas: acceso, inicio, directorio, detalle del estudiante y perfil. La autenticación es una simulación local con una cuenta predefinida; no hay registro de cuentas, servidor ni autenticación de producción. Los datos académicos y correos mostrados son de demostración.

## 2. Requerimientos funcionales

### RF01 — Iniciar y cerrar sesión de demostración

El aplicativo debe permitir el acceso únicamente con la cuenta autorizada. Debe validar campos vacíos, formato del correo y coincidencia de credenciales, mostrar mensajes comprensibles y permitir mostrar u ocultar la contraseña.

**Criterios de aceptación:**

- Al enviar ambos campos vacíos, mostrar «Ingresa tu correo» e «Ingresa tu contraseña».
- Rechazar un correo con formato incorrecto antes de comprobar las credenciales.
- Rechazar un correo no autorizado o una contraseña incorrecta con «Correo o contraseña incorrectos».
- Permitir el acceso con `demo@campus.test` y `Campus123`.
- Comparar el correo sin distinguir mayúsculas y después de quitar espacios de los extremos; comparar la contraseña exactamente.
- Al ingresar, retirar el acceso de la pila de navegación. Al cerrar sesión, regresar al formulario limpio e impedir recuperar pantallas privadas con Atrás.

### RF02 — Consultar el directorio y el detalle de estudiantes

El aplicativo debe mostrar ocho estudiantes ficticios en una lista desplazable. Cada tarjeta debe permitir abrir la información correspondiente al estudiante seleccionado mediante su identificador entero.

**Criterios de aceptación:**

- Mostrar los ocho registros y permitir llegar al último mediante desplazamiento.
- Presentar nombre, carrera e iniciales en las tarjetas.
- Consultar una fuente de datos compartida para mostrar código, correo y descripción en el detalle.
- Navegar usando el ID del estudiante, sin depender de su posición en la lista.
- Permitir regresar de detalle a directorio y de directorio a inicio.
- Ante un identificador inexistente, mostrar «Estudiante no encontrado» y una acción de retorno.

### RF03 — Consultar el perfil y regresar al inicio

El aplicativo debe mostrar el perfil de Noe Oswaldo Escobal con avatar e información académica identificada como «Datos de demostración».

**Criterios de aceptación:**

- Mostrar el nombre del autor y la etiqueta de datos de demostración.
- Incluir una flecha de retorno y el botón «Ir al inicio».
- Volver al inicio existente sin duplicarlo y retirar el perfil de la pila, evitando que Atrás lo reabra inmediatamente.

## 3. Tecnologías y organización del código

Se utilizan Kotlin, Jetpack Compose, Material 3, Navigation Compose, Gradle con Kotlin DSL y los iconos de Material. El desarrollo asistido se realizó con Gemini integrado en Android Studio. Las versiones se encuentran en la configuración Gradle del proyecto.

Las rutas de la siguiente tabla son relativas a `app/src/main/java/com/tuapp/navlab/`, salvo donde se indica otra ubicación.

| Archivo o carpeta | Responsabilidad |
| --- | --- |
| `MainActivity.kt` | Inicializar la actividad, aplicar el tema e invocar la navegación. |
| `navigation/Screen.kt` | Definir las rutas y construir la ruta de detalle. |
| `navigation/AppNavigation.kt` | Configurar destinos, argumentos y navegación. |
| `screens/LoginScreen.kt` | Formulario, validación y acceso de demostración. |
| `screens/HomeScreen.kt` | Accesos al directorio y al perfil, y cierre de sesión. |
| `screens/ListScreen.kt` | Directorio desplazable y selección de estudiantes. |
| `screens/DetailScreen.kt` | Información del estudiante seleccionado. |
| `screens/ProfileScreen.kt` | Perfil académico y regreso al inicio. |
| `components/StudentAvatar.kt` | Avatar circular reutilizable generado a partir de iniciales. |
| `model/Student.kt` | Modelo con ID, nombre, carrera, código, correo y descripción. |
| `data/StudentDataSource.kt` | Ocho registros locales y búsqueda por ID. |
| `ui/theme/` | Colores, tipografía y tema compartido. |
| `app/src/main/res/values/strings.xml` | Nombre del aplicativo. |
| `app/build.gradle.kts` | Configuración del módulo y dependencias. |

Las pantallas y los datos se mantienen en archivos independientes de `MainActivity.kt`.

## 4. Ejecución y credenciales

1. Abrir la carpeta `Semana05` como proyecto en Android Studio.
2. Sincronizar el proyecto con Gradle.
3. Seleccionar el módulo `app` y un emulador o dispositivo compatible.
4. Ejecutar la aplicación con Run.
5. Ingresar con la cuenta de demostración.

| Campo | Valor |
| --- | --- |
| Correo | `demo@campus.test` |
| Contraseña | `Campus123` |

El directorio utiliza datos locales; sus estudiantes no representan cuentas adicionales autorizadas para iniciar sesión.

## 5. Pruebas manuales y estado de verificación

Las pruebas se realizaron en el emulador Pixel 7 API 35. La aplicación se ejecutó y se revisaron sus pantallas. La siguiente tabla registra las comprobaciones observadas en capturas o confirmadas durante la revisión manual.

| Caso | Resultado observado |
| --- | --- |
| Enviar correo y contraseña vacíos | Se mostraron los mensajes de campos obligatorios. |
| Ingresar `a` como correo | Se mostró el aviso de formato inválido. |
| Usar `otro@campus.test` con `Campus123` | Se denegó el acceso con el mensaje de credenciales incorrectas. |
| Usar el correo autorizado con contraseña `a` | Se denegó el acceso. |
| Usar las credenciales de demostración correctas | Se abrió la pantalla de inicio. |
| Recorrer el directorio | Se confirmó la presencia de los ocho estudiantes mediante desplazamiento. |
| Abrir Carlos Mendoza Silva y Camila Huamán Soto | Se mostraron los detalles correspondientes al primero y al último estudiante. |
| Regresar desde el detalle al directorio y al inicio | Retorno correcto confirmado en la revisión manual. |
| Abrir el perfil | Se mostró Noe Oswaldo Escobal con datos de demostración. |
| Revisar flechas de retorno y acción superior de cierre | Los iconos se observaron claramente sobre las barras oscuras. |
| Girar la pantalla de inicio | Se confirmó que todo el contenido era accesible mediante desplazamiento. |

**Comprobaciones adicionales pendientes de registrar individualmente:** entradas compuestas solo por espacios; contraseña con cambio de mayúsculas o espacio final; correo con mayúsculas y espacios externos; envío desde el teclado; pulsaciones repetidas; ID inexistente; recreación de la actividad; tamaño de fuente grande; retorno con Atrás después de cerrar sesión y después de «Ir al inicio». También falta registrar una prueba sistemática de los ocho detalles.

Estos casos forman parte de los criterios del prompt. Su presencia en el prompt no equivale a una prueba ejecutada. La revisión manual descrita no constituye una certificación de ausencia total de errores ni una batería automatizada.

## 6. Historial de mejoras

Los cambios se organizaron por responsabilidad en la rama `semana5-mejoras`.

| Commit | Descripción |
| --- | --- |
| `f3f586c` | Agregar el modelo Student, los ocho registros y la consulta por ID. |
| `f852edf` | Definir la identidad visual, crear el avatar reutilizable, actualizar el nombre e incorporar iconos. |
| `b9b098e` | Integrar el acceso, las cinco pantallas, los retornos y la navegación del portal académico. |

El README y los commits se elaboraron por separado del trabajo solicitado a Gemini sobre el aplicativo.

## 7. Estructura del prompt

El prompt define un rol técnico, describe el proyecto existente y delimita los archivos que pueden modificarse. Después especifica los tres requerimientos funcionales, validaciones, diseño, organización del código y criterios de aceptación. Finalmente, solicita implementación directa, verificación y un reporte de los resultados reales.

Se redactó en inglés con instrucciones explícitas para mantener la interfaz y la respuesta final en español. El texto permite trabajar tanto sobre la base como sobre una versión que ya contenga mejoras, conservando lo que funciona y evitando duplicaciones. No se ha documentado una reproducción independiente desde la base con una única ejecución.

## 8. Prompt completo utilizado en Gemini

Se conserva íntegro el texto proporcionado como prompt final, incluidos los enlaces de correo presentes en el archivo original.

<details>
<summary>Mostrar el prompt completo en inglés</summary>

```text
Act as an experienced Android developer specializing in Kotlin, Jetpack Compose, Material 3, and Navigation Compose.

Implement and verify the complete “Campus Connect” application enhancement in the existing Android Studio project using this single request. Perform the necessary source-code changes directly. Do not ask me to manually edit files or implement code fragments.

1. SCOPE

Work exclusively on the Android application in Semana05.

You may modify application source code, Android resources, and build configuration only when necessary.

Do not:

- Create, modify, or delete README.md or documentation.
- Generate reports or instruction files.
- Execute or suggest Git operations.
- Change branches.
- Modify other weeks or unrelated projects.
- Add functionality beyond the three requirements below.

All application text and your final explanation must be in Spanish.

2. INSPECT BEFORE EDITING

The package is com.tuapp.navlab.

The baseline contains:

- MainActivity.kt
- navigation/Screen.kt
- navigation/AppNavigation.kt
- screens/HomeScreen.kt
- screens/ListScreen.kt
- screens/DetailScreen.kt
- screens/ProfileScreen.kt
- ui/theme/

Navigation uses a sealed class, NavHost, and an integer itemId argument.

Inspect the actual project and dependencies first. Some requested improvements may already exist:

- Preserve working functionality.
- Implement missing requirements.
- Repair defects.
- Do not duplicate screens, routes, data sources, or components.
- Preserve existing package names and compatible dependency versions.
- Avoid unnecessary architecture changes or toolchain upgrades.

3. APPLICATION OBJECTIVE

Create a local academic demonstration application called “Campus Connect” with five screens:

- Login
- Home
- Student directory
- Student details
- User profile

Preserve the laboratory’s focus on Compose navigation, navigation arguments, and correct back-stack behavior.

Implement exactly these three main functional requirements.

4. RF01 — DEMONSTRATION LOGIN AND LOGOUT

Create or complete LoginScreen with:

- “Campus Connect” branding.
- A short welcome message.
- Email field.
- Password field.
- Password visibility toggle.
- “Ingresar” button.
- A secondary “Acceso de demostración” section displaying the test credentials.

The only authorized account is:
Email: [demo@campus.test](mailto\:demo@campus.test)
Password: Campus123

This is a local academic simulation. Do not add registration, password recovery, Firebase, backend services, or production authentication.

INPUT VALIDATION

Use the same validation function for the login button and keyboard submission.

Validate in this order:

A. Required fields

- Empty or whitespace-only email: “Ingresa tu correo”.
- Empty or whitespace-only password: “Ingresa tu contraseña”.
- When both are missing, show both messages.
- Remain on Login.
- Keep the button enabled so invalid submissions produce clear feedback.

B. Email format

- Trim leading and trailing whitespace.
- Reject internal whitespace.
- Validate with android.util.Patterns.EMAIL_ADDRESS.
- Invalid format: “Ingresa un correo válido. Ejemplo: [demo@campus.test](mailto\:demo@campus.test)”.
- Reject examples such as “a”, “demo”, and “demo@”.
- A syntactically valid address is not necessarily an authorized account.

C. Credentials

- Compare the normalized email case-insensitively.
- Compare the password exactly, including case and whitespace.
- Do not trim or silently transform the password.
- Reject every other email and password.
- When input validation passes but credentials do not match, display “Correo o contraseña incorrectos”.
- Do not invent additional password restrictions unrelated to this demonstration account.

ERROR PRESENTATION

- Use field-level isError and supportingText.
- Show credential errors near the form.
- Do not communicate errors through color alone.
- Show validation errors only after a submission attempt.
- Clear a field’s previous error when that field is edited; revalidate on submission.
- Clear the general credential error when either field changes.
- Preserve the email after a failed attempt.
- Toggling visibility must not change the password value.
- Use accessible labels “Mostrar contraseña” and “Ocultar contraseña” as appropriate.

NAVIGATION

- A fresh application launch starts at Login.
- Successful login opens Home and removes Login from the back stack.
- Home includes a greeting, directory and profile access cards, and “Cerrar sesión”.
- Logout clears all private destinations and opens a clean Login form.
- Back after logout must never recover a private destination.
- Prevent repeated taps from creating duplicate destinations.
- Preserve coherent navigation during rotation and activity recreation.
- No durable session persistence is required.
- Never log or persist the entered password, including through rememberSaveable.

5. RF02 — STUDENT DIRECTORY AND DETAILS

Create or reuse one Student model and one shared local data source containing exactly eight fictional students.

Each student includes:

- Unique integer ID.
- Full name.
- Degree program.
- Academic code.
- Fictional email using campus.test.
- Short description.

DIRECTORY

In ListScreen:

- Display “Directorio de estudiantes”.
- Use LazyColumn with stable keys based on IDs.
- Show selectable cards with initials avatar, name, and degree program.
- Allow scrolling to reach all eight students.
- Provide a clearly visible back arrow to Home.

DETAILS

On selection:

- Build the route using the student ID, not the list position.
- Reuse or adapt Screen.Detail.createRoute.
- Declare itemId as NavType.IntType.
- Retrieve the matching student from the shared data source.

In DetailScreen:

- Display the selected student’s information.
- Group content into readable sections.
- Provide a clearly visible back arrow to the directory.
- Handle a missing argument defensively.
- For an unknown integer ID, show “Estudiante no encontrado” and a working return action.
- Avoid forced null assertions and lookups that throw when no record is found.

Expected navigation:
Home → Directory → Details.
Back:
Details → Directory → Home.

6. RF03 — PROFILE AND RETURN TO HOME

In ProfileScreen:

- Display “Noe Oswaldo Escobal”.
- Include an initials avatar.
- Show fictional academic information labeled “Datos de demostración”.
- Do not invent real personal contact information.
- Do not use the instructor’s name.
- Include a visible back arrow and an “Ir al inicio” button.

The “Ir al inicio” button must:

- Return to the existing Home destination.
- Remove Profile from the back stack.
- Avoid duplicating Home.
- Prevent Back from reopening the dismissed Profile.

Do not add profile editing or registration.

7. VISUAL DESIGN AND ICON CONTRAST

Use Material 3 with:

- Navy blue as the primary color.
- Turquoise accents.
- Light backgrounds.
- Rounded cards.
- Consistent spacing based on 8, 16, and 24 dp.
- Shared typography and colors defined through MaterialTheme.
- An explicit light theme with dynamic colors disabled.

SCREEN COMPOSITION

Login:
Branded welcome, form card, primary action, and secondary demonstration credentials.

Home:
Greeting, directory and profile cards with icons and descriptions, and a separate logout action.

Directory:
Initials avatar on the left, clear name/program hierarchy, and a detail indicator.

Details:
Prominent identity header and grouped academic information.

Profile:
Personal header, demonstration information, and return action.

MANDATORY TOP-BAR CONTRAST

Back arrows and logout icons must be clearly visible against navy top bars.

- Configure top-bar container, title, navigation-icon, and action-icon colors explicitly.
- For a primary-color container, use the matching onPrimary foreground color, which must provide readable contrast.
- Check navigationIconContentColor and actionIconContentColor in the applicable Material 3 top-bar color configuration.
- Inspect individual Icon tint values and remove or correct dark overrides that defeat the top-bar foreground color.
- Do not render enabled navigation or logout actions with disabled-looking opacity.
- Apply the correction consistently to Directory, Details, Profile, and Home actions.
- Home must not have a back arrow that returns to Login. Logout is the explicit action for leaving the session.
- Use the auto-mirrored back-arrow icon when available.
- Add Spanish accessibility descriptions such as “Volver” and “Cerrar sesión”.

Also ensure Android status-bar icons remain readable against their background using APIs compatible with the existing project.

8. RESPONSIVENESS AND ACCESSIBILITY

- Keep all interface text in Spanish.
- Generate avatars from initials without external images.
- Use touch targets of at least 48 dp.
- Provide descriptions for interactive icons without redundant announcements for decorative icons.
- Maintain readable contrast, including text on turquoise surfaces.
- Do not rely on color alone for validation.
- Avoid fixed heights that clip text.
- Support larger fonts and small screens.
- Preserve existing working scrolling behavior.
- In landscape, all content and actions must remain reachable through scrolling where needed; everything does not need to fit simultaneously.
- Do not lock orientation to hide layout problems.
- Keep fields, error messages, and submission reachable with the keyboard open.
- Apply system and keyboard insets without double padding.
- Configure appropriate keyboard types and Next/Done actions.
- Do not add controls for unimplemented features.

9. CODE ORGANIZATION

Maintain independent responsibilities:

MainActivity.kt:
Activity setup, theme application, and AppNavigation invocation only.

navigation/Screen.kt:
Sealed-class routes and detail route construction.

navigation/AppNavigation.kt:
NavHost, destinations, arguments, and back-stack behavior.

screens/:

- LoginScreen.kt
- HomeScreen.kt
- ListScreen.kt
- DetailScreen.kt
- ProfileScreen.kt

components/:
Reusable components such as StudentAvatar. If a shared top bar meaningfully prevents inconsistent icon colors, extract one and use it consistently.

model/Student.kt:
Student data model.

data/StudentDataSource.kt:
Local records and ID lookup.

ui/theme/:
Colors, typography, and theme.

res/values/strings.xml:
Static interface text and parameterized strings where appropriate.

Do not put screen implementations, student records, or login validation inside MainActivity.

Keep the code understandable for a student. Avoid unnecessary layers, dependencies, and duplicate session state. Comment only non-obvious decisions, especially back-stack operations.

10. IMPLEMENTATION AND BUILD

Complete inspection, implementation, integration, and available verification in this single request.

- Edit files directly when tools permit.
- Do not stop after presenting a plan.
- Do not ask me to manually copy fixes into individual files.
- Request only permissions required by the tools.
- Reuse existing dependencies and verify imported APIs and icons.
- Add dependencies only when necessary and compatible.
- Use remember and rememberSaveable appropriately for non-sensitive state.
- Do not leave TODOs, placeholders, empty implementations, unresolved imports, or missing resources.

If editing is unavailable, clearly report that limitation rather than claiming the application was modified.

If execution tools are available:

- Build the app module with the project’s Gradle Wrapper.
- Fix errors introduced by your implementation.
- Build again.
- Report external blockers precisely.
- If a connected emulator is available and launching is supported, launch the application for verification.

Do not equate successful compilation with successful runtime testing.

11. ACCEPTANCE CHECKS

Execute these checks where tools permit. Otherwise, report them as pending.

RF01:

- Both inputs empty: both required-field errors appear.
- Whitespace-only inputs: required-field errors.
- Email “a” or “demo@”: invalid-format error.
- [otro@campus.test](mailto\:otro@campus.test) with Campus123: access denied.
- Authorized email with “a”: access denied.
- Password “campus123”: access denied.
- Password “Campus123 ”: access denied.
- Email “ [DEMO@CAMPUS.TEST](mailto\:DEMO@CAMPUS.TEST) ” with Campus123: access granted.
- Password visibility changes without changing its value.
- Repeated taps do not duplicate Home.
- Logout followed by Back does not restore private screens.

RF02:

- Exactly eight students are present and reachable.
- Each selected card opens the matching record.
- Details → Directory → Home works.
- Unknown integer IDs produce a controlled not-found state.

RF03:

- Correct user name and demonstration label.
- Return to the existing Home without duplication.
- Back does not reopen the dismissed Profile.

Visual and interaction:

- Back arrows are clearly visible on every secondary screen.
- Logout icons are clearly visible on Home.
- Status-bar icons are readable.
- Portrait and landscape content remains reachable.
- Keyboard and larger font settings do not hide essential controls.

12. FINAL RESPONSE

Respond in Spanish with only:

- A concise summary of actual application changes.
- Created or modified files and their responsibilities.
- The actual build and launch results.
- Demonstration credentials.
- Brief verification steps and any pending checks.

Do not create or modify documentation. Do not execute or suggest Git operations.

Deliver the complete application enhancement through this request, including all five screens, three functional requirements, input validation, visible navigation icons, and independently organized components.
```

</details>
