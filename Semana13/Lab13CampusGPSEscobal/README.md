# CampusGPS — TECSUP Lima & Modo Académico

Aplicación Android profesional desarrollada en **Kotlin, Jetpack Compose, Material 3, MapLibre Native Android y OpenFreeMap**, diseñada para la orientación dentro de la sede **TECSUP Lima (Av. Cascanueces 2221, Santa Anita, Lima)**, conservando y mejorando los requisitos del Laboratorio 13 de Programación en Móviles.

---

## 🌟 Características Principales

1. **Selector de Dos Modos:**
   * **TECSUP Lima (Modo Principal):** Centrado en la sede real de Santa Anita (`-12.0453, -76.9533`), incluye lugares verificados (Pabellón Principal, Laboratorios MTI, Cafetería, Auditorio Central, Estacionamiento).
   * **Demo Campus (Modo Académico):** Conserva exactamente los 5 lugares y el punto base (`-12.0464, -77.0428`) de la guía original del Laboratorio 13.
2. **Navegación de Cámara Avanzada:**
   * **"Mi ubicación":** Solicita la posición GPS actual del usuario mediante `FusedLocationProviderClient` y centra el mapa.
   * **"Ver campus":** Centra la cámara en el campus seleccionado sin modificar ni forzar el GPS.
   * **"Ajustar vista":** Ajusta el zoom automáticamente para mostrar todos los puntos de interés activos.
3. **Innovación y Usabilidad:**
   * **Buscador en tiempo real:** Permite filtrar lugares por nombre o descripción.
   * **Ficha informativa:** Diálogo detallado al pulsar cualquier lugar del mapa o de la lista.
   * **Círculo de radio sutil:** Visualiza radios de 100m, 200m y 500m con un diseño traslúcido y elegante.
   * **Modo Oscuro Completo:** Sincronizado entre Material 3 y los estilos vectoriales oscuros de OpenFreeMap.

---

## 🏛️ Arquitectura MVVM + Repository

```text
PantallaMapa, MapaUbicacion y ListaLugares (View / Compose)
       | eventos                     ^ StateFlow
       v                             |
MapaViewModel -----------------------+
       | usa interfaces
       +-- UbicacionRepository -> UbicacionRepositoryGoogle -> FusedLocationProviderClient
       +-- LugaresRepository -> LugaresRepositoryLocal -> Lugares TECSUP & Demo
util: Distancia / Formato (funciones puras)
```

---

## 🚀 Ejecución y Pruebas en el Emulador Pixel 7

1. Abrir el proyecto en **Android Studio** (JDK 17).
2. Sincronizar Gradle.
3. En el emulador **Pixel 7**, configurar la ubicación simulada en Extended Controls (**...** -> **Location**):
   * Latitud: `-12.0453`
   * Longitud: `-76.9533`
4. Ejecutar la aplicación (`assembleDebug`).
5. Probar el selector de modos (**TECSUP Lima** / **Demo Campus**), el botón **"Mi ubicación"**, el botón **"Ver campus"**, los filtros y el buscador.
