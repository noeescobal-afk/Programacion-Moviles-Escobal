package com.tecsup.campusgps.data

// ARCHIVO: UbicacionRepositoryGoogle.kt
// Implementación de ubicación basada en FusedLocationProviderClient. Requiere permiso previamente concedido.
import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await
class UbicacionRepositoryGoogle(context: Context): UbicacionRepository {
    private val appContext = context.applicationContext
    override fun playServicesDisponible(): Boolean = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext)==ConnectionResult.SUCCESS
    private val cliente = LocationServices.getFusedLocationProviderClient(context.applicationContext)
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val lugar = cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            ?: error("Activa el GPS e intenta de nuevo")
        Posicion(lugar.latitude, lugar.longitude)
    }
}
