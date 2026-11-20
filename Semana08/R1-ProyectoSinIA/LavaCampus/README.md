# LavaCampus

Aplicación móvil para la gestión de una lavandería universitaria, desarrollada como parte del curso de **Programación en Móviles**.

**Autor:** Noe Oswaldo Escobal Huayana  
**Curso:** Programación en Móviles  
**Institución:** TECSUP  
**Proyecto:** Laboratorio MVVM + Room - LavaCampus

---

## Descripción

LavaCampus es una aplicación Android desarrollada con Kotlin y Jetpack Compose que permite gestionar el servicio de lavandería de un campus universitario.

El proyecto contiene una implementación base y una segunda fase de mejoras avanzadas asistidas por IA (descuento por volumen, repetición de pedidos, detección y conteo de pedidos atrasados y rediseño visual boutique), detalladas en `PROMPTS.md`.

La aplicación cuenta con dos perfiles: **Estudiante** y **Administrador**.

El estudiante puede iniciar sesión, consultar sus pedidos, registrar nuevos pedidos de lavandería con descuento por volumen, repetir pedidos anteriores, revisar tarifas, realizar seguimiento del estado de sus pedidos y administrar preferencias de su perfil.

El administrador dispone de un panel general con contador de pedidos atrasados, gestión de pedidos (con identificación visual de atrasos), servicios y clientes (con avatares fotográficos), además de opciones para controlar la recepción de nuevos pedidos.

La información se almacena localmente mediante Room (esquema versión 2 con migración explícita) y las preferencias de la aplicación se mantienen utilizando DataStore.

---

## Requisitos funcionales

### Perfil Estudiante

- Iniciar sesión como estudiante.
- Visualizar el pedido activo y su estado.
- Consultar los últimos pedidos realizados.
- Registrar un nuevo pedido.
- Seleccionar servicios y cantidades.
- Aplicación de un **10% de descuento por volumen** cuando el servicio de "Lavado y secado" supera estrictamente los 5 kg (calculado sobre el subtotal del lavado).
- Calcular automáticamente el total del pedido con desglose de subtotal, descuento y total final.
- Calcular la fecha estimada de entrega.
- Consultar el detalle de un pedido.
- Repetir un pedido anterior desde su detalle, precargando servicios activos con precios actuales para su revisión previa a la confirmación.
- Cancelar pedidos que todavía se encuentren en estado `RECIBIDO`.
- Consultar el historial de pedidos.
- Consultar las tarifas de los servicios activos.
- Visualizar información y estadísticas del perfil (con fotografía dinámica de cliente).
- Activar o desactivar el modo oscuro.
- Configurar avisos y notificaciones.
- Cambiar al perfil de administrador (regresando al Login para autenticar).
- Cerrar sesión.

### Perfil Administrador

- Visualizar un panel con indicadores generales, ingresos pagados y contador de pedidos atrasados.
- Consultar pedidos de todos los estudiantes.
- Buscar y filtrar pedidos.
- Identificar visualmente y priorizar pedidos atrasados en la Bandeja.
- Avanzar pedidos entre sus diferentes estados.
- Asignar casilleros disponibles.
- Registrar el pago de un pedido.
- Gestionar los servicios de lavandería con soporte fotográfico.
- Crear y editar servicios.
- Desactivar servicios utilizados anteriormente.
- Consultar la lista de clientes con avatares fotográficos.
- Buscar clientes por nombre o código.
- Consultar cantidad de pedidos y gasto por cliente.
- Activar o desactivar la recepción de nuevos pedidos.
- Configurar modo oscuro y notificaciones.
- Cambiar al perfil de estudiante (regresando al Login).
- Cerrar sesión.

---

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Room
- DataStore Preferences
- Navigation Compose
- Kotlin Coroutines
- Flow y StateFlow
- KSP
- Gradle

### Configuración utilizada

- Kotlin: `2.2.10`
- KSP: `2.2.10-2.0.2`
- Room: `2.7.2`
- JVM: `21`
- minSdk: `26`

---

## Arquitectura

El proyecto utiliza el patrón **MVVM (Model - View - ViewModel)**.

