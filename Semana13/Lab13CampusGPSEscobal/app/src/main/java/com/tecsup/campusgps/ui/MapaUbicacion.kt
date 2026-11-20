package com.tecsup.campusgps.ui

// ARCHIVO: MapaUbicacion.kt
// Mapa visual utilizando MapLibre Native + OpenFreeMap con soporte para ruta de navegación ("Cómo llegar").

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tecsup.campusgps.model.AccionMapa
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.distanciaMetros
import com.tecsup.campusgps.util.formatearDistancia
import org.maplibre.android.annotations.Icon
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.PolylineOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import com.tecsup.campusgps.ui.theme.LocalCampusDarkTheme
private val TECSUP_LIMA_BASE = LatLng(-12.0453, -76.9533)

private fun crearIconoMarcador(context: Context, colorHex: Int): Icon {
    val size = 52
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paintBorde = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paintBorde)

    val paintCentro = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorHex
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, (size / 2f) - 5f, paintCentro)

    return IconFactory.getInstance(context).fromBitmap(bitmap)
}

private fun calcularPuntosCirculo(centro: LatLng, radioMetros: Double, puntos: Int = 64): List<LatLng> {
    val lat = Math.toRadians(centro.latitude)
    val lng = Math.toRadians(centro.longitude)
    val dR = radioMetros / 6371000.0
    val lista = mutableListOf<LatLng>()
    for (i in 0 until puntos) {
        val bearing = 2.0 * Math.PI * i / puntos
        val latP = Math.asin(Math.sin(lat) * Math.cos(dR) + Math.cos(lat) * Math.sin(dR) * Math.cos(bearing))
        val lngP = lng + Math.atan2(Math.sin(bearing) * Math.sin(dR) * Math.cos(lat), Math.cos(dR) - Math.sin(lat) * Math.sin(latP))
        lista.add(LatLng(Math.toDegrees(latP), Math.toDegrees(lngP)))
    }
    return lista
}

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    radio: Int,
    mostrarRadio: Boolean,
    accionMapa: AccionMapa,
    modoEdicion: Boolean,
    lugarAEditar: Lugar?,
    posicionTemporalEdicion: Posicion?,
    rutaDestino: Lugar?,
    onAccionEjecutada: () -> Unit,
    onAjustarVista: () -> Unit,
    onSeleccionarLugarParaEditar: (Lugar) -> Unit,
    onPosicionTemporalCambiada: (Posicion) -> Unit,
    onLimpiarRuta: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val oscuro = LocalCampusDarkTheme.current

    val mapView = remember { MapView(contexto) }
    var maplibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var isStyleLoaded by remember { mutableStateOf(false) }

    val currentModoEdicion by rememberUpdatedState(modoEdicion)
    val currentLugarAEditar by rememberUpdatedState(lugarAEditar)
    val currentLugares by rememberUpdatedState(lugares)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    LaunchedEffect(mapView) {
        mapView.getMapAsync { map ->
            maplibreMap = map
            map.cameraPosition = CameraPosition.Builder()
                .target(TECSUP_LIMA_BASE)
                .zoom(16.0)
                .build()
        }
    }

    LaunchedEffect(maplibreMap) {
        val map = maplibreMap ?: return@LaunchedEffect

        map.setOnMarkerClickListener { marker ->
            if (currentModoEdicion) {
                val lugar = currentLugares.firstOrNull { it.lugar.nombre == marker.title }?.lugar
                if (lugar != null) {
                    onSeleccionarLugarParaEditar(lugar)
                }
                true
            } else {
                false
            }
        }

        map.addOnMapClickListener { latLng ->
            if (currentModoEdicion && currentLugarAEditar != null) {
                onPosicionTemporalCambiada(Posicion(latLng.latitude, latLng.longitude))
                true
            } else {
                false
            }
        }
    }

    val styleUrl = "https://tiles.openfreemap.org/styles/bright"

    LaunchedEffect(maplibreMap, oscuro) {
        val map = maplibreMap ?: return@LaunchedEffect
        isStyleLoaded = false
        map.setStyle(Style.Builder().fromUri(styleUrl)) {
            isStyleLoaded = true
            Log.d("CampusGPS_Mapa", "Estilo cargado en MapLibre. isStyleLoaded=true")
        }
    }

    // Manejo de AccionMapa
    LaunchedEffect(accionMapa, maplibreMap, isStyleLoaded) {
        val map = maplibreMap ?: return@LaunchedEffect
        if (!isStyleLoaded) return@LaunchedEffect

        when (accionMapa) {
            is AccionMapa.CentrarUbicacion -> {
                val latLng = LatLng(accionMapa.posicion.latitud, accionMapa.posicion.longitud)
                map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(latLng).zoom(16.5).build()), 1000)
                onAccionEjecutada()
            }
            is AccionMapa.CentrarCampus -> {
                val latLng = LatLng(accionMapa.centro.latitud, accionMapa.centro.longitud)
                map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(latLng).zoom(16.0).build()), 1000)
                onAccionEjecutada()
            }
            is AccionMapa.SeleccionarLugar -> {
                val latLng = LatLng(accionMapa.lugar.latitud, accionMapa.lugar.longitud)
                map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(latLng).zoom(17.5).build()), 1000)
                onAccionEjecutada()
            }
            is AccionMapa.AjustarVista -> {
                if (accionMapa.posiciones.isNotEmpty()) {
                    val builder = LatLngBounds.Builder()
                    accionMapa.posiciones.forEach { builder.include(LatLng(it.latitud, it.longitud)) }
                    if (estado is UbicacionUi.Lista) {
                        builder.include(LatLng(estado.posicion.latitud, estado.posicion.longitud))
                    }
                    val bounds = builder.build()
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 1000)
                }
                onAccionEjecutada()
            }
            is AccionMapa.Ninguna -> {}
        }
    }

    val cercanoId = lugares.firstOrNull { it.metros != null }?.lugar?.id
    val iconoUsuario = remember(contexto) { crearIconoMarcador(contexto, android.graphics.Color.rgb(220, 53, 69)) }     // Rojo
    val iconoAzul = remember(contexto) { crearIconoMarcador(contexto, android.graphics.Color.rgb(18, 87, 166)) }       // Azul
    val iconoVerde = remember(contexto) { crearIconoMarcador(contexto, android.graphics.Color.rgb(40, 167, 69)) }      // Verde
    val iconoNaranja = remember(contexto) { crearIconoMarcador(contexto, android.graphics.Color.rgb(255, 140, 0)) }    // Naranja

    // Dibuja contorno de radio, ruta guía y marcadores
    LaunchedEffect(maplibreMap, isStyleLoaded, estado, lugares, radio, mostrarRadio, oscuro, modoEdicion, lugarAEditar, posicionTemporalEdicion, rutaDestino) {
        val map = maplibreMap ?: return@LaunchedEffect
        if (!isStyleLoaded) return@LaunchedEffect

        map.clear()

        // 1. Dibujar contorno de radio
        if (estado is UbicacionUi.Lista && mostrarRadio) {
            val posLatLng = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            val colorBorde = if (oscuro) android.graphics.Color.rgb(145, 197, 255) else android.graphics.Color.rgb(18, 87, 166)

            map.addPolyline(
                PolylineOptions()
                    .addAll(calcularPuntosCirculo(posLatLng, radio.toDouble()))
                    .color(colorBorde)
                    .width(2.5f)
            )
        }

        // 1.5. Dibujar línea de ruta guía ("Cómo llegar")
        if (estado is UbicacionUi.Lista && rutaDestino != null && rutaDestino.ubicada) {
            val userPos = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            val destPos = LatLng(rutaDestino.latitud, rutaDestino.longitud)
            map.addPolyline(
                PolylineOptions()
                    .add(userPos, destPos)
                    .color(android.graphics.Color.parseColor("#087F70"))
                    .width(4.5f)
            )
        }

        // 2. Dibujar marcador de usuario
        if (estado is UbicacionUi.Lista) {
            val posLatLng = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            map.addMarker(
                MarkerOptions()
                    .position(posLatLng)
                    .title("Tu Ubicación Actual")
                    .icon(iconoUsuario)
            )
        }

        // 3. Dibujar marcadores de lugares ubicados
        lugares.forEach { item ->
            if (item.lugar.ubicada && !(modoEdicion && lugarAEditar != null && item.lugar.id == lugarAEditar.id)) {
                val esElMasCercano = item.lugar.id == cercanoId
                val icono = if (esElMasCercano) iconoVerde else iconoAzul
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(item.lugar.latitud, item.lugar.longitud))
                        .title(item.lugar.nombre)
                        .snippet(if (modoEdicion) "Toca para editar posición" else item.lugar.categoria)
                        .icon(icono)
                )
            }
        }

        // 4. Marcador de vista previa para el lugar en edición
        if (modoEdicion && lugarAEditar != null && posicionTemporalEdicion != null) {
            val tempLatLng = LatLng(posicionTemporalEdicion.latitud, posicionTemporalEdicion.longitud)
            map.addMarker(
                MarkerOptions()
                    .position(tempLatLng)
                    .title("EDITANDO: ${lugarAEditar.nombre}")
                    .snippet("Toca el mapa para recolocar")
                    .icon(iconoNaranja)
            )
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Banner flotante superior para "Cómo llegar"
        if (rutaDestino != null && estado is UbicacionUi.Lista) {
            val userPos = (estado as UbicacionUi.Lista).posicion
            val metros = distanciaMetros(userPos, rutaDestino)
            val mins = (metros / 80f).toInt().coerceAtLeast(1)
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("🧭 Navegando a: ${rutaDestino.nombre}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("🚶 Aprox. $mins min (${formatearDistancia(metros)})", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    TextButton(onClick = onLimpiarRuta) {
                        Text("Salir", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Controles flotantes de navegación en el borde derecho del mapa
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SmallFloatingActionButton(
                onClick = onAjustarVista,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Campus",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                shadowElevation = 4.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            maplibreMap?.animateCamera(CameraUpdateFactory.zoomIn(), 300)
                        }
                    ) {
                        Text(
                            "+",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.width(24.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    IconButton(
                        onClick = {
                            maplibreMap?.animateCamera(CameraUpdateFactory.zoomOut(), 300)
                        }
                    ) {
                        Text(
                            "−",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
