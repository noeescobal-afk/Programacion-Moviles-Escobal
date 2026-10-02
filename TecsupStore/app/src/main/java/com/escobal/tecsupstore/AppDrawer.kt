package com.escobal.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun AppDrawer(rutaActual: String?, onNavegar: (String) -> Unit) {
    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "NE",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Noe Escobal", style = MaterialTheme.typography.titleMedium)
                Text("noe@tecsup.edu.pe", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(8.dp))
        destinosDrawer.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                icon = { Icon(destino.icono, contentDescription = null) },
                selected = rutaActual == destino.ruta,
                onClick = { onNavegar(destino.ruta) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            selected = false,
            onClick = { },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}