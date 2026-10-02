package com.escobal.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DestinoDrawer(val ruta: String, val titulo: String, val icono: ImageVector)

val destinosDrawer = listOf(
    DestinoDrawer("inicio", "Inicio", Icons.Default.Home),
    DestinoDrawer("pedidos", "Mis pedidos", Icons.Default.ShoppingCart),
    DestinoDrawer("favoritos", "Favoritos", Icons.Default.Favorite),
    DestinoDrawer("perfil", "Perfil", Icons.Default.Person)
)

@Composable
fun AppDrawer(onNavegar: (String) -> Unit) {
    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        destinosDrawer.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                icon = { Icon(destino.icono, contentDescription = null) },
                selected = false,
                onClick = { onNavegar(destino.ruta) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}