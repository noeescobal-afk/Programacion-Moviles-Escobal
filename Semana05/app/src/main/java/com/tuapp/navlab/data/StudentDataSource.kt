package com.tuapp.navlab.data

import com.tuapp.navlab.model.Student

object StudentDataSource {
    val students = listOf(
        Student(
            id = 1,
            name = "Carlos Mendoza Silva",
            degree = "Ingeniería de Software",
            code = "U20211023",
            email = "carlos.mendoza@campus.test",
            description = "Estudiante de décimo ciclo apasionado por el desarrollo móvil y arquitecturas limpias en Android."
        ),
        Student(
            id = 2,
            name = "Lucía Fernández Ramos",
            degree = "Ingeniería de Sistemas",
            code = "U20221456",
            email = "lucia.fernandez@campus.test",
            description = "Investigadora junior en inteligencia artificial aplicada a la automatización de procesos académicos."
        ),
        Student(
            id = 3,
            name = "Diego Quispe Morales",
            degree = "Ingeniería de Ciberseguridad",
            code = "U20232890",
            email = "diego.quispe@campus.test",
            description = "Entusiasta de la seguridad informática, hacking ético y protección de datos en la nube."
        ),
        Student(
            id = 4,
            name = "Ana Sofía Torres Vega",
            degree = "Ingeniería de Software",
            code = "U20223102",
            email = "ana.torres@campus.test",
            description = "Diseñadora de interfaces UI/UX y desarrolladora frontend con enfoque en accesibilidad."
        ),
        Student(
            id = 5,
            name = "Mateo Benítez Rojas",
            degree = "Ciencia de Datos",
            code = "U20234567",
            email = "mateo.benitez@campus.test",
            description = "Especialista en análisis estadístico, machine learning y procesamiento de grandes volúmenes de datos."
        ),
        Student(
            id = 6,
            name = "Valeria Paredes Castillo",
            degree = "Ingeniería de Sistemas",
            code = "U20219874",
            email = "valeria.paredes@campus.test",
            description = "Lideresa de proyectos estudiantiles y entusiasta de metodologías ágiles Scrum."
        ),
        Student(
            id = 7,
            name = "Gabriel Ríos Navarro",
            degree = "Ingeniería de Ciberseguridad",
            code = "U20240112",
            email = "gabriel.rios@campus.test",
            description = "Auditor de redes y sistemas criptográficos con interés en sistemas distribuidos."
        ),
        Student(
            id = 8,
            name = "Camila Huamán Soto",
            degree = "Ciencia de Datos",
            code = "U20227788",
            email = "camila.huaman@campus.test",
            description = "Investigadora en procesamiento de lenguaje natural y visualización avanzada de información."
        )
    )

    fun getStudentById(id: Int): Student? {
        return students.find { it.id == id }
    }
}
