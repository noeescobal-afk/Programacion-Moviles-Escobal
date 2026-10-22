# PROMPTS.md — Fase 2 con IA

## 1. Objetivo de la fase
En esta fase utilicé Gemini como apoyo para implementar y revisar tres mejoras sobre la aplicación LavaCampus, manteniendo la arquitectura MVVM, la separación de responsabilidades y las reglas de negocio existentes en el proyecto.

## 2. Mejora 1 — Pedidos atrasados
**Commit:** `ia: agrega pedidos atrasados en la bandeja`

Se solicitó identificar en la bandeja del administrador aquellos pedidos cuya fecha de entrega ya pasó y que continúan en un estado activo.

Durante el desarrollo se implementó:
- detección de pedidos atrasados comparando la `fechaEntrega` con la fecha actual (`LocalDate.now()`);
- indicador visual con el chip `Atrasado` en las tarjetas de la bandeja;
- resaltado visual de la fecha de entrega en rojo;
- priorización de pedidos atrasados colocándolos en la parte superior de la bandeja;
- centralización de la comprobación de atraso (`Pedido.esAtrasado()`) para evitar duplicar lógica entre el ViewModel y la UI.

Prueba realizada:
Se verificaron pedidos con fechas anteriores a la fecha actual que aparecieron identificados como `Atrasado` y ordenados con prioridad en la bandeja.

## 3. Mejora 2 — Descuento por volumen
**Commit:** `ia: agrega descuento por volumen`

**Regla solicitada:**
Si el servicio de lavado supera estrictamente los 5 kg, se aplica un descuento del 10 % sobre el importe correspondiente al lavado.

Se documentó e implementó:
- nuevo campo `descuento` persistido en la entidad `Pedido`;
- actualización de la versión de la base de datos Room a la versión 2;
- cálculo del descuento por volumen en el repositorio y el ViewModel;
- visualización detallada en el resumen de costos con Subtotal, Descuento por volumen (-10 %) y Total final;
- persistencia del descuento en Room para conservar el valor histórico.

### Corrección realizada durante la revisión
La primera propuesta generada por la IA sugirió utilizar una migración destructiva de Room (`fallbackToDestructiveMigration`). Dicha solución fue rechazada durante la revisión manual porque podía eliminar datos existentes de los usuarios y pedidos. Se sustituyó correctamente por una migración explícita de Room de versión 1 a 2 utilizando la sentencia `ALTER TABLE pedidos ADD COLUMN descuento REAL NOT NULL DEFAULT 0.0` mediante `addMigrations(...)`.

Pruebas realizadas:
- 5.0 kg: no aplica descuento;
- 5.5 kg a S/ 8.00/kg (subtotal S/ 44.00): se aplicó un descuento de S/ 4.40 y el total final fue S/ 39.60;
- 8.0 kg de lavado dentro de una selección mayor: se aplicó correctamente el descuento del 10 % sobre el componente de lavado.

## 4. Mejora 3 — Repetir un pedido
**Commit:** `ia: agrega repetir un pedido`

Se solicitó permitir utilizar un pedido anterior como base para preparar uno nuevo sin crear automáticamente el pedido en la base de datos.

Se implementó:
- botón `Repetir pedido` en la pantalla de detalle del pedido;
- diálogo de confirmación mediante `AlertDialog` antes de iniciar el flujo;
- transporte eficiente únicamente del ID del pedido mediante la ruta sellada `AppScreen.NuevoPedido(repetirPedidoId)`;
- recuperación del pedido original en `NuevoPedidoViewModel` utilizando `LavaRepository`;
- precarga automática de servicios y cantidades;
- filtrado para incluir únicamente servicios que continúan activos;
- uso de los precios actuales de los servicios al recalcular subtotales y totales;
- persistencia de la editabilidad del formulario antes de confirmar;
- re-cálculo del descuento por volumen y la fecha estimada de entrega;
- el pedido real solo se crea al pulsar el botón `Confirmar pedido`;
- mantenimiento de las reglas existentes, incluido el límite máximo de tres pedidos activos (lo cual bloqueó correctamente la creación cuando ya se alcanzaba el límite).

Pruebas realizadas:
- Un pedido anterior con 3.0 kg se precargó correctamente con sus cantidades y un total de S/ 24.00 utilizando precios vigentes;
- se modificó la cantidad a 5.5 kg y se recalculó el subtotal a S/ 44.00, descuento S/ 4.40 y total final S/ 39.60;
- al volver atrás sin confirmar, se comprobó que no se creó ningún pedido nuevo;
- al intentar confirmar teniendo tres pedidos activos, el sistema rechazó la operación mostrando el mensaje `Ya tienes 3 pedidos activos.`

## 5. Decisiones de arquitectura
Durante el proceso se mantuvieron las siguientes decisiones arquitectónicas:
- la View no accede directamente a DAO ni a la base de datos Room;
- el ViewModel se comunica exclusivamente a través de `LavaRepository`;
- los componentes Composable se concentran en estado, eventos y representación visual;
- las reglas de creación y validación se reutilizan en lugar de duplicar procesos;
- para repetir pedidos se transmite únicamente el identificador del pedido (`pedidoId`) y no una entidad Room completa a través de la navegación.

## 6. Revisión humana de las respuestas de IA
El código generado por la IA no fue aceptado de forma ciega ni automática. Se realizó una revisión rigurosa de la arquitectura, persistencia, reglas de negocio, compilación y comportamiento visual.

Ejemplos reales de intervenciones y correcciones manuales:
1. **Pedidos atrasados**: corrección de la duplicación lógica inicial entre ViewModel y UI, centralizándola mediante la función de extensión `Pedido.esAtrasado()`.
2. **Migración de base de datos**: rechazo del uso de `fallbackToDestructiveMigration` propuesto por la IA y su reemplazo por una migración explícita con `Migration(1, 2)` y `ALTER TABLE`.
3. **Repetición de pedidos**: verificación estricta de que el flujo utilice los precios actuales del catálogo de servicios y que se respete el límite de tres pedidos activos al confirmar.

## 7. Validación técnica
Para asegurar la calidad del software, se ejecutaron las siguientes validaciones:
- compilación del proyecto mediante el comando:
  `.\gradlew.bat assembleDebug`
  obteniendo como resultado `BUILD SUCCESSFUL`.
- revisión del diff de cambios antes de registrar con:
  `git diff --check`

## 8. Conclusión
La asistencia de la inteligencia artificial aceleró significativamente la estructuración inicial de las pantallas y ViewModels. Sin embargo, la revisión humana y el ajuste iterativo fueron indispensables para preservar las reglas de negocio estrictas, mantener la separación de capas de la arquitectura MVVM y garantizar la integridad de los datos persistentes en Room.
