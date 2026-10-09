package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onMedicoSeleccionado: (Int) -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    var textoBusqueda by remember { mutableStateOf("") }
    val medicos = Repositorio.buscarMedicos(
        especialidadId = especialidadId,
        texto = textoBusqueda
    )

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Seleccionar médico",
                onVolverAtras = onVolverAtras
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Información de la especialidad seleccionada
            if (especialidad != null) {
                Text(
                    text = especialidad.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = especialidad.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Especialidad no encontrada",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de búsqueda de médico
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar médico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Renderizado condicional de lista o estado vacío
            if (medicos.isEmpty()) {
                MensajeListaVacia(
                    mensaje = "No se encontraron médicos"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicos) { medico ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMedicoSeleccionado(medico.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = medico.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "CMP: ${medico.cmp}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (especialidad != null) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = especialidad.nombre,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicosScreenPreview() {
    SaludPlusCitasTheme {
        MedicosScreen(
            especialidadId = 1,
            onMedicoSeleccionado = {},
            onVolverAtras = {}
        )
    }
}
