package com.tecsup.avisoscampus.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.ModoTema
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.receiver.CargadorReceiver
import com.tecsup.avisoscampus.receiver.ModoAvionReceiver
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel
import java.util.Locale

enum class SeccionNavegacion(val titulo: String, val icono: ImageVector) {
    INICIO("Inicio", Icons.Default.Home),
    HISTORIAL("Historial", Icons.Default.History),
    CONFIGURACION("Configuración", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val avion by vm.escuchandoAvion.collectAsState()
    val cargador by vm.escuchandoCargador.collectAsState()
    val modoTema by vm.modoTema.collectAsState()

    var seccionActual by remember { mutableStateOf(SeccionNavegacion.INICIO) }
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { vm.permisoRespondido(it) }

    LaunchedEffect(contexto) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
        vm.eventos.collect { if (!Notificador.mostrar(contexto, it)) vm.noSePudoMostrar() }
    }

    DisposableEffect(avion, contexto) {
        if (avion) {
            val receptor = ModoAvionReceiver(vm::alCambiarModoAvion)
            ContextCompat.registerReceiver(contexto, receptor, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED), ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else onDispose { }
    }

    DisposableEffect(cargador, contexto) {
        if (cargador) {
            val receptor = CargadorReceiver(vm::alCambiarCargador)
            val filtro = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            ContextCompat.registerReceiver(contexto, receptor, filtro, ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else onDispose { }
    }

    val colores = MaterialTheme.colorScheme
    val esHorizontal = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        containerColor = colores.background,
        topBar = {
            HeaderBar(
                permiso = permiso,
                avisosCount = avisos.size
            )
        },
        bottomBar = {
            NavegacionInferior(
                seccionActual = seccionActual,
                alSeleccionar = { seccionActual = it }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Crossfade(
                targetState = seccionActual,
                animationSpec = tween(durationMillis = 300),
                label = "SeccionTransition"
            ) { seccion ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (seccion) {
                        SeccionNavegacion.INICIO -> SeccionInicio(
                            vm = vm,
                            avisos = avisos,
                            faltan = faltan,
                            mensaje = mensaje,
                            avion = avion,
                            cargador = cargador,
                            permiso = permiso,
                            contexto = contexto,
                            esHorizontal = esHorizontal,
                            alIrAHistorial = { seccionActual = SeccionNavegacion.HISTORIAL }
                        )
                        SeccionNavegacion.HISTORIAL -> SeccionHistorial(
                            avisos = avisos,
                            onGenerarPrueba = vm::avisarAhora
                        )
                        SeccionNavegacion.CONFIGURACION -> SeccionConfiguracion(
                            modoTema = modoTema,
                            alCambiarTema = vm::cambiarModoTema,
                            permiso = permiso,
                            contexto = contexto,
                            lanzador = lanzador,
                            esHorizontal = esHorizontal
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderBar(
    permiso: PermisoUi,
    avisosCount: Int
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = "AvisosCampus",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = "CENTRO DE NOTIFICACIONES",
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.sp,
                        color = Color(0xFFBDE5FF)
                    )
                }
            }
        },
        actions = {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (permiso) {
                                    PermisoUi.Concedido -> Color(0xFF10B981)
                                    PermisoUi.Denegado -> Color(0xFFEF4444)
                                    PermisoUi.SinPedir -> Color(0xFFF59E0B)
                                }
                            )
                    )
                    Text(
                        text = "$avisosCount avisos",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0A192F),
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun NavegacionInferior(
    seccionActual: SeccionNavegacion,
    alSeleccionar: (SeccionNavegacion) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        SeccionNavegacion.entries.forEach { seccion ->
            val seleccionada = seccionActual == seccion
            NavigationBarItem(
                selected = seleccionada,
                onClick = { alSeleccionar(seccion) },
                icon = {
                    Icon(
                        imageVector = seccion.icono,
                        contentDescription = seccion.titulo
                    )
                },
                label = {
                    Text(
                        text = seccion.titulo,
                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Composable
private fun SeccionInicio(
    vm: AvisosViewModel,
    avisos: List<Aviso>,
    faltan: Int?,
    mensaje: String?,
    avion: Boolean,
    cargador: Boolean,
    permiso: PermisoUi,
    contexto: Context,
    esHorizontal: Boolean,
    alIrAHistorial: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth()
        ) {
            BannerHeader(avisosCount = avisos.size)
        }

        Box(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth()
        ) {
            PanelIndicadores(
                avisosCount = avisos.size,
                receptoresActivos = listOf(avion, cargador).count { it },
                permiso = permiso
            )
        }

        mensaje?.let { error ->
            Card(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    OutlinedButton(
                        onClick = { abrirAjustes(contexto) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ajustes")
                    }
                }
            }
        }

        if (esHorizontal) {
            Row(
                modifier = Modifier
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TarjetaAvisoAhora(onAvisar = vm::avisarAhora)
                    TarjetaReceptores(
                        avion = avion,
                        cargador = cargador,
                        onCambiarAvion = vm::cambiarEscucha,
                        onCambiarCargador = vm::cambiarEscuchaCargador
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RecordatorioCircularWidget(
                        faltan = faltan,
                        onIniciar = { vm.recordarEn() },
                        onCancelar = vm::cancelarRecordatorio
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaAvisoAhora(onAvisar = vm::avisarAhora)

                RecordatorioCircularWidget(
                    faltan = faltan,
                    onIniciar = { vm.recordarEn() },
                    onCancelar = vm::cancelarRecordatorio
                )

                TarjetaReceptores(
                    avion = avion,
                    cargador = cargador,
                    onCambiarAvion = vm::cambiarEscucha,
                    onCambiarCargador = vm::cambiarEscuchaCargador
                )
            }
        }

        // Vista previa del historial reciente
        if (avisos.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Actividad Reciente",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TextButton(onClick = alIrAHistorial) {
                                Text("Ver todo")
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        HistorialAvisos(
                            avisos = avisos.take(2)
                        )
                    }
                }
            }
        }

        Text(
            text = "AvisosCampus · Semana 15",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun SeccionHistorial(
    avisos: List<Aviso>,
    onGenerarPrueba: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxWidth()
        ) {
            HistorialAvisos(
                avisos = avisos,
                onGenerarPrueba = onGenerarPrueba
            )
        }
    }
}

@Composable
private fun SeccionConfiguracion(
    modoTema: ModoTema,
    alCambiarTema: (ModoTema) -> Unit,
    permiso: PermisoUi,
    contexto: Context,
    lanzador: androidx.activity.result.ActivityResultLauncher<String>,
    esHorizontal: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (esHorizontal) {
            Row(
                modifier = Modifier
                    .widthIn(max = 1000.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TarjetaTema(
                        modoTema = modoTema,
                        alCambiarTema = alCambiarTema
                    )
                    TarjetaGestionPermisos(
                        permiso = permiso,
                        contexto = contexto,
                        lanzador = lanzador
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TarjetaInfoCanales()
                    TarjetaAcercaDe()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaTema(
                    modoTema = modoTema,
                    alCambiarTema = alCambiarTema
                )
                TarjetaGestionPermisos(
                    permiso = permiso,
                    contexto = contexto,
                    lanzador = lanzador
                )
                TarjetaInfoCanales()
                TarjetaAcercaDe()
            }
        }
    }
}

@Composable
private fun BannerHeader(avisosCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0F2027),
                            Color(0xFF203A43),
                            Color(0xFF2C5364)
                        )
                    )
                )
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Tu campus al día · $avisosCount en historial",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFE0F2FE),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    text = "Gestión Inteligente de Avisos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Supervisa notificaciones instantáneas, recordatorios urgentes con cuenta regresiva y receptores automáticos del estado del dispositivo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF93C5FD)
                )
            }
        }
    }
}

