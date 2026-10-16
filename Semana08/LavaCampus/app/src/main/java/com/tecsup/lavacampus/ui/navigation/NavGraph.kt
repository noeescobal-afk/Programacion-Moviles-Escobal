package com.tecsup.lavacampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.AdminHomeScreen
import com.tecsup.lavacampus.ui.estudiante.EstudianteHomeScreen

sealed class Destino(val ruta: String) {
    object EstudianteHome : Destino("estudiante_home")
    object AdminHome : Destino("admin_home")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    rol: Rol = Rol.ESTUDIANTE,
    onCambiarRol: (Rol) -> Unit = {}
) {
    val inicioRuta = if (rol == Rol.ESTUDIANTE) Destino.EstudianteHome.ruta else Destino.AdminHome.ruta

    NavHost(
        navController = navController,
        startDestination = inicioRuta
    ) {
        composable(Destino.EstudianteHome.ruta) {
            EstudianteHomeScreen(
                onIrAAdmin = {
                    onCambiarRol(Rol.ADMINISTRADOR)
                    navController.navigate(Destino.AdminHome.ruta)
                }
            )
        }
        composable(Destino.AdminHome.ruta) {
            AdminHomeScreen(
                onIrAEstudiante = {
                    onCambiarRol(Rol.ESTUDIANTE)
                    navController.navigate(Destino.EstudianteHome.ruta)
                }
            )
        }
    }
}
