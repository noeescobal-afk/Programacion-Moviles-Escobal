package com.tecsup.lavacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

enum class AdminTab {
    PANEL, BANDEJA, SERVICIOS, CLIENTES, AJUSTES
}

@Composable
fun AdminBottomBar(
    currentTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Panel") },
            label = { Text("Panel") },
            selected = currentTab == AdminTab.PANEL,
            onClick = { onTabSelected(AdminTab.PANEL) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pedidos") },
            label = { Text("Pedidos") },
            selected = currentTab == AdminTab.BANDEJA,
            onClick = { onTabSelected(AdminTab.BANDEJA) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.RoomService, contentDescription = "Servicios") },
            label = { Text("Servicios") },
            selected = currentTab == AdminTab.SERVICIOS,
            onClick = { onTabSelected(AdminTab.SERVICIOS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = "Clientes") },
            label = { Text("Clientes") },
            selected = currentTab == AdminTab.CLIENTES,
            onClick = { onTabSelected(AdminTab.CLIENTES) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") },
            selected = currentTab == AdminTab.AJUSTES,
            onClick = { onTabSelected(AdminTab.AJUSTES) }
        )
    }
}
