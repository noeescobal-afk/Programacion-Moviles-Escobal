package com.tecsup.campusgps.ui

// ARCHIVO: PantallaMapa.kt
// Presentación declarativa: recibe estados del ViewModel y comunica eventos de usuario.
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.viewmodel.MapaViewModel
import com.tecsup.campusgps.model.UbicacionUi
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("CampusGPS") }) }) { margen ->
        Column(Modifier.padding(margen).fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Button(onClick={ vm.buscar() },enabled=estado !is UbicacionUi.Buscando) { Text("Obtener mi ubicación") }
            when(val e=estado) {
                is UbicacionUi.Lista -> {Text("Latitud: ${e.posicion.latitud}");Text("Longitud: ${e.posicion.longitud}")}
                is UbicacionUi.Error -> Text(e.mensaje, color=MaterialTheme.colorScheme.error)
                UbicacionUi.Buscando -> CircularProgressIndicator()
                else -> Text("Sin posición")
            }
        }
    }
}
