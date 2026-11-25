# PROMPTS · MuroCampus

Laboratorio 09 de Programación en Móviles. Las tres mejoras están en la rama `mejora-ia-semana09` del repositorio del curso. El nombre distingue esta entrega de la rama `mejora-ia` de Semana05.

## Registro de prompts y decisiones

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **Búsqueda por título.** Agrega un campo «Buscar por título» en `PantallaLista.kt` y guarda la consulta en `PostsViewModel.kt`. Filtra las publicaciones ya descargadas sin distinguir mayúsculas de minúsculas. Conserva el filtro por usuario, el detalle y el mensaje «Sin publicaciones». | Expuso la consulta como `StateFlow` de solo lectura, filtró los títulos sobre `postsOriginales` con `ignoreCase = true` y conectó el campo al ViewModel. | Conservé la búsqueda local y su combinación con el chip de usuario. Probé consultas con y sin coincidencias y limpié el campo para recuperar la lista del usuario seleccionado. | Escribir en el buscador no requiere volver a consultar el servicio y debe respetar la selección activa. |
| **Encabezado Accept.** Agrega en `RetrofitClient.kt` un interceptor de OkHttp que envíe `Accept: application/json` en cada solicitud. Regístralo antes del interceptor de logging y conserva la configuración de Retrofit. | Agregó el interceptor, reconstruyó la solicitud con `addHeader` y lo colocó antes de `HttpLoggingInterceptor`. | Conservé el encabezado centralizado en `RetrofitClient.kt` y verifiqué el valor enviado en Logcat. | Un solo interceptor aplica el encabezado a las solicitudes del cliente. El orden permite verlo en el registro. |
| **Eliminar una publicación.** Agrega `@DELETE("posts/{id}")` con `Response<Unit>`, encapsula la respuesta HTTP como `Result<Unit>` y agrega confirmación en el detalle. Al tener éxito, vuelve a la lista y retira el post localmente. Si falla, muestra el error en el detalle. | Agregó el endpoint y comprobó `response.isSuccessful` en el repositorio. Reutilizó `safeCall` y la propagación de la cancelación. Añadió el estado de eliminación al ViewModel y conectó el diálogo y la navegación. | Conservé la confirmación previa y la retirada local tras una respuesta exitosa. La verificación en el emulador cubrió el flujo de éxito. | El diálogo requiere confirmar la acción. El repositorio interpreta la respuesta HTTP y el ViewModel actualiza la lista después del éxito. JSONPlaceholder simula la eliminación. |

## Evidencias

| Mejora | Resultado observado | Capturas |
|---|---|---|
| Búsqueda | Con Usuario 2 activo, `LAUDANTIUM` encontró un título en minúsculas. Al borrar la consulta reaparecieron las tarjetas del mismo usuario. | [Coincidencia](PROMPTS-assets/busqueda.png) · [Respuesta de Gemini](PROMPTS-assets/gemini-busqueda.png) |
| Encabezado Accept | `GET /posts` incluyó `Accept: application/json` y respondió HTTP 200. | [Logcat](PROMPTS-assets/accept.png) · [Respuesta de Gemini](PROMPTS-assets/gemini-accept.png) |
| Eliminar | Se mostró el diálogo de confirmación. `DELETE /posts/1` respondió HTTP 200 y la publicación dejó de aparecer en la lista. | [Confirmación](PROMPTS-assets/eliminar-confirmacion.png) · [HTTP 200](PROMPTS-assets/eliminar-200.png) · [Lista actualizada](PROMPTS-assets/eliminar-lista.png) · [Respuesta de Gemini](PROMPTS-assets/gemini-eliminar.png) |
