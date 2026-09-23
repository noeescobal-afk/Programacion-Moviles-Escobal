package com.tuapp.clinicasaludplus.model

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val experience: String,
    val description: String
)

data class Appointment(
    val id: Int,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: String
)

data class MedicalRecord(
    val id: Int,
    val date: String,
    val title: String,
    val description: String
)
