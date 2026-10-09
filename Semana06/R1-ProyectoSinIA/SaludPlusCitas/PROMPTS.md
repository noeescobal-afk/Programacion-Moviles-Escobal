\# Registro de uso de IA - Semana 06 R2



\## Proyecto



\*\*Aplicación:\*\* Clínica SaludPlus - App Paciente  

\*\*Estudiante:\*\* Noe Oswaldo Escobal  

\*\*Rama de mejora:\*\* `semana06-mejora-ia`



Este documento registra el uso de asistencia de IA durante la segunda fase del laboratorio. Se incluyen los prompts utilizados o preparados, el objetivo de cada intervención, los resultados obtenidos y las correcciones realizadas durante la implementación.



\---



\# 1. Calendario dinámico de agendamiento



\## Prompt



```text

Necesito mejorar FechaHoraScreen.kt para implementar un calendario dinámico de agendamiento médico.



IMPORTANTE:

\- Conserva la estructura actual de la pantalla y sus parámetros.

\- No modifiques otras pantallas.

\- No modifiques Repositorio.kt.

\- No cambies las rutas de navegación.

\- El proyecto ya tiene habilitado core library desugaring porque minSdk es 24.

\- Debes utilizar java.time.LocalDate.

\- Mantén la integración existente con Repositorio.horariosDisponibles(medicoId, fecha).

\- El repositorio actualmente trabaja con fechas en formato dd/MM/yyyy.



Requisitos:



1\. Eliminar la lista de fechas fijas actualmente definida en FechaHoraScreen.

2\. Usar LocalDate.now() para obtener la fecha actual.

3\. Mostrar los próximos 5 días hábiles disponibles.

&#x20;  - No mostrar sábados.

&#x20;  - No mostrar domingos.

&#x20;  - Nunca mostrar fechas anteriores a la fecha actual.

4\. Agregar navegación semanal:

&#x20;  - Mostrar una flecha "<" para retroceder una semana.

&#x20;  - Mostrar una flecha ">" para avanzar una semana.

&#x20;  - No permitir retroceder a una semana anterior a la semana actual.

&#x20;  - Al cambiar de semana, limpiar la fecha y hora seleccionadas.

5\. Sobre los días mostrar dinámicamente el mes y año.

6\. Cada día debe mostrarse de forma compacta y similar a un calendario.

7\. Mantener LazyRow para mostrar los días.

8\. Cuando el usuario seleccione una fecha:

&#x20;  - guardarla como LocalDate;

&#x20;  - reiniciar horaSeleccionada;

&#x20;  - convertirla a dd/MM/yyyy al consultar Repositorio.horariosDisponibles().

9\. Mantener LazyVerticalGrid con 3 columnas para los horarios.

10\. Los horarios ya reservados deben seguir desapareciendo mediante Repositorio.horariosDisponibles().

11\. El botón "Continuar" solo debe habilitarse cuando exista fecha y hora seleccionadas.

12\. Cuando se llame a onContinuar(fecha, hora), convertir LocalDate nuevamente a dd/MM/yyyy.

13\. Mantener Material 3 y mejorar la organización visual.

14\. Usar Locale español para días y meses.

15\. No hardcodear ninguna fecha específica.



Antes de realizar cambios, analiza el código existente de FechaHoraScreen.kt y reutiliza todo lo que ya funciona correctamente.

```



\## Resumen de la respuesta



La asistencia propuso sustituir las fechas fijas por fechas calculadas con `LocalDate.now()`, almacenar la selección como `LocalDate`, generar días hábiles y convertir las fechas al formato `dd/MM/yyyy` al comunicarse con el repositorio.



También se mantuvieron `LazyRow`, `LazyVerticalGrid`, la consulta de horarios disponibles y la habilitación condicionada del botón Continuar.



\## Corrección realizada



Durante la prueba se detectó que, cuando la fecha actual era viernes, inicialmente solo aparecía ese viernes en lugar de los cinco días hábiles requeridos.



Se realizó una corrección posterior para continuar recorriendo el calendario después del fin de semana hasta completar exactamente cinco días hábiles.



\---



\# 2. Corrección de navegación entre bloques de días



