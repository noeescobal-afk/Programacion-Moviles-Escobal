# Semana 14 — CentroTareas

**Curso:** Programación en Móviles · **Tema:** Hilos en Android · **Tecnología:** Kotlin, Jetpack Compose, Material 3, StateFlow, corrutinas.

## Objetivo

Demostrar que una app no debe bloquear el hilo principal: se implementan una tarea de progreso cancelable, un cálculo de números primos y la comparación entre cargas secuenciales y paralelas. Datos ficticios, sin conexión ni permisos especiales.

## Arquitectura

```text
PantallaTareas (Compose) → TareasViewModel (StateFlow + viewModelScope)
                                      ↓
                         TareasRepository (interfaz)
                                      ↓
                         TareasRepositoryLocal (delay, Default)
```

## Estructura

```text
app/src/main/java/com/tecsup/centrotareas/
├── MainActivity.kt
├── model/Estados.kt
├── data/TareasRepository.kt
├── data/TareasRepositoryLocal.kt
├── viewmodel/TareasViewModel.kt
├── viewmodel/TareasViewModelFactory.kt
└── ui/PantallaTareas.kt
```

## Observación del Paso 1 (ejemplo didáctico)

El ejercicio de clase introduce una espera bloqueante de cuatro segundos en el hilo principal. Al hacerlo, no se atienden toques ni se redibuja el texto «Trabajando...». Una espera mayor puede provocar el aviso «La aplicación no responde» (ANR). En el código final esta implementación se retira y se reemplaza por `viewModelScope.launch` y `withContext`/`delay`. Esta observación describe el comportamiento esperado de la demostración; no es evidencia de una ejecución verificada en este dispositivo.

## Pruebas de aceptación pendientes de ejecutar en Android Studio

- [ ] Tocar «Tócame» durante el progreso: el contador responde.
- [ ] Logcat filtro `HILO`: antes/después en main y procesamiento en Dispatchers.Default.
- [ ] Progreso de 0 a 100 % en ~4 s; cancelar y reiniciar.
- [ ] El botón Iniciar no permite tareas duplicadas.
- [ ] Primos: N=100→25, N=1000→168 y N=10000→1229.
- [ ] Cálculo grande cancelable, sin congelar la pantalla.
- [ ] Una por una ~5 s; A la vez ~3 s.
- [ ] Error simulado y reintento exitoso al desactivar el interruptor.
- [ ] Al rotar la pantalla no se reinicia el ViewModel ni el progreso.
- [ ] No quedan `GlobalScope`, `runBlocking` ni `Thread.sleep` en código final.

## Para pensar

1. **¿Por qué no se veía «Trabajando...» en el Paso 1?** Porque el hilo principal estaba bloqueado antes de poder renderizar el nuevo estado.
2. **¿Qué diferencia hay entre `Thread.sleep` y `delay`?** `sleep` bloquea físicamente el hilo; `delay` suspende la corrutina sin ocupar el hilo y respeta cancelación.
3. **¿Por qué `Default` para primos e `IO` para red?** `Default` se optimiza para tareas de CPU; `IO` para operaciones bloqueantes de entrada/salida. Estas cargas ficticias usan `delay`, sin solicitudes de red reales.
4. **¿Qué ocurre si se cierra la pantalla?** El ViewModel mantiene el trabajo durante cambios de configuración; cuando se destruye definitivamente, `viewModelScope` cancela las corrutinas asociadas.
5. **¿Por qué `async` tarda ~3 s y no ~5 s?** Porque inicia las cargas simultáneamente; el total se aproxima a la más lenta. Si una falla, `coroutineScope` cancela a sus hermanas y la excepción llega al manejo de error.

## Evidencias

Guardar capturas reales del emulador en `evidencias/`: `01_progreso.png`, `02_cancelacion.png`, `03_primos.png`, `04_cargas.png`, `05_error.png`. No se incluyen capturas inventadas.

## Base del desarrollo

Implementación estructurada conforme a la guía de clase **GLAB-S14-JLEONS-2026**; el docente autorizó asistencia de IA en ambas fases según comunicación del estudiante. Las diferencias funcionales de la rama de mejoras se documentan en `PROMPTS.md`.

## Fase 2: mejoras implementadas

1. **Progreso desde Repository:** `Flow<Float>` con `flowOn(Dispatchers.Default)`; el ViewModel colecciona eventos.
2. **Reintento automático:** pedidos reintentados hasta 3 veces con espera de 500 y 1000 ms, respetando cancelación.
3. **Comparación de tiempos:** registra 5 mediciones por modalidad mientras vive el ViewModel y muestra la media.
4. **Elemento innovador:** tarjeta «Laboratorio de eficiencia» con porcentaje de tiempo ahorrado, calculado con medidas reales de ambas modalidades.

El historial comparativo es **en memoria de sesión**; no pretende ser DataStore ni persistir tras cerrar la aplicación.
