package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import kotlinx.coroutines.launch

private val AzulDrawer = Color(0xFF246BFD)
private val AzulClaroDrawer = Color(0xFFE8F0FF)

@Composable
fun SaludPlusDrawer(
    drawerState: DrawerState,
    rutaActual: String,
    onNavegarSedes: () -> Unit,
    onNavegarDoctores: () -> Unit,
    onNavegarAgenda: () -> Unit,
    onCerrarSesionConfirmado: () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var mostrarDialogoLogout by remember { mutableStateOf(false) }
    val usuario = Repositorio.usuarioActual

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {
                // Cabecera del Drawer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AzulClaroDrawer)
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(AzulDrawer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.HealthAndSafety,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "SaludPlus",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AzulDrawer
                            )
                            Text(
                                text = "Gestión de Citas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (usuario != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = usuario.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = usuario.correo,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                // Opciones del Menú
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Place, contentDescription = null) },
                    label = { Text("Sedes", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == "sedes",
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavegarSedes()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.MedicalServices, contentDescription = null) },
                    label = { Text("Doctor", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == "doctores",
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavegarDoctores()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null) },
                    label = { Text("Agenda", fontWeight = FontWeight.SemiBold) },
                    selected = rutaActual == "mis_citas",
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavegarAgenda()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider()

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    label = {
                        Text(
                            "Cerrar sesión",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        mostrarDialogoLogout = true
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        content = content
    )

    // Diálogo de confirmación de cierre de sesión
    if (mostrarDialogoLogout) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoLogout = false
            },
            title = {
                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cerrar sesión?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoLogout = false
                        Repositorio.cerrarSesion()
                        onCerrarSesionConfirmado()
                    }
                ) {
                    Text(
                        text = "Cerrar sesión",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoLogout = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
