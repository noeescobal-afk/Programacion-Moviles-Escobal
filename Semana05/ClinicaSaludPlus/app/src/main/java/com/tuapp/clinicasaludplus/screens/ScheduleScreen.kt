package com.tuapp.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuapp.clinicasaludplus.data.ClinicData
import com.tuapp.clinicasaludplus.model.Appointment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    doctorId: Int,
    onBackClick: () -> Unit,
    onConfirmAppointment: (Appointment) -> Unit
) {
    val doctor = ClinicData.doctors.find { id -> id.id == doctorId }

    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (doctor == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text(
                        text = "Médico no encontrado",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("Regresar")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Doctor(a): ${doctor.name}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Especialidad: ${doctor.specialty}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Seleccione una fecha",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ClinicData.availableDates) { date ->
                            FilterChip(
                                selected = (selectedDate == date),
                                onClick = { selectedDate = date },
                                label = { Text(date) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Seleccione una hora",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ClinicData.availableTimes) { time ->
                            FilterChip(
                                selected = (selectedTime == time),
                                onClick = { selectedTime = time },
                                label = { Text(time) }
                            )
                        }
                    }
                }

                val isFormValid = selectedDate != null && selectedTime != null

                Button(
                    onClick = {
                        if (isFormValid) {
                            val newAppointment = Appointment(
                                id = 0, // will be assigned or tracked
                                doctorName = doctor.name,
                                specialty = doctor.specialty,
                                date = selectedDate!!,
                                time = selectedTime!!,
                                status = "Confirmada"
                            )
                            onConfirmAppointment(newAppointment)
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text("Confirmar cita")
                }
            }
        }
    }
}
