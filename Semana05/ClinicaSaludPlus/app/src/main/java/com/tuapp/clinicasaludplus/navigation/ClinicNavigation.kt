package com.tuapp.clinicasaludplus.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuapp.clinicasaludplus.data.ClinicData
import com.tuapp.clinicasaludplus.model.Appointment
import com.tuapp.clinicasaludplus.screens.*
import kotlinx.coroutines.launch

@Composable
fun ClinicNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val appointments = remember {
        mutableStateListOf<Appointment>().apply {
            addAll(ClinicData.initialAppointments)
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Clínica Salud+",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Mis citas") },
                    label = { Text("Mis citas") },
                    selected = currentRoute == Screen.Appointments.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Appointments.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial médico") },
                    label = { Text("Historial médico") },
                    selected = currentRoute == Screen.MedicalHistory.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MedicalHistory.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onDoctorClick = { doctorId ->
                        navController.navigate(Screen.DoctorDetail.createRoute(doctorId))
                    }
                )
            }

            composable(
                route = Screen.DoctorDetail.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                DoctorDetailScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onScheduleClick = { id ->
                        navController.navigate(Screen.Schedule.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.Schedule.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                ScheduleScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onConfirmAppointment = { newAppointment ->
                        appointments.add(newAppointment)
                        navController.navigate(
                            Screen.Confirmation.createRoute(
                                doctorId = doctorId,
                                date = newAppointment.date,
                                time = newAppointment.time
                            )
                        ) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                )
            }

            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument("doctorId") { type = NavType.IntType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                val date = backStackEntry.arguments?.getString("date") ?: ""
                val time = backStackEntry.arguments?.getString("time") ?: ""
                val doctor = ClinicData.doctors.find { it.id == doctorId }

                ConfirmationScreen(
                    doctorName = doctor?.name ?: "Médico",
                    specialty = doctor?.specialty ?: "Especialidad",
                    date = date,
                    time = time,
                    onViewAppointmentsClick = {
                        navController.navigate(Screen.Appointments.route) {
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

            composable(Screen.Appointments.route) {
                AppointmentsScreen(
                    appointments = appointments,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.MedicalHistory.route) {
                MedicalHistoryScreen(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
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
