package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Unidad

@Composable
fun SelectorCantidad(
    cantidad: Double,
    unidad: String,
    onCantidadChanged: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val (min, max, step) = when (unidad) {
        Unidad.KG.name -> Triple(0.0, 10.0, 0.5)
        else -> Triple(0.0, 30.0, 1.0)
    }

    val canDecrease = cantidad > min
    val canIncrease = cantidad < max

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledIconButton(
            onClick = {
                if (canDecrease) {
                    val nueva = (cantidad - step).coerceAtLeast(min)
                    onCantidadChanged(nueva)
                }
            },
            enabled = canDecrease,
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Disminuir", modifier = Modifier.size(18.dp))
        }

        Text(
            text = if (unidad == Unidad.KG.name) {
                String.format(java.util.Locale.getDefault(), "%.1f", cantidad)
            } else {
                cantidad.toInt().toString()
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.widthIn(min = 36.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        FilledIconButton(
            onClick = {
                if (canIncrease) {
                    val nueva = (cantidad + step).coerceAtMost(max)
                    onCantidadChanged(nueva)
                }
            },
            enabled = canIncrease,
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = "Aumentar", modifier = Modifier.size(18.dp))
        }
    }
}
