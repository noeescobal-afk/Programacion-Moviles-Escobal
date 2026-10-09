package com.saludplus.citas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.R
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio

private val AzulDoctor = Color(0xFF246BFD)
private val AzulClaroDoctor = Color(0xFFEAF1FF)
private val VerdeDoctor = Color(0xFF168A65)
private val VerdeClaroDoctor = Color(0xFFDDF6EA)

@Composable
fun DoctorCard(
    medico: Medico,
    onVerHorarios: () -> Unit,
    modifier: Modifier = Modifier
) {
    val especialidad = Repositorio.obtenerEspecialidad(medico.especialidadId)
    val sede = Repositorio.obtenerSede(medico.sedeId)

    val imagenResId = when (medico.id) {
        1 -> R.drawable.medico_1
        2 -> R.drawable.medico_2
        3 -> R.drawable.medico_3
        4 -> R.drawable.medico_4
        5 -> R.drawable.medico_5
        6 -> R.drawable.medico_6
        7 -> R.drawable.medico_7
        8 -> R.drawable.medico_8
        9 -> R.drawable.medico_9
        10 -> R.drawable.medico_10
        else -> R.drawable.medico_1
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onVerHorarios() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Fotografía
                Image(
                    painter = painterResource(id = imagenResId),
                    contentDescription = "Foto de ${medico.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, AzulDoctor, CircleShape)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = medico.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = especialidad?.nombre ?: "Especialidad",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulDoctor
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "CMP: ${medico.cmp}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VerdeClaroDoctor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Disponible",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = VerdeDoctor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sede y Teléfono
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzulClaroDoctor, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        tint = AzulDoctor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = sede?.nombre ?: "SaludPlus",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = null,
                        tint = AzulDoctor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = medico.telefono,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onVerHorarios,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulDoctor,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ver horarios",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