@Composable
private fun PanelIndicadores(
    avisosCount: Int,
    receptoresActivos: Int,
    permiso: PermisoUi
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Indicador 1: Avisos
        CardIndicador(
            icono = Icons.Default.Notifications,
            valor = "$avisosCount",
            etiqueta = "Avisos Generados",
            colorIcono = Color(0xFF0284C7),
            modifier = Modifier.weight(1f)
        )

        // Indicador 2: Receptores
        CardIndicador(
            icono = Icons.Default.Sensors,
            valor = "$receptoresActivos / 2",
            etiqueta = "Receptores Activos",
            colorIcono = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
        )

        // Indicador 3: Permiso
        val (estadoTexto, estadoColor) = when (permiso) {
            PermisoUi.Concedido -> "Permitido" to Color(0xFF10B981)
            PermisoUi.Denegado -> "Denegado" to MaterialTheme.colorScheme.error
            PermisoUi.SinPedir -> "Sin pedir" to Color(0xFFF59E0B)
        }

        CardIndicador(
            icono = if (permiso == PermisoUi.Concedido) Icons.Default.Verified else Icons.Default.Security,
            valor = estadoTexto,
            etiqueta = "Permisos Android",
            colorIcono = estadoColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CardIndicador(
    icono: ImageVector,
    valor: String,
    etiqueta: String,
    colorIcono: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(colorIcono.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedContent(
                targetState = valor,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ValorIndicador"
            ) { targetValor ->
                Text(
                    text = targetValor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }

            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TarjetaAvisoAhora(onAvisar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Aviso Instantáneo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Envía una notificación al canal normal de Avisos de forma inmediata.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onAvisar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mostrar aviso ahora")
            }
        }
    }
}

@Composable
fun RecordatorioCircularWidget(
    faltan: Int?,
    onIniciar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activo = faltan != null
    val segundosTotales = 10
    val segundosRestantes = faltan ?: 0
    val targetProgress = if (activo) segundosRestantes / segundosTotales.toFloat() else 0f

    val progressAnimado by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "timerProgress"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Recordatorio Programado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (activo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (activo) "Canal Urgente" else "10 segundos",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (activo) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Indicador Circular Animado (Reloj con segundos en el centro)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(170.dp)
                    .padding(4.dp)
            ) {
                val trackColor = MaterialTheme.colorScheme.surfaceVariant
                val primaryColor = MaterialTheme.colorScheme.primary
                val secondaryColor = MaterialTheme.colorScheme.secondary

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    val arcSize = Size(diameter, diameter)

                    // Anillo de fondo
                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Arco de progreso animado
                    if (activo || progressAnimado > 0f) {
                        val sweepAngle = 360f * progressAnimado
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(primaryColor, secondaryColor, primaryColor)
                            ),
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                }

                // Texto en el centro del indicador circular
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (activo) String.format(Locale.getDefault(), "00:00:%02d", segundosRestantes) else "00:00:10",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (activo) "$segundosRestantes s restantes" else "Listo para iniciar",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onIniciar,
                    enabled = !activo,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Iniciar 10 s")
                }

                OutlinedButton(
                    onClick = onCancelar,
                    enabled = activo,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
private fun TarjetaReceptores(
    avion: Boolean,
    cargador: Boolean,
    onCambiarAvion: (Boolean) -> Unit,
    onCambiarCargador: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RssFeed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Receptores del Sistema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            InterruptorDispositivo(
                icono = Icons.Default.Flight,
                titulo = "Modo Avión",
                detalle = "Detecta cuando se activa o desactiva el modo avión",
                activo = avion,
                alCambiar = onCambiarAvion
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            InterruptorDispositivo(
                icono = Icons.Default.Bolt,
                titulo = "Cargador",
                detalle = "Detecta conexión y desconexión de energía",
                activo = cargador,
                alCambiar = onCambiarCargador
            )
        }
    }
}

@Composable
private fun InterruptorDispositivo(
    icono: ImageVector,
    titulo: String,
    detalle: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (activo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (activo) Color(0xFF10B981).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (activo) "MONITOREANDO" else "INACTIVO",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activo) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = detalle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = activo,
            onCheckedChange = alCambiar
        )
    }
}

