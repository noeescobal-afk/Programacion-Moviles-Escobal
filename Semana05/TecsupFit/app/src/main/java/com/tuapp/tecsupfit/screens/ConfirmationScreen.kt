package com.tuapp.tecsupfit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuapp.tecsupfit.model.FitnessClass
import com.tuapp.tecsupfit.model.Reservation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    classId: Int,
    scheduleIndex: Int,
    classes: List<FitnessClass>,
    reservations: MutableList<Reservation>,
    onViewReservationsClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val fitnessClass = classes.find { it.id == classId }
    val schedule = fitnessClass?.schedules?.getOrNull(scheduleIndex)

    if (fitnessClass == null || schedule == null) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Confirmación de reserva") })
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No se encontró la información de la clase",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onHomeClick) {
                    Text("Regresar al inicio")
                }
            }
        }
        return
    }

    var isConfirmed by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }

    val alreadyReserved = reservations.any { it.classId == classId && it.schedule == schedule }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Confirmación de reserva") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Resumen de tu reserva",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Clase: ${fitnessClass.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Instructor: ${fitnessClass.instructor}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Horario: $schedule", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Sala: ${fitnessClass.room}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Duración: ${fitnessClass.duration}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!isConfirmed && !alreadyReserved) {
                Button(
                    onClick = {
                        val newReservation = Reservation(
                            id = reservations.size + 1,
                            classId = fitnessClass.id,
                            className = fitnessClass.name,
                            schedule = schedule,
                            room = fitnessClass.room,
                            status = "Confirmada"
                        )
                        reservations.add(newReservation)
                        isConfirmed = true
                        message = "Reserva registrada correctamente"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar reserva")
                }
            } else {
                if (alreadyReserved && !isConfirmed) {
                    Text(
                        text = "Ya tienes una reserva registrada para esta clase y horario.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onViewReservationsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver mis reservas")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onHomeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar al inicio")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
