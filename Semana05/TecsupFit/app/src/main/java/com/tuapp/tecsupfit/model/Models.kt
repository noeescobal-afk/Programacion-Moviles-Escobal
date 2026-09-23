package com.tuapp.tecsupfit.model

data class FitnessClass(
    val id: Int,
    val name: String,
    val instructor: String,
    val day: String,
    val time: String,
    val room: String,
    val duration: String,
    val availableSpots: Int,
    val description: String,
    val schedules: List<String>
)

data class Reservation(
    val id: Int,
    val classId: Int,
    val className: String,
    val schedule: String,
    val room: String,
    val status: String
)

data class Routine(
    val id: Int,
    val name: String,
    val duration: String,
    val description: String
)
