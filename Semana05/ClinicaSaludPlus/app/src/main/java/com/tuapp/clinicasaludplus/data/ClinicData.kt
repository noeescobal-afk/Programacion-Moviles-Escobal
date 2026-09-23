package com.tuapp.clinicasaludplus.data

import com.tuapp.clinicasaludplus.model.Appointment
import com.tuapp.clinicasaludplus.model.Doctor
import com.tuapp.clinicasaludplus.model.MedicalRecord

object ClinicData {
    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dra. Ana Torres",
            specialty = "Cardiología",
            rating = 4.9,
            experience = "12 años de experiencia",
            description = "Especialista en cardiología clínica y prevención de enfermedades cardiovasculares. Egresada del Hospital Clínico."
        ),
        Doctor(
            id = 2,
            name = "Dr. Luis Vega",
            specialty = "Pediatría",
            rating = 4.7,
            experience = "8 años de experiencia",
            description = "Pediatra dedicado al cuidado integral de la salud infantil, vacunación y desarrollo en la infancia."
        ),
        Doctor(
            id = 3,
            name = "Dra. Rosa Díaz",
            specialty = "Dermatología",
            rating = 4.8,
            experience = "10 años de experiencia",
            description = "Dermatóloga experta en salud cutánea, tratamientos dermatológicos y cuidado preventivo de la piel."
        )
    )

    val initialAppointments = listOf(
        Appointment(
            id = 100,
            doctorName = "Dr. Carlos Gómez",
            specialty = "Medicina General",
            date = "15/05/2026",
            time = "10:00",
            status = "Completada"
        )
    )

    val medicalHistoryRecords = listOf(
        MedicalRecord(
            id = 1,
            date = "15/05/2026",
            title = "Consulta de Medicina General",
            description = "Chequeo médico general de rutina. Presión arterial y signos vitales normales."
        ),
        MedicalRecord(
            id = 2,
            date = "10/01/2026",
            title = "Control Cardiológico Preventivo",
            description = "Electrocardiograma de control. Ritmo cardíaco sin alteraciones."
        )
    )

    val availableDates = listOf("Lun 26", "Mar 27", "Mié 28")
    val availableTimes = listOf("09:00", "10:30", "15:00")
}
