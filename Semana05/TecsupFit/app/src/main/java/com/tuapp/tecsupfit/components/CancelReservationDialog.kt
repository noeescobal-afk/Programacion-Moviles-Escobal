package com.tuapp.tecsupfit.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.tuapp.tecsupfit.model.Reservation

@Composable
fun CancelReservationDialog(
    reservation: Reservation,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancelar reserva") },
        text = {
            Text("¿Estás seguro de cancelar la reserva para \"${reservation.className}\" (${reservation.schedule})?")
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
