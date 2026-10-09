package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

data class ResultadoMedico(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val descripcion: String
)

private val resultadosEjemplo = listOf(
    ResultadoMedico(
        id = 1,
        titulo = "Análisis de sangre completo",
        fecha = "05/10/2026",
        descripcion = "Hemograma completo dentro de los rangos normales. Glucosa y colesterol adecuados."
    ),
    ResultadoMedico(
        id = 2,
        titulo = "Radiografía de tórax",
        fecha = "28/09/2026",
        descripcion = "Campos pulmonares claros, sin evidencias de lesiones activas ni alteración cardiopulmonar."
    ),
    ResultadoMedico(
        id = 3,
        titulo = "Perfil lipídico",
        fecha = "15/09/2026",
        descripcion = "Triglicéridos y HDL en parámetros óptimos. Se recomienda mantener dieta balanceada."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadosScreen(
    onInicio: () -> Unit,
    onMisCitas: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resultados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onInicio,
                    icon = { Text(text = "🏠", fontSize = 20.sp) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onMisCitas,
                    icon = { Text(text = "📅", fontSize = 20.sp) },
                    label = { Text("Mis citas") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text(text = "📋", fontSize = 20.sp) },
                    label = { Text("Resultados") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPerfil,
                    icon = { Text(text = "👤", fontSize = 20.sp) },
                    label = { Text("Perfil") }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(resultadosEjemplo) { resultado ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = resultado.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = resultado.fecha,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = resultado.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultadosScreenPreview() {
    SaludPlusCitasTheme {
        ResultadosScreen(
            onInicio = {},
            onMisCitas = {},
            onPerfil = {}
        )
    }
}
