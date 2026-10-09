package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.DoctorCard
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulMedicos = Color(0xFF246BFD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicosScreen(
    sedeId: Int = 1,
    onMedicoSeleccionado: (Int) -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sede = Repositorio.obtenerSede(sedeId)
    var textoBusqueda by remember { mutableStateOf("") }
    
    val medicos = Repositorio.medicosPorSede(sedeId).filter { medico ->
        textoBusqueda.isBlank() || medico.nombre.contains(textoBusqueda.trim(), ignoreCase = true)
    }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Médicos",
                onVolverAtras = onVolverAtras
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Médicos disponibles",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (sede != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        tint = AzulMedicos,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = sede.nombre,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulMedicos
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                placeholder = { Text("Buscar médico por nombre...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (medicos.isEmpty()) {
                MensajeListaVacia(
                    mensaje = "No hay médicos disponibles para esta sede"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicos, key = { it.id }) { medico ->
                        DoctorCard(
                            medico = medico,
                            onVerHorarios = { onMedicoSeleccionado(medico.id) }
                        )
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
            sedeId = 1,
            onMedicoSeleccionado = {},
            onVolverAtras = {}
        )
    }
}
