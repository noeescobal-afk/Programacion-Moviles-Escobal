\# RegistroNotas



Aplicación móvil desarrollada en Kotlin utilizando Jetpack Compose para registrar notas académicas y calcular el promedio ponderado de un ciclo universitario.



\---



\## Descripción



RegistroNotas es una aplicación Android que permite ingresar las notas de diferentes cursos mediante controles interactivos, calcular el promedio ponderado considerando los porcentajes asignados a cada curso y mostrar una observación final del rendimiento académico.



La aplicación utiliza componentes de Jetpack Compose como:



\- Slider

\- Switch

\- Checkbox

\- Button

\- Card



para construir una interfaz dinámica y reactiva.



\---



\## Cursos evaluados



La aplicación considera los siguientes cursos:



| Curso | Peso |

|---|---|

| Fundamentos de Programación | 20% |

| Programación Orientada a Objetos | 25% |

| Programación en Móviles | 30% |

| Base de Datos | 25% |



\---



\## Funcionalidades implementadas



\### 1. Selección de notas mediante Slider



Cada curso cuenta con un Slider que permite seleccionar una nota entre:



```

0 - 20

```



Los valores son enteros y se actualizan dinámicamente en la interfaz.



\---



\### 2. Redondeo del promedio final



Se implementó un Switch que permite seleccionar si el promedio final será mostrado redondeado.



Cuando está activado:



```

Promedio final: 15

```



Cuando está desactivado:



```

Promedio final: 14.55

```



\---



\### 3. Confirmación de notas



Se agregó un Checkbox:



```

Confirmo que las notas son correctas

```



El botón de cálculo permanece deshabilitado hasta que el usuario confirme las notas ingresadas.



\---



\### 4. Cálculo del promedio ponderado



El promedio se calcula utilizando los pesos establecidos:



```

Promedio ponderado =



(Fundamentos × 0.20) +

(POO × 0.25) +

(Móviles × 0.30) +

(Base de Datos × 0.25)

```



\---



\### 5. Clasificación del rendimiento académico



La aplicación genera una observación según el promedio final obtenido:



| Promedio | Observación |

|---|---|

| 17 - 20 | EXCELENTE |

| 13 - 16 | APROBADO |

| 10 - 12 | EN RECUPERACIÓN |

| 0 - 9 | DESAPROBADO |



\---



\## Tecnologías utilizadas



\- Kotlin

\- Android Studio

\- Jetpack Compose

\- Material 3



\---



\## Estructura del proyecto



```

RegistroNotas

│

├── app

│   └── src

│       └── main

│           └── java

│               └── com.escobal.registronotas

│                   └── MainActivity.kt

│

├── evidencias

│   ├── C3-sliders-notas.png

│   ├── C4-switch-checkbox.png

│   ├── C5-calculo-promedio.png

│   └── C6-resultado-final.png

│

└── README.md

```



\---



\## Evidencias del desarrollo



Las evidencias muestran el desarrollo progresivo de la aplicación:



\### C3 - Sliders de notas



Implementación de los controles Slider para seleccionar las notas de cada curso.



\### C4 - Switch y Checkbox



Implementación del Switch para redondear el promedio y Checkbox para confirmar las notas.



\### C5 - Cálculo del promedio



Implementación del cálculo del promedio ponderado y habilitación del botón de cálculo.



\### C6 - Resultado final



Implementación de la clasificación del rendimiento mediante la observación:



\- EXCELENTE

\- APROBADO

\- EN RECUPERACIÓN

\- DESAPROBADO



\---



\## Autor



Noe Oswaldo Escobal

\---



\## Conclusión



La aplicación RegistroNotas permite gestionar notas académicas mediante una interfaz interactiva desarrollada con Jetpack Compose, aplicando manejo de estados, controles modernos y cálculo automático del promedio ponderado.

