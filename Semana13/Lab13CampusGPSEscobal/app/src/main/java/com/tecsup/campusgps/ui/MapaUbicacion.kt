package com.tecsup.campusgps.ui

// ARCHIVO: MapaUbicacion.kt
// Mapa visual: administra la cámara y dibuja marcadores; no consulta los repositorios.
import androidx.compose.runtime.*
import androidx.compose.foundation.isSystemInDarkTheme
import com.google.android.gms.maps.model.MapStyleOptions
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import com.tecsup.campusgps.model.*
@Composable fun MapaUbicacion(estado: UbicacionUi, lugares: List<LugarConDistancia>, seleccionado: Int?, radio: Int, modifier: Modifier=Modifier) {
    // La cámara parte de Lima hasta que se conoce la ubicación del usuario.
    val camara = rememberCameraPositionState { position=CameraPosition.fromLatLngZoom(LatLng(-12.0464,-77.0428),12f) }
    LaunchedEffect(estado) { if (estado is UbicacionUi.Lista) camara.animate(
        com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(LatLng(estado.posicion.latitud,estado.posicion.longitud),16f)) }
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id==seleccionado }?.lugar
        if (lugar!=null) camara.animate(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud,lugar.longitud),17f))
    }
    val cercano = lugares.firstOrNull { it.metros!=null }?.lugar?.id
    val oscuro = isSystemInDarkTheme()
    val estilo = remember(oscuro) { if(oscuro) MapStyleOptions("""[{"elementType":"geometry","stylers":[{"color":"#242f3e"}]},{"elementType":"labels.text.fill","stylers":[{"color":"#aebad0"}]},{"featureType":"road","elementType":"geometry","stylers":[{"color":"#38465a"}]},{"featureType":"water","elementType":"geometry","stylers":[{"color":"#17263c"}]}]""") else null }
    GoogleMap(modifier=modifier,cameraPositionState=camara,properties=MapProperties(mapStyleOptions=estilo)) {
        if(estado is UbicacionUi.Lista) Circle(center=LatLng(estado.posicion.latitud,estado.posicion.longitud),radius=radio.toDouble(),
            strokeColor=android.graphics.Color.rgb(0,108,198),fillColor=android.graphics.Color.argb(32,0,108,198),strokeWidth=3f)

        if (estado is UbicacionUi.Lista) Marker(state=rememberUpdatedMarkerState(position=LatLng(estado.posicion.latitud,estado.posicion.longitud)),title="Estás aquí")
        lugares.forEach { item ->
            key(item.lugar.id) {
                Marker(state=rememberUpdatedMarkerState(position=LatLng(item.lugar.latitud,item.lugar.longitud)),
                    title=item.lugar.nombre,snippet=item.lugar.categoria,
                    icon=BitmapDescriptorFactory.defaultMarker(if(item.lugar.id==cercano) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_AZURE))
            }
        }
    }
}
