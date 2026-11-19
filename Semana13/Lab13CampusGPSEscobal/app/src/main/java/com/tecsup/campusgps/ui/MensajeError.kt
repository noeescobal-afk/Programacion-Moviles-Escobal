package com.tecsup.campusgps.ui

// ARCHIVO: MensajeError.kt
// Componente de estados de error con acciones apropiadas para permisos y GPS.
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.*
@Composable fun MensajeError(error: UbicacionUi.Error, alReintentar: ()->Unit) {
    val contexto=LocalContext.current
    Card(Modifier.fillMaxWidth().padding(12.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)) {
        Column(Modifier.padding(12.dp)) {
            Text(error.mensaje,color=MaterialTheme.colorScheme.onErrorContainer)
            when(error.tipo) {
                TipoError.SIN_PERMISO -> OutlinedButton(onClick={
                    contexto.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package",contexto.packageName,null)))
                }) { Text("Abrir ajustes") }
                TipoError.GPS_APAGADO, TipoError.OTRO -> OutlinedButton(onClick=alReintentar) { Text("Reintentar") }
                TipoError.SIN_PLAY_SERVICES -> Unit
            }
        }
    }
}
