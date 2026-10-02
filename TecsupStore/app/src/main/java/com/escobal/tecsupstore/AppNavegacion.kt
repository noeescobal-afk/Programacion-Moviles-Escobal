package com.escobal.tecsupstore

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("TECSUP Store") }) }
    ) { padding ->
        NavHost(navController, startDestination = "inicio", modifier = Modifier.padding(padding)) {
            composable("inicio") { PantallaInicio() }
            composable("pedidos") { PantallaSimple("Mis pedidos") }
            composable("favoritos") { PantallaSimple("Favoritos") }
            composable("perfil") { PantallaSimple("Perfil") }
        }
    }
}