\## Prompt preparado



```text

Corrige FechaHoraScreen.kt para que siempre muestre exactamente cinco días hábiles consecutivos.



Si el primer día disponible es viernes, el bloque debe continuar con lunes, martes, miércoles y jueves.



Al presionar la flecha siguiente debe mostrarse un nuevo bloque de cinco días hábiles sin repetir fechas del bloque anterior.



La flecha anterior debe regresar al bloque previo y nunca permitir fechas anteriores al bloque inicial.



Mantén LocalDate, LazyRow, LazyVerticalGrid y la lógica existente de horarios.

```



\## Resultado



Al intentar continuar el trabajo asistido se alcanzó el límite disponible de la herramienta, mostrando `RESOURCE\_EXHAUSTED`.



\## Corrección realizada



La lógica fue revisada manualmente. Se implementó navegación por bloques consecutivos de cinco días hábiles y se verificó que no existieran repeticiones entre bloques.



Ejemplo validado:



\- Vie 9, Lun 12, Mar 13, Mié 14, Jue 15

\- Vie 16, Lun 19, Mar 20, Mié 21, Jue 22

\- Vie 23, Lun 26, Mar 27, Mié 28, Jue 29



\---



\# 3. Formato de fecha completo en español



\## Prompt preparado



```text

Mejora ConfirmarCitaScreen para mostrar la fecha recibida en formato dd/MM/yyyy como una fecha legible en español.



Ejemplo:

16/09/2026 -> Miércoles 16 de setiembre 2026



Utiliza java.time.LocalDate, DateTimeFormatter y Locale español.



No modifiques el formato interno utilizado por el repositorio ni los parámetros de navegación.

```



\## Implementación



Debido a la limitación temporal de la herramienta de IA, la adaptación se realizó manualmente siguiendo la especificación preparada.



Se mantuvo `dd/MM/yyyy` como formato interno y se transformó únicamente para presentación.



\---



\# 4. Reutilización del formato de fecha



\## Prompt preparado



```text

Evita repetir la lógica de conversión de fechas en las distintas pantallas.



Crea una función reutilizable formatearFechaEspanol(fecha: String) dentro de ui/components.



La función debe recibir dd/MM/yyyy y devolver una fecha completa en español.



Reutilízala en las pantallas donde se presentan fechas de citas, sin modificar el formato almacenado en Repositorio.

```



\## Implementación



Se creó el componente `FormatoFecha.kt` con la función `formatearFechaEspanol()`.



La función se reutilizó en pantallas relacionadas con citas, resultados y notificaciones para mantener una presentación consistente.



\---



\# 5. Mejora visual de médicos



\## Prompt preparado



```text

Mejora MedicosScreen manteniendo toda la lógica actual.



Quiero tarjetas médicas más profesionales con:

\- fotografía circular del médico;

\- nombre;

\- especialidad;

\- CMP;

\- valoración visual;

\- indicador de disponibilidad;

\- diseño Material 3.



No modifiques la navegación ni la lógica del repositorio.

```



\## Implementación



Se incorporaron recursos gráficos locales para los médicos y se rediseñaron sus tarjetas conservando el comportamiento original de selección y navegación.



\---



\# 6. Renovación visual de la aplicación



\## Prompt preparado



```text

Mejora visualmente las pantallas principales de Clínica SaludPlus utilizando Material 3.



Mantén intacta la lógica existente, rutas, parámetros y repositorio.



Busca una identidad visual médica consistente utilizando azul, superficies claras, tarjetas con bordes redondeados, iconografía Material y una jerarquía visual clara.

```



\## Implementación



Se aplicó progresivamente una identidad visual consistente en:



\- Inicio.

\- Especialidades.

\- Selección de fecha y hora.

\- Confirmación de cita.

\- Perfil.

\- Mis citas.

\- Detalle de cita.

\- Cita exitosa.

\- Resultados.

\- Notificaciones.

\- Inicio de sesión.

\- Registro.

\- Splash.



Los cambios fueron principalmente de interfaz y conservaron el flujo funcional existente.



\---



\# 7. Perfil del paciente



\## Prompt preparado



