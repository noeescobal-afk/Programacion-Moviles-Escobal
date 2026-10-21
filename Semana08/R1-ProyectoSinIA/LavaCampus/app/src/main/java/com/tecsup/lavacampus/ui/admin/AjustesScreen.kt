package com.tecsup.lavacampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.ui.components.AdminBottomBar
import com.tecsup.lavacampus.ui.components.AdminTab
import com.tecsup.lavacampus.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    usuario: Usuario,
    modoOscuro: Boolean,
    onModoOscuroChanged: (Boolean) -> Unit,
    avisos: Boolean,
    onAvisosChanged: (Boolean) -> Unit,
    recibirPedidos: Boolean,
    onRecibirPedidosChanged: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onLogout: () -> Unit,
    onTabSelected: (AdminTab) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Ajustes Administrador")
        },
        bottomBar = {
            AdminBottomBar(
                currentTab = AdminTab.AJUSTES,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Información de Sesión",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Nombre: ${usuario.nombre}")
                    Text("Correo: ${usuario.correo}")
                    Text("Rol: ${usuario.rol}")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Switch(checked = modoOscuro, onCheckedChange = onModoOscuroChanged)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notificaciones (Avisos)", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Switch(checked = avisos, onCheckedChange = onAvisosChanged)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Recibir pedidos", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Permitir o bloquear la creación de nuevos pedidos en el sistema",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(checked = recibirPedidos, onCheckedChange = onRecibirPedidosChanged)
                    }
                }
            }

            Button(
                onClick = onCambiarPerfil,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Cambiar de perfil")
            }

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
