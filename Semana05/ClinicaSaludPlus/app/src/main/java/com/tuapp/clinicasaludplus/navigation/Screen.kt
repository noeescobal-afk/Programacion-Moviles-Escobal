package com.tuapp.clinicasaludplus.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DoctorDetail : Screen("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_detail/$doctorId"
    }
    object Schedule : Screen("schedule/{doctorId}") {
        fun createRoute(doctorId: Int) = "schedule/$doctorId"
    }
    object Confirmation : Screen("confirmation/{doctorId}/{date}/{time}") {
        fun createRoute(doctorId: Int, date: String, time: String) =
            "confirmation/$doctorId/$date/$time"
    }
    object Appointments : Screen("appointments")
    object MedicalHistory : Screen("medical_history")
}
