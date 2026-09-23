package com.tuapp.clinicasaludplus.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.tuapp.clinicasaludplus.model.Appointment

@Composable
fun CancelAppointmentDialog(
    appointment: Appointment,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancelar cita") },
        text = {
            Text("¿Estás seguro de que deseas cancelar la cita con ${appointment.doctorName} el ${appointment.date} a las ${appointment.time}?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sí, cancelar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Volver")
            }
        }
    )
}
