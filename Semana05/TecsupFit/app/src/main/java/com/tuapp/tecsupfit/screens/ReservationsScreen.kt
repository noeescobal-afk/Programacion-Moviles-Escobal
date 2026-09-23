package com.tuapp.tecsupfit.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tuapp.tecsupfit.components.CancelReservationDialog
import com.tuapp.tecsupfit.components.ReservationCard
import com.tuapp.tecsupfit.model.Reservation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    reservations: List<Reservation>,
    onCancelReservation: (Int) -> Unit
) {
    var reservationIdToCancel by rememberSaveable { mutableStateOf<Int?>(null) }
    val reservationToCancel = reservations.find { it.id == reservationIdToCancel }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Reservas") }
            )
        }
    ) { innerPadding ->
        if (reservations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes reservas registradas",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                items(reservations, key = { it.id }) { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onCancelClick = {
                            if (reservation.status == "Confirmada") {
                                reservationIdToCancel = reservation.id
                            }
                        }
                    )
                }
            }
        }

        reservationToCancel?.let { res ->
            CancelReservationDialog(
                reservation = res,
                onDismiss = { reservationIdToCancel = null },
                onConfirm = {
                    onCancelReservation(res.id)
                    reservationIdToCancel = null
                }
            )
        }
    }
}
