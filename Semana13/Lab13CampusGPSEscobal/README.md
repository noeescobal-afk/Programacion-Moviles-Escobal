# CampusGPS — Laboratorio 13 (TECSUP)

App Android en Kotlin, Jetpack Compose, Material 3 y Google Maps Compose para ubicar 5 puntos ficticios de Lima y ordenarlos por distancia al teléfono.

## Ejecutar
1. Abrir `Semana13/Lab13CampusGPSEscobal` en Android Studio (JDK 17).
2. Activar **Maps SDK for Android** en Google Cloud. Restringir la clave a `com.tecsup.campusgps` y a la huella SHA-1 obtenida con `gradlew signingReport`.
3. Crear `local.properties` en la raíz del proyecto con `sdk.dir=...` (si hace falta) y `MAPS_API_KEY=...`. **No subirlo a Git.**
4. Sincronizar Gradle. Usar un emulador que incluya Google Play. En `Extended controls > Location`, colocar latitud **-12.0464** y longitud **-77.0428**.
5. Ejecutar, solicitar permiso y probar `Obtener mi ubicación`.

## Arquitectura MVVM + Repository
```text
PantallaMapa, MapaUbicacion y ListaLugares (View/Compose)
       | eventos                      ^ StateFlow
       v                              |
MapaViewModel ------------------------+
       | usa interfaces
       +-- UbicacionRepository -> UbicacionRepositoryGoogle -> FusedLocationProviderClient
       +-- LugaresRepository -> LugaresRepositoryLocal -> 5 lugares ficticios
util: Distancia / Formato (funciones puras)
```

## Estructura
```text
com/tecsup/campusgps/
├── MainActivity.kt
├── model/Modelos.kt
├── data/{UbicacionRepository,UbicacionRepositoryGoogle,LugaresRepository,LugaresRepositoryLocal}.kt
├── util/{Distancia,Formato}.kt
├── viewmodel/{MapaViewModel,CampusViewModelFactory}.kt
└── ui/{PantallaMapa,MapaUbicacion,ListaLugares,MensajeError}.kt
```

## Evidencias (capturas del emulador)
Se deben tomar capturas reales desde Android Studio y guardarlas en `evidencias/`:
1. `01-permiso.png` — cuadro de permiso Android.
2. `02-ubicacion.png` — mapa con ubicación.
3. `03-marcadores.png` — cinco marcadores del campus.
4. `04-lista.png` — distancias ordenadas y selección.
5. `05-error.png` — mensaje de permiso denegado o GPS apagado.

> Pendiente: capturas reales, no se han inventado ni incluido imágenes de una ejecución que no ocurrió.

## Pruebas de aceptación
Consulta `PRUEBAS.md`: las pruebas de emulador están pendientes de ejecución real.

## Para pensar
**1. ¿Por qué FusedLocationProviderClient está en el Repository?** Porque la implementación Android/Google corresponde al acceso a datos. El ViewModel usa la interfaz, facilitando cambios y pruebas sin depender directamente del servicio de ubicación.

**2. Permiso vs. clave de Maps.** El permiso lo concede cada usuario para acceder a su ubicación; la clave autoriza el acceso de la aplicación al mapa de Google. Si falta el permiso, no se conoce la posición; si falta la clave, el mapa puede aparecer gris o fallar la autorización.

**3. ¿Por qué local.properties?** Mantiene la clave fuera de los archivos versionados y permite configuración por equipo. Si se publica, revocar la clave, crear otra y revisar el historial. Una clave Maps integrada en la app no constituye un secreto inaccesible: debe restringirse por aplicación y SHA-1.

**4. ¿Dónde calcular distancias?** En `actualizarDistancias` del ViewModel invocando una función de `util/Distancia.kt`, no dentro de Compose. La interfaz dibuja estados y no debe ejecutar lógica de dominio.

**5. ¿Y si niega el permiso dos veces?** Se muestra el error, sin cerrar la app. El botón Abrir ajustes permite conceder permiso manualmente si Android ya no vuelve a mostrar el cuadro.

## Uso de IA
De acuerdo con la autorización verbal comunicada por el estudiante, se empleó ChatGPT para generar el proyecto y organizar el historial en ambas fases. Las ramas mantienen la división didáctica `main` (funcionalidad base) y `mejora-ia` (mejoras); **no representan una afirmación de que main se programara sin IA**.
