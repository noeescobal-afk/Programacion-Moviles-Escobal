package com.tecsup.campusgps.data

// ARCHIVO: UbicacionRepositoryGoogle.kt
// Implementación de ubicación basada en FusedLocationProviderClient con logs de diagnóstico.

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val cts = CancellationTokenSource()
        // Solicita ubicación actual y fresca con alta precisión
        val lugar = cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token).await()
            ?: cliente.lastLocation.await()
            ?: error("Activa el GPS e intenta de nuevo")

        val horaFormateada = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date(lugar.time))
        Log.d(
            "DIAGNOSTICO_GPS",
            "Ubicación GPS obtenida -> Lat: ${lugar.latitude}, Lng: ${lugar.longitude}, " +
                    "Precisión: ${lugar.accuracy}m, Hora de fijación: $horaFormateada (timestamp: ${lugar.time})"
        )

        Posicion(lugar.latitude, lugar.longitude)
    }
}
