package com.tecsup.lavacampus.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.AdminBottomBar
import com.tecsup.lavacampus.ui.components.AdminTab
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.viewmodel.BandejaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandejaScreen(
    viewModel: BandejaViewModel,
    onVerGestion: (Int) -> Unit,
    onTabSelected: (AdminTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Bandeja de Pedidos")
        },
        bottomBar = {
            AdminBottomBar(
                currentTab = AdminTab.BANDEJA,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        if (uiState.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.pedidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay pedidos en el sistema",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.pedidos) { pedidoConDetalle ->
                    PedidoCard(
                        pedidoConDetalle = pedidoConDetalle,
                        onClick = { onVerGestion(pedidoConDetalle.pedido.id) }
                    )
                }
            }
        }
    }
}