```text
┌──────────────────────────────┐
│             VIEW             │
│     Pantallas / Compose      │
└──────────────┬───────────────┘
               │ eventos
               ▼
┌──────────────────────────────┐
│          VIEWMODEL           │
│ Estado + reglas de negocio   │
│     Flow / StateFlow         │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          REPOSITORY          │
│       LavaRepository         │
│     LavaRepositoryRoom      │
└──────────┬───────────┬───────┘
           │           │
           ▼           ▼
┌──────────────────┐ ┌──────────────────┐
│ DAO / ROOM       │ │    DATASTORE     │
│ lavacampus.db    │ │   Preferencias   │
└──────────────────┘ └──────────────────┘
```

---

## Estructura del proyecto

```text
com.tecsup.lavacampus
│
├── MainActivity.kt
│
├── data
│   ├── local
│   │   ├── DatosIniciales.kt
│   │   ├── LavaDatabase.kt
│   │   │
│   │   ├── dao
│   │   │   ├── DetalleDao.kt
│   │   │   ├── PedidoDao.kt
│   │   │   ├── ServicioDao.kt
│   │   │   └── UsuarioDao.kt
│   │   │
│   │   ├── entity
│   │   │   ├── DetallePedido.kt
│   │   │   ├── Pedido.kt
│   │   │   ├── Servicio.kt
│   │   │   └── Usuario.kt
│   │   │
│   │   └── relation
│   │       ├── DetalleConServicio.kt
│   │       └── PedidoConDetalle.kt
│   │
│   ├── prefs
│   │   └── PreferenciasStore.kt
│   │
│   └── repository
│       ├── LavaRepository.kt
│       └── LavaRepositoryRoom.kt
...
```

---

## Base de datos

La base de datos local se denomina `lavacampus.db` y se encuentra en **versión de esquema 2**.