@Composable
private fun TarjetaTema(
    modoTema: ModoTema,
    alCambiarTema: (ModoTema) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Apariencia y Tema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Selecciona la apariencia preferida para la interfaz de AvisosCampus.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OpcionTemaCard(
                    titulo = "Automático",
                    icono = Icons.Default.BrightnessAuto,
                    seleccionado = modoTema == ModoTema.AUTOMATICO,
                    onClick = { alCambiarTema(ModoTema.AUTOMATICO) },
                    modifier = Modifier.weight(1f)
                )

                OpcionTemaCard(
                    titulo = "Claro",
                    icono = Icons.Default.LightMode,
                    seleccionado = modoTema == ModoTema.CLARO,
                    onClick = { alCambiarTema(ModoTema.CLARO) },
                    modifier = Modifier.weight(1f)
                )

                OpcionTemaCard(
                    titulo = "Oscuro",
                    icono = Icons.Default.DarkMode,
                    seleccionado = modoTema == ModoTema.OSCURO,
                    onClick = { alCambiarTema(ModoTema.OSCURO) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun OpcionTemaCard(
    titulo: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = if (seleccionado) borderStroke(MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                color = if (seleccionado) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun borderStroke(color: Color) = androidx.compose.foundation.BorderStroke(2.dp, color)

@Composable
private fun TarjetaGestionPermisos(
    permiso: PermisoUi,
    contexto: Context,
    lanzador: androidx.activity.result.ActivityResultLauncher<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Permisos del Sistema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            val (estadoTexto, estadoColor) = when (permiso) {
                PermisoUi.Concedido -> "Concedido" to Color(0xFF10B981)
                PermisoUi.Denegado -> "Denegado" to MaterialTheme.colorScheme.error
                PermisoUi.SinPedir -> "Sin solicitar" to Color(0xFFF59E0B)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Estado actual:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = estadoTexto,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = estadoColor
                )
            }

            Button(
                onClick = {
                    if (Notificador.tienePermiso(contexto)) {
                        // Permiso ya disponible
                    } else if (Build.VERSION.SDK_INT >= 33) {
                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Solicitar permiso de notificaciones")
            }

            if (permiso == PermisoUi.Denegado) {
                OutlinedButton(
                    onClick = { abrirAjustes(contexto) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abrir ajustes de Android")
                }
            }
        }
    }
}

@Composable
private fun TarjetaInfoCanales() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Canales Configurados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ItemCanalInfo(
                    nombre = "Avisos (Prioridad Normal)",
                    descripcion = "Canal principal para notificaciones de entregas e información general."
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                ItemCanalInfo(
                    nombre = "Urgentes (Prioridad Alta)",
                    descripcion = "Canal prioritario con estilo BigTextStyle para recordatorios programados."
                )
            }
        }
    }
}

@Composable
private fun ItemCanalInfo(nombre: String, descripcion: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = nombre,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TarjetaAcercaDe() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "AvisosCampus v1.0",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Semana 15 · Arquitectura MVVM + Repository",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Desarrollado con Jetpack Compose y Material 3",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun abrirAjustes(contexto: Context) {
    contexto.startActivity(
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
        }
    )
}
