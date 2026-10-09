package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.DetalleCitaScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH,
        modifier = modifier
    ) {
        // 1. Splash
        composable(Rutas.SPLASH) {
            SplashScreen(
                onCrearCuenta = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN)
                }
            )
        }

        // 2. Registro
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN)
                },
                onVerTerminos = {
                    navController.navigate(Rutas.TERMINOS)
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Login
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onCrearCuenta = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 4. Términos
        composable(Rutas.TERMINOS) {
            TerminosScreen(
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 5. Inicio (Home)
        composable(Rutas.INICIO) {
            HomeScreen(
                onAgendarCita = {
                    navController.navigate(Rutas.ESPECIALIDADES)
                },
                onEspecialidadSeleccionada = { especialidadId ->
                    navController.navigate(
                        Rutas.medicos(especialidadId)
                    )
                },
                onMisCitas = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        launchSingleTop = true
                    }
                },
                onResultados = {
                    navController.navigate(Rutas.RESULTADOS) {
                        launchSingleTop = true
                    }
                },
                onPerfil = {
                    navController.navigate(Rutas.PERFIL) {
                        launchSingleTop = true
                    }
                },
                onNotificaciones = {
                    navController.navigate(Rutas.NOTIFICACIONES)
                }
            )
        }

        // 6. Especialidades
        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
                onEspecialidadSeleccionada = { especialidadId ->
                    navController.navigate(
                        Rutas.medicos(especialidadId)
                    )
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 7. Médicos por Especialidad
        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(
                navArgument("especialidadId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getInt("especialidadId")
                ?: return@composable

            MedicosScreen(
                especialidadId = especialidadId,
                onMedicoSeleccionado = { medicoId ->
                    navController.navigate(
                        Rutas.fechaHora(medicoId)
                    )
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 8. Fecha y Hora
        composable(
            route = Rutas.FECHA_HORA,
            arguments = listOf(
                navArgument("medicoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId")
                ?: return@composable

            FechaHoraScreen(
                medicoId = medicoId,
                onContinuar = { fecha, hora ->
                    navController.navigate(
                        Rutas.confirmarCita(
                            medicoId = medicoId,
                            fecha = fecha,
                            hora = hora
                        )
                    )
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 9. Confirmar Cita
        composable(
            route = Rutas.CONFIRMAR_CITA,
            arguments = listOf(
                navArgument("medicoId") {
                    type = NavType.IntType
                },
                navArgument("fecha") {
                    type = NavType.StringType
                },
                navArgument("hora") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId")
                ?: return@composable
            val fecha = backStackEntry.arguments?.getString("fecha")
                ?: return@composable
            val hora = backStackEntry.arguments?.getString("hora")
                ?: return@composable

            ConfirmarCitaScreen(
                medicoId = medicoId,
                fecha = fecha,
                hora = hora,
                onCitaConfirmada = { citaId ->
                    navController.navigate(
                        Rutas.citaExitosa(citaId)
                    ) {
                        popUpTo(Rutas.INICIO) {
                            inclusive = false
                        }
                    }
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        // 10. Cita Exitosa
        composable(
            route = Rutas.CITA_EXITOSA,
            arguments = listOf(
                navArgument("citaId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId")
                ?: return@composable

            CitaExitosaScreen(
                citaId = citaId,
                onVerMisCitas = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        popUpTo(Rutas.INICIO) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onIrInicio = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 11. Mis Citas
        composable(Rutas.MIS_CITAS) {
            MisCitasScreen(
                onCitaSeleccionada = { citaId ->
                    navController.navigate(
                        Rutas.detalleCita(citaId)
                    )
                },
                onAgendarCita = {
                    navController.navigate(Rutas.ESPECIALIDADES)
                },
                onInicio = {
                    navController.navigate(Rutas.INICIO) {
                        launchSingleTop = true
                    }
                },
                onResultados = {
                    navController.navigate(Rutas.RESULTADOS) {
                        launchSingleTop = true
                    }
                },
                onPerfil = {
                    navController.navigate(Rutas.PERFIL) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 12. Detalle de Cita
        composable(
            route = Rutas.DETALLE_CITA,
            arguments = listOf(
                navArgument("citaId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId")
                ?: return@composable

            DetalleCitaScreen(
                citaId = citaId,
                onVolverAtras = {
                    navController.popBackStack()
                },
                onCitaCancelada = {
                    navController.popBackStack()
                }
            )
        }

        // 13. Perfil
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onInicio = {
                    navController.navigate(Rutas.INICIO) {
                        launchSingleTop = true
                    }
                },
                onMisCitas = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        launchSingleTop = true
                    }
                },
                onResultados = {
                    navController.navigate(Rutas.RESULTADOS) {
                        launchSingleTop = true
                    }
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 14. Resultados
        composable(Rutas.RESULTADOS) {
            ResultadosScreen(
                onInicio = {
                    navController.navigate(Rutas.INICIO) {
                        launchSingleTop = true
                    }
                },
                onMisCitas = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        launchSingleTop = true
                    }
                },
                onPerfil = {
                    navController.navigate(Rutas.PERFIL) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 15. Notificaciones
        composable(Rutas.NOTIFICACIONES) {
            NotificacionesScreen(
                onVolverAtras = {
                    navController.popBackStack()
                },
                onCitaSeleccionada = { citaId ->
                    navController.navigate(
                        Rutas.detalleCita(citaId)
                    )
                }
            )
        }
    }
}