Para actualizar el esquema desde la versión 1 sin destruir datos existentes, se implementó una migración explícita mediante `Migration(1, 2)` que añade la columna `descuento` a la tabla `pedidos`:

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE pedidos ADD COLUMN descuento REAL NOT NULL DEFAULT 0.0")
    }
}
```

Las tablas principales son:
- `usuarios`: Almacena estudiantes y administradores.
- `servicios`: Catálogo de servicios con soporte fotográfico.
- `pedidos`: Registra los pedidos, estado, fecha de entrega, total, **descuento**, pago y casillero.
- `detalles_pedido`: Líneas individuales de cada pedido con precios históricos congelados.

---

## Reglas de negocio

1. Un estudiante solo puede visualizar sus propios pedidos.
2. Un administrador puede visualizar todos los pedidos.
3. Solo se pueden seleccionar servicios activos.
4. Debe existir al menos un servicio con cantidad mayor que cero para crear un pedido.
5. Los servicios por kilogramo admiten cantidades entre 1.0 y 10.0 kg en intervalos de 0.5 kg.
6. Los servicios por prenda o unidad utilizan cantidades enteras.
7. Un estudiante puede tener como máximo tres pedidos activos.
8. **Descuento por volumen**: Si el servicio de lavado (`KG`) supera estrictamente los 5 kg, se aplica un 10% de descuento exclusivamente sobre el subtotal del lavado. El descuento se almacena en el pedido.
9. El precio utilizado queda almacenado en el detalle del pedido.
10. La fecha de entrega se calcula utilizando el mayor tiempo de entrega de los servicios seleccionados.
11. Si la fecha calculada corresponde a domingo, la entrega pasa al lunes.
12. Un estudiante solamente puede cancelar pedidos en estado `RECIBIDO`.
13. Los estados avanzan en orden: `RECIBIDO → EN_PROCESO → LISTO → ENTREGADO` (o `CANCELADO`).
14. Para pasar a `LISTO` se requiere un casillero (`C-01` a `C-30`).
15. Para pasar a `ENTREGADO` el pedido debe estar pagado.
16. **Pedido atrasado**: Un pedido se considera atrasado cuando su `fechaEntrega` es anterior a la fecha actual y permanece en estado `RECIBIDO`, `EN_PROCESO` o `LISTO`. Los pedidos `ENTREGADO` y `CANCELADO` nunca se consideran atrasados.
17. **Repetir pedido**: Permite tomar un pedido anterior como base, precargando servicios activos con los precios actuales. Requiere confirmación y no crea el pedido automáticamente.
18. **Cambio de perfil**: Cambiar de perfil regresa al Login para autenticar el perfil seleccionado.
19. Las preferencias (modo oscuro, avisos, recepción de pedidos) se conservan mediante DataStore.

---

# Rediseño visual final

La interfaz fue rediseñada utilizando **Jetpack Compose y Material 3** bajo la identidad boutique **Crema + Verde Oliva**, incorporando:
- Tarjetas redondeadas (18-24 dp) y superficies elegantes.
- Paleta cálida en modo claro y carbón/salvia en modo oscuro.
- Fotografías locales para servicios, banner e iniciales/avatares de clientes.
- Barra de navegación inferior flotante.
- Jerarquía visual avanzada en el panel de administración y flujo de pedidos.

---

# Capturas de pantalla

## Capturas de la implementación inicial
- `docs/capturas/estudiante-inicio.png`
- `docs/capturas/estudiante-nuevo-pedido.png`
- `docs/capturas/estudiante-perfil.png`
- `docs/capturas/admin-panel.png`
- `docs/capturas/admin-gestion-pedido.png`
- `docs/capturas/admin-clientes.png`

## Capturas de la versión final
- `docs/capturas/estudiante-inicio-final.png`
- `docs/capturas/estudiante-nuevo-pedido-final.png`
- `docs/capturas/estudiante-perfil-final.png`
- `docs/capturas/admin-panel-final.png`
- `docs/capturas/admin-gestion-pedido-final.png`
- `docs/capturas/admin-clientes-final.png`

---

# Pruebas de aceptación

| Prueba | Resultado |
|---|---|
| Login con contraseña incorrecta | Se muestra mensaje de error y no permite ingresar |
| Nuevo pedido sin seleccionar servicios | El botón Confirmar permanece deshabilitado |
| 5.0 kg de lavado | No aplica descuento |
| 5.5 kg de lavado | Subtotal S/ 44.00, descuento S/ 4.40, total S/ 39.60 |
| Intentar crear un cuarto pedido activo | Se muestra el límite de tres pedidos |
| Cancelar pedido en proceso | No se permite |
| Pasar a LISTO sin seleccionar casillero | Se rechaza la operación |
| Pedido atrasado | Se identifica visualmente en rojo en la Bandeja |
| Contador de atrasados | Se muestra en el Panel |
| Repetir pedido | Precarga cantidades utilizando precios actuales tras confirmación |
| Salir sin confirmar un pedido repetido | No crea el pedido |
| Confirmar con tres pedidos activos | Se rechaza por límite de pedidos |
| Cambio de perfil | Regresa al Login para autenticar el perfil seleccionado |
| Persistencia de preferencias | La configuración se conserva al reiniciar la aplicación |

---

# Para pensar

## ¿Por qué un pedido tiene detalles en otra tabla?
Un pedido puede contener más de un servicio. Usar `detalles_pedido` evita repetir columnas dentro de `pedidos` y facilita consultas agrupadas.

## ¿Por qué guardar `precioUnitario` y `subtotal` en el detalle?
El precio representa lo que el estudiante pagó en el momento. Al cambiar las tarifas en el futuro, los pedidos antiguos conservan su historial sin alterar los cálculos.

## ¿Cuándo utilizar `suspend` y cuándo utilizar `Flow`?
Operaciones puntuales de una sola ejecución utilizan `suspend`; datos reactivos que cambian en el tiempo y actualizan la interfaz utilizan `Flow`.

---

# Compilación

```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

Resultado verificado:
```text
BUILD SUCCESSFUL
```

---

## Conclusión

Con LavaCampus implementé una aplicación móvil robusta en Android utilizando MVVM, Room (con migración explícita), DataStore, Jetpack Compose y Material 3, integrando tanto las funcionalidades base como las mejoras avanzadas de la Fase 2 sin alterar la separación de responsabilidades ni las reglas de negocio.
