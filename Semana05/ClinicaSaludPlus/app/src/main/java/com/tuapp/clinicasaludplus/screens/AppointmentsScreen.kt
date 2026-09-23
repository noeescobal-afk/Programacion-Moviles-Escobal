package com.tuapp.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuapp.clinicasaludplus.model.Appointment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    appointments: List<Appointment>,
    onMenuClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú de navegación"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes citas programadas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appointments) { appointment ->
                        AppointmentCard(appointment = appointment)
                    }
                }
            }

            OutlinedButton(
                onClick = onHomeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar al inicio")
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    val isConfirmed = appointment.status.equals("Confirmada", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appointment.doctorName,
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (isConfirmed) statusConfirmedContainer() else statusCompletedContainer()
                ) {
                    Text(
                        text = appointment.status,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = if (isConfirmed) statusConfirmedText() else statusCompletedText()
                    )
                }
            }

            Text(
                text = "Especialidad: ${appointment.specialty}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Fecha: ${appointment.date} — Hora: ${appointment.time}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun statusConfirmedContainer() = MaterialTheme.colorScheme.primaryContainer

@Composable
fun statusConfirmedText() = MaterialTheme.colorScheme.onPrimaryContainer

@Composable
fun statusCompletedContainer() = MaterialTheme.colorScheme.surfaceVariant

@Composable
fun statusCompletedText() = MaterialTheme.colorScheme.onSurfaceVariant
