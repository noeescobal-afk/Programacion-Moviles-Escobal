# Uso de IA — Semana 13

El docente autorizó usar IA también en la fase de funcionalidad base. Las ramas son una división de entregables, no un registro de ausencia de IA.

| Prompt formulado a ChatGPT | Respuesta resumida | Qué se cambió o decidió | Justificación técnica |
| --- | --- | --- | --- |
| "Implementa CampusGPS conforme a los nueve pasos de la guía, MVVM, repositorios y commits separados" | Propuso estructura Kotlin, permisos, mapa, distancias, UI y errores | Se dividieron archivos por capas y los cambios por etapa | Mantener dependencias y trazabilidad según rúbrica |
| "Añade filtros por categoría respetando la arquitectura" | Propuso combinar `StateFlow` de lugares y categoría | `lugaresFiltrados` se calcula en ViewModel, `FilterChip` solo emite eventos | Evitar filtrado de datos en Composables |
| "Añade radio 100, 200 y 500 m y círculo visual en mapa" | Propuso un estado de radio y un `Circle` de Maps Compose | Se dejó radio en ViewModel y dibujo en View | Separar estado de representación |
| "Adapta el diseño al modo oscuro del sistema" | Propuso `isSystemInDarkTheme` y `MapStyleOptions` | Se modificó el tema y el estilo del mapa en UI | Consistencia visual sin contaminar Repository |

## Revisión y estado real
Este código fue generado con IA y **no se ha compilado ni validado en un emulador** en este entorno. Revisar imports y Gradle, ejecutar las doce pruebas y guardar capturas verdaderas antes de entregar. No insertar claves de Google Maps en prompts ni Git.
