package com.tuapp.tecsupfit.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Reservations : Screen("reservations")
    object Routines : Screen("routines")
    object Profile : Screen("profile")

    object ClassDetail : Screen("class_detail/{classId}") {
        fun createRoute(classId: Int) = "class_detail/$classId"
    }

    object Confirmation : Screen("confirmation/{classId}/{scheduleIndex}") {
        fun createRoute(classId: Int, scheduleIndex: Int) = "confirmation/$classId/$scheduleIndex"
    }
}
