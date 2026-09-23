package com.tuapp.tecsupfit.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuapp.tecsupfit.components.FitBottomBar
import com.tuapp.tecsupfit.data.FitnessData
import com.tuapp.tecsupfit.screens.ClassDetailScreen
import com.tuapp.tecsupfit.screens.ConfirmationScreen
import com.tuapp.tecsupfit.screens.HomeScreen
import com.tuapp.tecsupfit.screens.ProfileScreen
import com.tuapp.tecsupfit.screens.ReservationsScreen
import com.tuapp.tecsupfit.screens.RoutinesScreen

@Composable
fun FitNavigation() {
    val navController = rememberNavController()
    val reservations = remember {
        mutableStateListOf(FitnessData.initialReservation)
    }

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Reservations.route,
        Screen.Routines.route,
        Screen.Profile.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                FitBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    classes = FitnessData.fitnessClasses,
                    onClassClick = { classId ->
                        navController.navigate(Screen.ClassDetail.createRoute(classId))
                    }
                )
            }
            composable(Screen.Reservations.route) {
                ReservationsScreen(
                    reservations = reservations
                )
            }
            composable(Screen.Routines.route) {
                RoutinesScreen(
                    routines = FitnessData.routines
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    reservations = reservations
                )
            }
            composable(
                route = Screen.ClassDetail.route,
                arguments = listOf(navArgument("classId") { type = NavType.IntType })
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getInt("classId") ?: 0
                ClassDetailScreen(
                    classId = classId,
                    classes = FitnessData.fitnessClasses,
                    onBackClick = { navController.popBackStack() },
                    onContinueClick = { id, scheduleIndex ->
                        navController.navigate(Screen.Confirmation.createRoute(id, scheduleIndex))
                    },
                    onHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument("classId") { type = NavType.IntType },
                    navArgument("scheduleIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getInt("classId") ?: 0
                val scheduleIndex = backStackEntry.arguments?.getInt("scheduleIndex") ?: 0
                ConfirmationScreen(
                    classId = classId,
                    scheduleIndex = scheduleIndex,
                    classes = FitnessData.fitnessClasses,
                    reservations = reservations,
                    onViewReservationsClick = {
                        navController.navigate(Screen.Reservations.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
