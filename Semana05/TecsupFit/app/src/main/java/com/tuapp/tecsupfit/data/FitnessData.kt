package com.tuapp.tecsupfit.data

import com.tuapp.tecsupfit.model.FitnessClass
import com.tuapp.tecsupfit.model.Reservation
import com.tuapp.tecsupfit.model.Routine

object FitnessData {
    val fitnessClasses = listOf(
        FitnessClass(
            id = 1,
            name = "Yoga funcional",
            instructor = "Carla Mendoza",
            day = "Hoy",
            time = "07:00",
            room = "Sala 2",
            duration = "60 min",
            availableSpots = 8,
            description = "Sesión de movilidad, equilibrio y respiración para todos los niveles.",
            schedules = listOf("Hoy - 07:00", "Hoy - 09:00", "Miércoles - 08:00")
        ),
        FitnessClass(
            id = 2,
            name = "Cross Training",
            instructor = "Diego Ramos",
            day = "Hoy",
            time = "18:00",
            room = "Sala 1",
            duration = "45 min",
            availableSpots = 5,
            description = "Entrenamiento de fuerza y resistencia mediante circuitos funcionales.",
            schedules = listOf("Hoy - 18:00", "Jueves - 17:00", "Sábado - 10:00")
        ),
        FitnessClass(
            id = 3,
            name = "Spinning",
            instructor = "Lucía Torres",
            day = "Viernes",
            time = "19:30",
            room = "Sala 3",
            duration = "50 min",
            availableSpots = 10,
            description = "Clase cardiovascular en bicicleta estacionaria con intensidad progresiva.",
            schedules = listOf("Martes - 19:30", "Viernes - 18:30", "Sábado - 11:30")
        )
    )

    val routines = listOf(
        Routine(
            id = 1,
            name = "Rutina de movilidad",
            duration = "15 minutos",
            description = "Movimientos suaves para mejorar la flexibilidad y preparar el cuerpo."
        ),
        Routine(
            id = 2,
            name = "Fuerza básica",
            duration = "25 minutos",
            description = "Ejercicios básicos para fortalecer piernas, brazos y zona media."
        ),
        Routine(
            id = 3,
            name = "Cardio en casa",
            duration = "20 minutos",
            description = "Secuencia cardiovascular que puede realizarse sin equipamiento."
        )
    )

    val initialReservation = Reservation(
        id = 1,
        classId = 0,
        className = "Entrenamiento funcional",
        schedule = "15/05/2026 - 10:00",
        room = "Sala 1",
        status = "Completada"
    )
}
