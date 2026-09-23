package com.tuapp.clinicasaludplus.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppointmentStatusLabel(status: String) {
    val isConfirmed = status.equals("Confirmada", ignoreCase = true)
    val isCancelled = status.equals("Cancelada", ignoreCase = true)

    val containerColor = when {
        isConfirmed -> MaterialTheme.colorScheme.primaryContainer
        isCancelled -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = when {
        isConfirmed -> MaterialTheme.colorScheme.onPrimaryContainer
        isCancelled -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = containerColor
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
