package com.saludplus.citas.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.components.SaludPlusDrawer
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
import com.saludplus.citas.ui.screens.doctores.DoctoresScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen
import com.saludplus.citas.ui.screens.sedes.SedesScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route ?: Rutas.SPLASH

    SaludPlusDrawer(
        drawerState = drawerState,
        rutaActual = rutaActual,
        onNavegarSedes = {
            navController.navigate(Rutas.SEDES) {
                launchSingleTop = true
            }
        },
        onNavegarDoctores = {
            navController.navigate(Rutas.DOCTORES) {
                launchSingleTop = true
            }
        },
        onNavegarAgenda = {
            navController.navigate(Rutas.MIS_CITAS) {
                launchSingleTop = true
            }
        },
        onCerrarSesionConfirmado = {
            navController.navigate(Rutas.LOGIN) {
                popUpTo(0) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    ) {
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
                        navController.navigate(Rutas.SEDES)
                    },
                    onSedes = {
                        navController.navigate(Rutas.SEDES) {
                            launchSingleTop = true
                        }
                    },
                    onDoctores = {
                        navController.navigate(Rutas.DOCTORES) {
                            launchSingleTop = true
                        }
                    },
                    onEspecialidadSeleccionada = { _ ->
                        navController.navigate(Rutas.SEDES)
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
                    },
                    onAbrirMenu = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }

            // 6. Sedes (Sección principal)
            composable(Rutas.SEDES) {
                SedesScreen(
                    onSedeSeleccionada = { sedeId ->
                        navController.navigate(Rutas.medicosSede(sedeId))
                    },
                    onInicio = {
                        navController.navigate(Rutas.INICIO) {
                            launchSingleTop = true
                        }
                    },
                    onDoctores = {
                        navController.navigate(Rutas.DOCTORES) {
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
                    onAbrirMenu = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }

            // 7. Doctores de una Sede
            composable(
                route = Rutas.MEDICOS_SEDE,
                arguments = listOf(
                    navArgument("sedeId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val sedeId = backStackEntry.arguments?.getInt("sedeId") ?: 1

                MedicosScreen(
                    sedeId = sedeId,
                    onMedicoSeleccionado = { medicoId ->
                        navController.navigate(Rutas.fechaHora(medicoId))
                    },
                    onVolverAtras = {
                        navController.popBackStack()
                    }
                )
            }

            // 8. Catálogo General de Doctores
            composable(Rutas.DOCTORES) {
                DoctoresScreen(
                    onAgendarCitaMedico = { _, _, medicoId ->
                        navController.navigate(Rutas.fechaHora(medicoId))
                    },
                    onInicio = {
                        navController.navigate(Rutas.INICIO) {
                            launchSingleTop = true
                        }
                    },
                    onSedes = {
                        navController.navigate(Rutas.SEDES) {
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
                    onAbrirMenu = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }

            // 9. Especialidades
            composable(Rutas.ESPECIALIDADES) {
                EspecialidadesScreen(
                    onEspecialidadSeleccionada = { _ ->
                        navController.navigate(Rutas.SEDES)
                    },
                    onVolverAtras = {
                        navController.popBackStack()
                    }
                )
            }

            // 10. Médicos por Especialidad
            composable(
                route = Rutas.MEDICOS,
                arguments = listOf(
                    navArgument("especialidadId") {
                        type = NavType.IntType
                    }
                )
            ) {
                MedicosScreen(
                    sedeId = 1,
                    onMedicoSeleccionado = { medicoId ->
                        navController.navigate(Rutas.fechaHora(medicoId))
                    },
                    onVolverAtras = {
                        navController.popBackStack()
                    }
                )
            }

            // 11. Fecha y Hora
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

            // 12. Confirmar Cita
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
                        navController.navigate(Rutas.citaExitosa(citaId)) {
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

            // 13. Cita Exitosa
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
                    },
                    onVolverSedes = {
                        navController.navigate(Rutas.SEDES) {
                            popUpTo(Rutas.INICIO) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // 14. Mis Citas (Agenda)
            composable(Rutas.MIS_CITAS) {
                MisCitasScreen(
                    onCitaSeleccionada = { citaId ->
                        navController.navigate(Rutas.detalleCita(citaId))
                    },
                    onAgendarCita = {
                        navController.navigate(Rutas.SEDES)
                    },
                    onInicio = {
                        navController.navigate(Rutas.INICIO) {
                            launchSingleTop = true
                        }
                    },
                    onSedes = {
                        navController.navigate(Rutas.SEDES) {
                            launchSingleTop = true
                        }
                    },
                    onDoctores = {
                        navController.navigate(Rutas.DOCTORES) {
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
                    onAbrirMenu = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }

            // 15. Detalle de Cita
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

            // 16. Perfil
            composable(Rutas.PERFIL) {
                PerfilScreen(
                    onInicio = {
                        navController.navigate(Rutas.INICIO) {
                            launchSingleTop = true
                        }
                    },
                    onSedes = {
                        navController.navigate(Rutas.SEDES) {
                            launchSingleTop = true
                        }
                    },
                    onDoctores = {
                        navController.navigate(Rutas.DOCTORES) {
                            launchSingleTop = true
                        }
                    },
                    onResultados = {
                        navController.navigate(Rutas.RESULTADOS) {
                            launchSingleTop = true
                        }
                    },
                    onCerrarSesion = {
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onAbrirMenu = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }

            // 17. Resultados
            composable(Rutas.RESULTADOS) {
                ResultadosScreen(
                    onInicio = {
                        navController.navigate(Rutas.INICIO) {
                            launchSingleTop = true
                        }
                    },
                    onSedes = {
                        navController.navigate(Rutas.SEDES) {
                            launchSingleTop = true
                        }
                    },
                    onDoctores = {
                        navController.navigate(Rutas.DOCTORES) {
                            launchSingleTop = true
                        }
                    },
                    onPerfil = {
                        navController.navigate(Rutas.PERFIL) {
                            launchSingleTop = true
                        }
                    },
                    onAbrirMenu = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }

            // 18. Notificaciones
            composable(Rutas.NOTIFICACIONES) {
                NotificacionesScreen(
                    onVolverAtras = {
                        navController.popBackStack()
                    },
                    onCitaSeleccionada = { citaId ->
                        navController.navigate(Rutas.detalleCita(citaId))
                    }
                )
            }
        }
    }
}
