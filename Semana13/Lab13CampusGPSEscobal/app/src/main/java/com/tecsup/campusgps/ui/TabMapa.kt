package com.tecsup.campusgps.ui

// CampusGPS: pestaña Mapa con interfaz institucional y controles funcionales.
// No altera el repositorio de lugares ni las coordenadas persistidas.

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.viewmodel.MapaViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tecsup.campusgps.R
import androidx.compose.foundation.isSystemInDarkTheme
import com.tecsup.campusgps.model.UbicacionUi
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import com.tecsup.campusgps.ui.theme.LocalCampusDarkTheme
private val AzulCampus = Color(0xFF0E5BD7)
private val AzulCabecera = Color(0xFF0A3D91)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugaresMapa by vm.lugaresMapa.collectAsState()
    val todos by vm.lugares.collectAsState()
    val cercaDeMi by vm.cercaDeMi.collectAsState()
    val radioVal by vm.radioVal.collectAsState()
    val mostrarRadio by vm.mostrarRadio.collectAsState()
    // Solo habilitamos el filtro si existe una ubicación GPS válida.
    val gpsActivo = estado is UbicacionUi.Lista
    val accionMapa by vm.accionMapa.collectAsState()
    val rutaDestino by vm.rutaDestino.collectAsState()
    val favoritos by vm.favoritos.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    // Los marcadores permanecen ocultos hasta solicitar la ubicación.
    var mostrarPuntosTrasGps by rememberSaveable {
        mutableStateOf(false)
    }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val lugarSeleccionado = todos.firstOrNull { it.lugar.id == seleccionado }
    if (lugarSeleccionado != null) {
        FichaInformativaBottomSheet(
            lugarConDistancia = lugarSeleccionado,
            esFavorito = favoritos.contains(lugarSeleccionado.lugar.id),
            onToggleFavorito = { vm.toggleFavorito(it) },
            onComoLlegar = { vm.establecerRuta(it) },
            onDismiss = { vm.seleccionar(null) },
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF082F70),
                                Color(0xFF0A479D),
                                Color(0xFF1264BF)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Identidad de la aplicación
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "CampusGPS",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Text(
                            text = "TECSUP Lima",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFDCE8FF)
                        )
                    }

                    // Logotipo institucional
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 3.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color(0xFFE5F1FC)
                        )
                    ) {
                        Image(
                            painter = painterResource(
                                id = R.drawable.tecsup_logo
                            ),
                            contentDescription = "Logotipo de TECSUP",
                            modifier = Modifier
                                .width(124.dp)
                                .height(48.dp)
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        },

        ) { outerPadding ->
        // El padding del Scaffold exterior se aplica UNA sola vez.
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 205.dp,
            sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            sheetContainerColor = if (LocalCampusDarkTheme.current) {
                Color(0xFF243650)
            } else {
                Color(0xFFF2F7FF)
            },
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "¿A dónde quieres ir?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Busca una instalación o necesidad",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    // Una superficie clicable real, no un TextField deshabilitado.
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { vm.cambiarPestanaNavegacion(1) },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = "Biblioteca, laboratorio, cafetería...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(
                                text = "Cerca de mí",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            Text(
                                text = when {
                                    !gpsActivo -> "Activa tu ubicación para buscar lugares cercanos"
                                    cercaDeMi -> "Filtrando instalaciones próximas"
                                    else -> "Todas las instalaciones visibles"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                        }

                        Switch(
                            checked = gpsActivo && cercaDeMi,
                            enabled = gpsActivo,
                            onCheckedChange = {
                                vm.toggleCercaDeMi()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF0E5BD7),
                                uncheckedThumbColor = Color(0xFF405875),
                                uncheckedTrackColor = Color(0xFFD7E5FB)
                            )
                        )



                    }
                    if (gpsActivo && cercaDeMi) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            listOf(100, 200, 500).forEach { distancia ->
                                FilterChip(
                                    modifier = Modifier.weight(1f),
                                    selected = radioVal == distancia,
                                    onClick = { vm.elegirRadioVal(distancia) },
                                    label = { Text("$distancia m") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AzulCampus,
                                        selectedLabelColor = Color.White,
                                    ),
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "Mostrar área en el mapa",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )


                            Switch(
                                checked = gpsActivo && cercaDeMi && mostrarRadio,
                                enabled = gpsActivo && cercaDeMi,
                                onCheckedChange = {
                                    vm.toggleMostrarRadio()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF0E5BD7),
                                    uncheckedThumbColor = Color(0xFF405875),
                                    uncheckedTrackColor = Color(0xFFD7E5FB)
                                )
                            )


                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            },
        ) { _ ->
            // Mapa debajo del panel: evita descontar dos veces la altura del sheet.
            Box(Modifier.fillMaxSize()) {
                MapaUbicacion(
                    estado = estado,
                    lugares = if (mostrarPuntosTrasGps && gpsActivo) {
                        lugaresMapa
                    } else {
                        emptyList()
                    },
                    radio = radioVal,
                    mostrarRadio = cercaDeMi && mostrarRadio,
                    accionMapa = accionMapa,
                    modoEdicion = false,
                    lugarAEditar = null,
                    posicionTemporalEdicion = null,
                    rutaDestino = rutaDestino,
                    onAccionEjecutada = { vm.limpiarAccionMapa() },
                    onAjustarVista = { vm.ajustarVista() },
                    onSeleccionarLugarParaEditar = {},
                    onPosicionTemporalCambiada = {},
                    onLimpiarRuta = { vm.limpiarRuta() },
                    modifier = Modifier.fillMaxSize(),
                )

                // Solo controles que ya tienen implementación real en el ViewModel.
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            mostrarPuntosTrasGps = true
                            vm.buscar()
                        },
                        containerColor = AzulCampus,
                        contentColor = Color.White
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Mi ubicación"
                        )
                    }
                }
            }
        }
    }
}
