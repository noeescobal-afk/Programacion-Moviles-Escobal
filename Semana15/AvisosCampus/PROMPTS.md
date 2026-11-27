# Registro de IA — Semana 15 / mejora-ia

ChatGPT se utilizó para preparar las mejoras funcionales iniciales. Posteriormente se solicitó a Gemini de Android Studio un rediseño visual adicional. El siguiente registro distingue los cambios visibles de los que aún requieren pruebas.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Rediseño premium de AvisosCampus (prompt completo debajo). | No se dispone de la respuesta textual del agente. El resultado visible incluye encabezado renovado, tarjetas de indicadores, temporizador circular, receptores y navegación inferior. | Se adoptó el rediseño visible en el emulador. No se dispone de evidencia de propuestas descartadas. | Mejorar la claridad, la organización visual y la facilidad de uso, conservando las funciones del laboratorio. |

## Prompt completo utilizado para el rediseño

Quiero que rediseñes completamente la interfaz visual de AvisosCampus, correspondiente a la Semana 15, en la rama mejora-ia.

Actualmente la aplicación funciona correctamente, pero su diseño es demasiado básico. Quiero una apariencia moderna, profesional y premium, utilizando Kotlin, Jetpack Compose y Material 3.

### Mejoras visuales

1. Implementa una identidad visual moderna con azul marino, celeste, blanco y degradados elegantes.
2. Rediseña el encabezado con un icono de campana, nombre AvisosCampus y subtítulo.
3. Mejora el panel principal con indicadores dinámicos de avisos generados, receptores activos y estado del permiso.
4. Utiliza iconos representativos para cada funcionalidad. No utilices emojis.
5. Implementa tarjetas modernas con esquinas redondeadas, elevación moderada y una mejor distribución de espacios.
6. Rediseña el recordatorio con un indicador circular animado que muestre los segundos restantes en el centro.
7. Añade animaciones suaves al activar interruptores, generar avisos y actualizar los contadores.
8. Mejora los interruptores del modo avión y cargador, mostrando claramente su estado activo o inactivo.
9. Rediseña el historial como una línea de tiempo, con iconos, colores por categoría, descripción y hora.
10. Implementa un estado vacío atractivo cuando todavía no existan avisos.
11. Añade una barra de navegación inferior con tres apartados: Inicio, Historial y Configuración.
12. En Configuración, permite seleccionar tema claro, oscuro o automático.
13. Adapta correctamente la interfaz a pantallas verticales y horizontales.
14. Evita que las tarjetas sean demasiado grandes. Aprovecha mejor el espacio disponible.
15. Añade transiciones suaves entre los apartados.

### Restricciones obligatorias

- Conserva todas las funcionalidades existentes.
- Mantén los canales Avisos y Urgentes.
- Conserva BigTextStyle en las notificaciones.
- Mantén los receptores del modo avión y del cargador.
- Conserva la cuenta regresiva de 10 segundos y su cancelación.
- Mantén el manejo del permiso denegado.
- Respeta estrictamente MVVM + Repository.
- No utilices Context, NotificationManager ni BroadcastReceiver dentro del ViewModel.
- Libera los receptores correctamente mediante onDispose.
- No utilices XML para construir interfaces.
- No modifiques ninguna carpeta de las semanas anteriores.
- Trabaja exclusivamente en Semana15/AvisosCampus.
- No realices commits, pushes ni cambios de rama.

Al terminar, verifica la compilación y actualiza PROMPTS.md registrando esta solicitud, los cambios realmente realizados y sus justificaciones técnicas.

Implementa directamente los cambios en los archivos del proyecto. No quiero solamente sugerencias ni ejemplos de código. Quiero que realices el rediseño completo.

## Evidencias y revisión

Las capturas reales se encuentran en `capturas/mejora-ia/`. Se observó la cuenta regresiva y la notificación emergente. No se afirma que se hayan completado las pruebas de cargador, expansión, temas o rotación sin evidencia adicional.
