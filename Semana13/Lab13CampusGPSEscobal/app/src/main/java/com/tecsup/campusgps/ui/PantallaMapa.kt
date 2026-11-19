package com.tecsup.campusgps.ui

// ARCHIVO: PantallaMapa.kt
// Presentación declarativa: recibe estados del ViewModel y comunica eventos de usuario.
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
    val lugares by vm.lugaresFiltrados.collectAsState()
    val todos by vm.lugares.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val radio by vm.radio.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val cercano by vm.masCercano.collectAsState()
    val contexto = LocalContext.current
    // Android dibuja el diálogo; aquí se maneja su resultado.
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permisos ->
        if (permisos.values.any { it }) vm.buscar() else vm.permisoDenegado()
    }
    fun alPulsar() {
        val aceptado = listOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
            .any { ContextCompat.checkSelfPermission(contexto,it)==PackageManager.PERMISSION_GRANTED }
        if (aceptado) vm.buscar() else lanzador.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
    }
    Scaffold(topBar = { TopAppBar(title = { Text("CampusGPS") }) }) { margen ->
        Column(Modifier.padding(margen).fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
            MapaUbicacion(estado,lugares,seleccionado,radio,Modifier.weight(1f).fillMaxWidth())
            androidx.compose.foundation.lazy.LazyRow(horizontalArrangement=Arrangement.spacedBy(6.dp),contentPadding=PaddingValues(horizontal=8.dp)) {
                items(5) { i ->
                    val cat = listOf("Todos","Estudio","Comida","Eventos","Servicios")[i]
                    FilterChip(selected=categoria==cat,onClick={vm.cambiarCategoria(cat)},label={Text(cat)})
                }
            }
            Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) {
                listOf(100,200,500).forEach { r -> FilterChip(selected=radio==r,onClick={vm.elegirRadio(r)},label={Text("${r} m")}) }
            }
            Text("Dentro del radio: ${lugares.count { (it.metros?:Float.MAX_VALUE)<=radio.toFloat() }} lugares")
            OutlinedButton(onClick={ cercano?.let { vm.seleccionar(it.id) } },enabled=cercano!=null) { Text("Ir al más cercano") }
            val primero = todos.firstOrNull { it.lugar.id==cercano?.id }
            if (primero!=null) Text("Más cercano: ${primero.lugar.nombre} (${primero.metros?.let { com.tecsup.campusgps.util.formatearDistancia(it) } ?: "Sin calcular"})")
            ListaLugares(lugares,seleccionado,vm::seleccionar,Modifier.fillMaxWidth().height(220.dp))
            Button(onClick={ alPulsar() },enabled=estado !is UbicacionUi.Buscando) { Text("Obtener mi ubicación") }
            when(val e=estado) {
                is UbicacionUi.Lista -> {Text("Latitud: ${e.posicion.latitud}");Text("Longitud: ${e.posicion.longitud}")}
                is UbicacionUi.Error -> MensajeError(e,::alPulsar)
                UbicacionUi.Buscando -> CircularProgressIndicator()
                else -> Text("Sin posición")
            }
        }
    }
}
