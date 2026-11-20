# PROMPTS.md — Semana 14, fase de mejoras

| Prompt importante | Qué respondió la IA | Qué se implementó o ajustó | Criterio técnico |
|---|---|---|---|
| «Traslada el progreso del ViewModel al Repository usando Flow y flowOn(Default), conservando MVVM» | Propuso un flujo de porcentajes cancelable | `progresoTarea()` en interfaz y Repository; colecta en ViewModel | El trabajo de segundo plano pertenece al Repository |
| «Agrega reintento automático, máximo tres intentos y esperas crecientes; no ocultes CancellationException» | Sugirió bucle con `delay` y propagación explícita de cancelación | `pedidosConReintento()` de 3 intentos | Un error no debe cerrar la app ni romper la cancelación estructurada |
| «Compara mediciones de cargas secuenciales y paralelas» | Propuso almacenar últimos tiempos en StateFlow | Listas de últimas 5 mediciones y promedios por modo | Comparar tiempos observados, no números inventados |
| «Agrega un panel innovador de eficiencia usando las medidas anteriores» | Sugirió mostrar porcentaje de ahorro | Cuarta tarjeta con barra y reducción porcentual | Visualiza el beneficio de `async` sin alterar el contrato de las tres tareas |

| «Rediseña CentroTareas como un dashboard neo-futurista, manteniendo MVVM y las funcionalidades» | Propuso un diseño con degradados azul-violeta, tarjetas modernas y estadísticas | Se rediseñó PantallaTareas.kt con paneles, colores y jerarquía visual | Mejorar la experiencia visual sin modificar la lógica de las corrutinas |
| «Corrige la visibilidad de Cancelar y agrega un selector de tema claro/oscuro con icono de luna y sol» | Propuso colores diferenciados y un estado de tema persistente | Se ajustaron los botones y se incorporó el selector de apariencia | Garantizar accesibilidad, contraste y personalización |
| «Corrige el contraste del texto Sin datos y de los indicadores de eficiencia en modo claro» | Recomendó colores dependientes del tema | Se ajustaron los indicadores para mejorar su legibilidad | Mantener contraste suficiente en ambos modos |

**Verificación pendiente:** pruebas y capturas reales en Android Studio. Las recomendaciones de IA se revisaron contra la guía de clase.