```text

Mejora PerfilScreen con un diseño Material 3 más profesional.



Debe mostrar:

\- avatar o fotografía;

\- nombre del paciente;

\- información personal;

\- resumen de actividad;

\- botón para cerrar sesión;

\- NavigationBar inferior.



La fotografía personalizada debe mostrarse únicamente para la cuenta correspondiente. Los demás usuarios deben utilizar un avatar genérico.

```



\## Corrección realizada



Durante las pruebas se observó que la fotografía de prueba aparecía para cualquier usuario registrado.



Se corrigió la condición para mostrar la fotografía únicamente en la cuenta correspondiente y utilizar un icono genérico para los demás pacientes.



\---



\# 8. Validaciones del registro



\## Prompt preparado



```text

Mejora las validaciones de RegistroScreen y Repositorio sin cambiar el flujo de navegación.



Requisitos:

\- nombre completo compuesto únicamente por letras y separadores válidos;

\- solicitar como mínimo nombre y apellido;

\- validar correctamente la estructura del correo;

\- evitar correos duplicados ignorando mayúsculas y minúsculas;

\- contraseña de mínimo 6 caracteres;

\- contraseña con al menos una letra y un número;

\- no aceptar espacios en la contraseña;

\- verificar confirmación de contraseña;

\- mostrar mensajes de error específicos.



Mantén el diseño Material 3 existente.

```



\## Implementación y pruebas



Las validaciones se incorporaron tanto en la interfaz como en el repositorio para evitar depender únicamente de la capa visual.



Se probaron casos de:



\- números en el nombre;

\- nombre incompleto;

\- correo inválido;

\- contraseña sin números;

\- contraseña menor a seis caracteres;

\- contraseñas diferentes;

\- correo duplicado;

\- registro válido.



El proyecto compiló correctamente después de las modificaciones.



\---



\# 9. Ilustración de pantalla inicial



\## Prompt preparado



```text

Mejora la pantalla Splash de Clínica SaludPlus manteniendo los botones y navegación actuales.



Incorpora una ilustración médica local y organiza la composición para transmitir una identidad moderna de salud digital.



No modifiques el comportamiento de registro o inicio de sesión.

```



\## Implementación



Se incorporó una ilustración médica como recurso local y se reorganizó la pantalla inicial manteniendo intactos los accesos y callbacks existentes.



\---



\# Observaciones



1\. La asistencia de IA permitió plantear rápidamente una solución basada en `LocalDate` para reemplazar las fechas fijas del prototipo. Sin embargo, fue necesario probar el comportamiento real porque la primera implementación no completaba correctamente los cinco días hábiles cuando el rango atravesaba un fin de semana.



2\. La indisponibilidad posterior de la herramienta por `RESOURCE\_EXHAUSTED` hizo necesario continuar manualmente varias mejoras. Los prompts preparados sirvieron como especificación de los cambios y permitieron mantener objetivos concretos sin alterar accidentalmente la navegación o el repositorio.



3\. Separar el formato interno `dd/MM/yyyy` de la presentación de fechas en español permitió mejorar la interfaz sin modificar el contrato existente entre navegación, pantallas y repositorio.



4\. Las pruebas funcionales fueron necesarias incluso después de obtener compilaciones exitosas. Se detectaron mediante estas pruebas problemas como la repetición de fechas y el uso de una misma fotografía de perfil para diferentes usuarios.



\# Conclusiones



1\. La segunda fase mejoró significativamente la experiencia de usuario respecto a la implementación inicial, principalmente mediante el calendario dinámico, el formato de fechas en español y una interfaz Material 3 más consistente.



2\. La IA resultó útil como apoyo para generar propuestas y especificaciones, pero sus resultados necesitaron validación y correcciones manuales. Una compilación exitosa no garantiza por sí sola que el comportamiento funcional sea correcto.



3\. Mantener la lógica de negocio separada de la interfaz permitió realizar cambios visuales extensos sin alterar el flujo principal de registro, autenticación y agendamiento.



4\. La validación final combinó revisión del código, compilación con Gradle y pruebas en emulador, lo que permitió conservar el funcionamiento de las citas y mejorar simultáneamente la robustez del registro de usuarios.

