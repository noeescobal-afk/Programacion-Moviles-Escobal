package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val AzulPrincipal = Color(0xFF246BFD)

@Composable
fun SaludPlusBottomBar(
    rutaActual: String,
    onNavegarInicio: () -> Unit,
    onNavegarSedes: () -> Unit,
    onNavegarDoctores: () -> Unit,
    onNavegarResultados: () -> Unit,
    onNavegarPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        // 1. Inicio
        NavigationBarItem(
            selected = rutaActual == "inicio",
            onClick = onNavegarInicio,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Inicio"
                )
            },
            label = {
                Text(
                    text = "Inicio",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (rutaActual == "inicio") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulPrincipal,
                selectedTextColor = AzulPrincipal,
                indicatorColor = Color(0xFFEAF1FF)
            )
        )

        // 2. Sedes
        NavigationBarItem(
            selected = rutaActual == "sedes",
            onClick = onNavegarSedes,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Sedes"
                )
            },
            label = {
                Text(
                    text = "Sedes",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (rutaActual == "sedes") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulPrincipal,
                selectedTextColor = AzulPrincipal,
                indicatorColor = Color(0xFFEAF1FF)
            )
        )

        // 3. Doctores
        NavigationBarItem(
            selected = rutaActual == "doctores",
            onClick = onNavegarDoctores,
            icon = {
                Icon(
                    imageVector = Icons.Filled.MedicalServices,
                    contentDescription = "Doctores"
                )
            },
            label = {
                Text(
                    text = "Doctores",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (rutaActual == "doctores") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulPrincipal,
                selectedTextColor = AzulPrincipal,
                indicatorColor = Color(0xFFEAF1FF)
            )
        )

        // 4. Resultados
        NavigationBarItem(
            selected = rutaActual == "resultados",
            onClick = onNavegarResultados,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Assignment,
                    contentDescription = "Resultados"
                )
            },
            label = {
                Text(
                    text = "Resultados",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (rutaActual == "resultados") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulPrincipal,
                selectedTextColor = AzulPrincipal,
                indicatorColor = Color(0xFFEAF1FF)
            )
        )

        // 5. Perfil
        NavigationBarItem(
            selected = rutaActual == "perfil",
            onClick = onNavegarPerfil,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Perfil"
                )
            },
            label = {
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (rutaActual == "perfil") FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulPrincipal,
                selectedTextColor = AzulPrincipal,
                indicatorColor = Color(0xFFEAF1FF)
            )
        )
    }
}
