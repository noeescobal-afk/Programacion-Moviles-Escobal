# Uso de IA — CampusGPS (Semana 13 / TECSUP Lima)

| Prompt formulado | Respuesta resumida | Qué se cambió o decidió | Justificación técnica |
| --- | --- | --- | --- |
| "Migra CampusGPS de Google Maps Compose a MapLibre Native Android + OpenFreeMap" | Propuso reemplazar dependencias e integrar `MapView` vía `AndroidView` | Se configuraron los estilos vectoriales `liberty` y `dark` de OpenFreeMap | Eliminar dependencia de Google Cloud y claves API de pago |
| "Implementa dos modos: TECSUP Lima y Demo Campus sin mezclar lugares ni alterar el GPS" | Propuso extender `Lugar` con `ModoCampus` y un selector en el ViewModel | Se crearon repositorios segmentados y el estado `AccionMapa` | Aislar la referencia del campus de la ubicación real del usuario |
| "Corrige el problema de la cámara y la posición anterior en California" | Propuso validar `isStyleLoaded` antes de ejecutar animaciones de cámara | Se añadió control de ciclo de vida y sincronización de estilos en MapLibre | Evitar que la carga asíncrona de estilos sobrescriba la posición de cámara |
| "Añade buscador de lugares, ficha detallada y botón Ajustar vista" | Propuso añadir `query`, `AlertDialog` de detalle y bounds de MapLibre | Se mejoró la innovación y experiencia de usuario (UI/UX) | Cumplir con los requerimientos de la fase de innovación académica |

## Revisión y estado real
Este proyecto fue desarrollado íntegramente con asistencia de inteligencia artificial (Gemini) siguiendo estrictamente las directrices del curso y los estándares de ingeniería Android y diseño UI/UX. Compilación verificada con `assembleDebug`.
