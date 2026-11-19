package com.tecsup.campusgps.ui

// ARCHIVO: MapaUbicacion.kt
// Mapa visual: administra la cámara y dibuja marcadores; no consulta los repositorios.
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import com.tecsup.campusgps.model.*
@Composable fun MapaUbicacion(estado: UbicacionUi, lugares: List<LugarConDistancia>, seleccionado: Int?, modifier: Modifier=Modifier) {
    // La cámara parte de Lima hasta que se conoce la ubicación del usuario.
    val camara = rememberCameraPositionState { position=CameraPosition.fromLatLngZoom(LatLng(-12.0464,-77.0428),12f) }
    LaunchedEffect(estado) { if (estado is UbicacionUi.Lista) camara.animate(
        com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(LatLng(estado.posicion.latitud,estado.posicion.longitud),16f)) }
    GoogleMap(modifier=modifier,cameraPositionState=camara) {
        if (estado is UbicacionUi.Lista) Marker(state=rememberUpdatedMarkerState(position=LatLng(estado.posicion.latitud,estado.posicion.longitud)),title="Estás aquí")
        lugares.forEach { item ->
            key(item.lugar.id) {
                Marker(state=rememberUpdatedMarkerState(position=LatLng(item.lugar.latitud,item.lugar.longitud)),
                    title=item.lugar.nombre,snippet=item.lugar.categoria,
                    icon=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            }
        }
    }
}
