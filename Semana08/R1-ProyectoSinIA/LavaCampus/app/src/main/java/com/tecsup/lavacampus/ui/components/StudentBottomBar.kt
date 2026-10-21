package com.tecsup.lavacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

enum class StudentTab {
    INICIO, PEDIDOS, TARIFAS, PERFIL
}

@Composable
fun StudentBottomBar(
    currentTab: StudentTab,
    onTabSelected: (StudentTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentTab == StudentTab.INICIO,
            onClick = { onTabSelected(StudentTab.INICIO) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Pedidos") },
            label = { Text("Pedidos") },
            selected = currentTab == StudentTab.PEDIDOS,
            onClick = { onTabSelected(StudentTab.PEDIDOS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Tarifas") },
            label = { Text("Tarifas") },
            selected = currentTab == StudentTab.TARIFAS,
            onClick = { onTabSelected(StudentTab.TARIFAS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentTab == StudentTab.PERFIL,
            onClick = { onTabSelected(StudentTab.PERFIL) }
        )
    }
}
