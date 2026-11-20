package com.tecsup.centrotareas.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel

// Colores Dark Theme
private val DarkBg = Color(0xFF080D1D)
private val DarkCardBg = Color(0xFF172442)
private val DarkCardBorder = Color(0xFF263B6B)
private val DarkTextMain = Color(0xFFFFFFFF)
private val DarkTextSec = Color(0xFF94A3B8)
private val DarkSuccessBg = Color(0xFF0A2E1F)
private val DarkSuccessFg = Color(0xFF56F0DC)
private val DarkErrorBg = Color(0xFF3B1414)
private val DarkErrorFg = Color(0xFFFF8A8A)

// Colores Light Theme (Variant Premium)
private val LightBg = Color(0xFFF1F5FF)
private val LightCardBg = Color(0xFFFFFFFF)
private val LightCardBorder = Color(0xFFCBD5E1)
private val LightTextMain = Color(0xFF172442)
private val LightTextSec = Color(0xFF64748B)
private val LightSuccessBg = Color(0xFFD1FAE5)
private val LightSuccessFg = Color(0xFF065F46)
private val LightErrorBg = Color(0xFFFEE2E2)
private val LightErrorFg = Color(0xFF991B1B)

// Acentos adaptativos con alto contraste garantizado
private val ElectricBlue = Color(0xFF527BFF)
private val TechViolet = Color(0xFFA996FF)
private val LightViolet = Color(0xFF6D28D9)
private val MintGreen = Color(0xFF56F0DC)
private val LightTurquoise = Color(0xFF00796B) // Alto contraste en modo claro

@Composable
fun PantallaTareas(
    vm: TareasViewModel,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val intento by vm.intento.collectAsState()
    val secuenciales by vm.tiemposSecuenciales.collectAsState()
    val paralelos by vm.tiemposParalelos.collectAsState()
    var toques by remember { mutableIntStateOf(0) }
    var numero by remember { mutableStateOf("500000") }

    val screenBg = if (isDarkMode) DarkBg else LightBg
    val textMain = if (isDarkMode) DarkTextMain else LightTextMain
    val textSec = if (isDarkMode) DarkTextSec else LightTextSec
    val turquoiseColor = if (isDarkMode) MintGreen else LightTurquoise

    val animatedProgress by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 300),
        label = "progreso"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado espectacular con selector de tema
            val headerBrush = if (isDarkMode) {
                Brush.horizontalGradient(listOf(ElectricBlue, TechViolet))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF7C3AED)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBrush)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "TECSUP · SEMANA 14",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "CentroTareas",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Controles derechos: Toggle Tema + Indicador Activo
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.Black.copy(alpha = 0.25f),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                            ) {
                                IconButton(
                                    onClick = onThemeToggle,
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Text(
                                        text = if (isDarkMode) "☀️" else "🌙",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }

                            Surface(
                                color = Color.Black.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MintGreen.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(MintGreen)
                                    )
                                    Text(
                                        text = "Activo",
                                        color = MintGreen,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Laboratorio de concurrencia y corrutinas en tiempo real",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Panel de estadísticas (2 tarjetas pequeñas)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val primosText = when (val p = primos) {
                        is PrimosUi.Listo -> "${p.cantidad} primos (${p.milisegundos}ms)"
                        PrimosUi.Calculando -> "Calculando..."
                        else -> "Sin datos"
                    }
                    val mediaSec = if (secuenciales.isEmpty()) null else secuenciales.average()
                    val mediaPar = if (paralelos.isEmpty()) null else paralelos.average()
                    val ahorroText = if (mediaSec != null && mediaPar != null && mediaSec > 0) {
                        val ah = ((mediaSec - mediaPar) / mediaSec * 100).coerceIn(0.0, 100.0)
                        "%.1f%% más rápido".format(ah)
                    } else {
                        "Sin datos"
                    }

                    StatCard(
                        title = "Último Cálculo Primos",
                        value = primosText,
                        modifier = Modifier.weight(1f),
                        accentColor = turquoiseColor,
                        isDarkMode = isDarkMode
                    )
                    StatCard(
                        title = "Ahorro Concurrente",
                        value = ahorroText,
                        modifier = Modifier.weight(1f),
                        accentColor = if (isDarkMode) TechViolet else LightViolet,
                        isDarkMode = isDarkMode
                    )
                }

                // Tarjeta 01: Tarea con progreso
                DashboardCard("01  Tarea con progreso", isDarkMode = isDarkMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { toques++ },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tócame: $toques")
                        }
                        Text(
                            text = if (corriendo) "Ejecutando hilo..." else "Interfaz fluida",
                            style = MaterialTheme.typography.bodySmall,
                            color = textSec
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textMain
                    )

                    // Barra de progreso con gradiente visual
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Progreso de tarea", style = MaterialTheme.typography.labelSmall, color = textSec)
                            Text("${(animatedProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = turquoiseColor)
                        }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDarkMode) Color(0xFF0F1A30) else Color(0xFFE2E8F0))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Brush.horizontalGradient(listOf(ElectricBlue, TechViolet, turquoiseColor)))
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = vm::tareaLarga,
                            enabled = !corriendo,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Iniciar")
                        }

                        // Botón Cancelar con contraste corregido
                        OutlinedButton(
                            onClick = vm::cancelar,
                            enabled = corriendo,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (corriendo) {
                                    if (isDarkMode) Color(0xFF4A1515) else Color(0xFFFEE2E2)
                                } else {
                                    if (isDarkMode) Color(0xFF1E2A47) else Color(0xFFE2E8F0)
                                },
                                contentColor = if (corriendo) {
                                    if (isDarkMode) Color(0xFFFF8A8A) else Color(0xFFDC2626)
                                } else {
                                    if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                                },
                                disabledContainerColor = if (isDarkMode) Color(0xFF1E2A47) else Color(0xFFE2E8F0),
                                disabledContentColor = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (corriendo) {
                                    if (isDarkMode) Color(0xFFFF5252) else Color(0xFFEF4444)
                                } else {
                                    if (isDarkMode) Color(0xFF2E3E60) else Color(0xFFCBD5E1)
                                }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cancelar")
                        }
                    }
                }

                // Tarjeta 02: Contar números primos
                DashboardCard("02  Contar números primos", isDarkMode = isDarkMode) {
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { if (it.length <= 9 && it.all(Char::isDigit)) numero = it },
                        label = { Text("N (límite superior)", color = textSec) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = if (isDarkMode) DarkCardBorder else LightCardBorder,
                            focusedTextColor = textMain,
                            unfocusedTextColor = textMain,
                            focusedContainerColor = if (isDarkMode) Color(0xFF0F1A30) else Color(0xFFF8FAFC),
                            unfocusedContainerColor = if (isDarkMode) Color(0xFF0F1A30) else Color(0xFFF8FAFC)
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { numero.toIntOrNull()?.let(vm::contar) },
                            enabled = primos != PrimosUi.Calculando && (numero.toIntOrNull() ?: 0) >= 2,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Contar")
                        }

                        // Botón Cancelar Primos
                        val isPrimosRunning = primos == PrimosUi.Calculando
                        OutlinedButton(
                            onClick = vm::cancelarPrimos,
                            enabled = isPrimosRunning,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isPrimosRunning) {
                                    if (isDarkMode) Color(0xFF4A1515) else Color(0xFFFEE2E2)
                                } else {
                                    if (isDarkMode) Color(0xFF1E2A47) else Color(0xFFE2E8F0)
                                },
                                contentColor = if (isPrimosRunning) {
                                    if (isDarkMode) Color(0xFFFF8A8A) else Color(0xFFDC2626)
                                } else {
                                    if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                                },
                                disabledContainerColor = if (isDarkMode) Color(0xFF1E2A47) else Color(0xFFE2E8F0),
                                disabledContentColor = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isPrimosRunning) {
                                    if (isDarkMode) Color(0xFFFF5252) else Color(0xFFEF4444)
                                } else {
                                    if (isDarkMode) Color(0xFF2E3E60) else Color(0xFFCBD5E1)
                                }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cancelar")
                        }
                    }
                    
                    AnimatedVisibility(visible = primos != PrimosUi.Inicial) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            when (val estado = primos) {
                                PrimosUi.Inicial -> {}
                                PrimosUi.Calculando -> Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = turquoiseColor)
                                    Text("Procesando en Background Dispatcher...", style = MaterialTheme.typography.bodyMedium, color = textSec)
                                }
                                is PrimosUi.Listo -> TechResult("${estado.cantidad} primos encontrados en ${estado.milisegundos} ms", isDarkMode = isDarkMode)
                                PrimosUi.Cancelado -> Text("Cálculo cancelado por el usuario", color = textSec, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // Tarjeta 03: Dos cargas
                DashboardCard("03  Dos cargas (Secuencial vs Paralela)", isDarkMode = isDarkMode) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Simular fallo de red", style = MaterialTheme.typography.bodyMedium, color = textMain, fontWeight = FontWeight.Medium)
                        Switch(
                            checked = simularError,
                            onCheckedChange = vm::cambiarSimularError,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = turquoiseColor,
                                checkedTrackColor = ElectricBlue.copy(alpha = 0.5f),
                                uncheckedThumbColor = textSec,
                                uncheckedTrackColor = if (isDarkMode) Color(0xFF0F1A30) else Color(0xFFE2E8F0)
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = vm::cargarUnaPorUna,
                            enabled = carga != CargaUi.Cargando,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = textMain,
                                disabledContentColor = textSec
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) DarkCardBorder else LightCardBorder),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }
                        Button(
                            onClick = vm::cargarALaVez,
                            enabled = carga != CargaUi.Cargando,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("A la vez")
                        }
                    }

                    AnimatedVisibility(visible = carga != CargaUi.Inicial) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            when (val estado = carga) {
                                CargaUi.Inicial -> {}
                                CargaUi.Cargando -> Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = turquoiseColor)
                                    Text("Cargando datos... (Intento $intento/3 con Backoff)", style = MaterialTheme.typography.bodyMedium, color = textSec)
                                }
                                is CargaUi.Exito -> TechResult("${estado.modo} · ${estado.perfil}\n${estado.pedidos} pedidos procesados · ${estado.milisegundos} ms", isDarkMode = isDarkMode)
                                is CargaUi.Error -> {
                                    val errBg = if (isDarkMode) DarkErrorBg else LightErrorBg
                                    val errFg = if (isDarkMode) DarkErrorFg else LightErrorFg
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(errBg, RoundedCornerShape(12.dp))
                                            .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Icon(Icons.Default.Warning, contentDescription = null, tint = errFg, modifier = Modifier.size(18.dp))
                                            Text(estado.mensaje, color = errFg, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Button(
                                            onClick = vm::cargarALaVez,
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Reintentar operación")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Tarjeta 04: Laboratorio de eficiencia · IA
                DashboardCard("04  Laboratorio de eficiencia · IA", isDarkMode = isDarkMode) {
                    Text(
                        "Análisis comparativo de rendimiento (últimas 5 ejecuciones)",
                        style = MaterialTheme.typography.bodySmall,
                        color = textSec
                    )
                    val mediaSec = if (secuenciales.isEmpty()) null else secuenciales.average()
                    val mediaPar = if (paralelos.isEmpty()) null else paralelos.average()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Secuencial Promedio", style = MaterialTheme.typography.labelSmall, color = textSec)
                            Text(mediaSec?.let { "%.0f ms".format(it) } ?: "Sin datos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = textMain)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Paralela Promedio", style = MaterialTheme.typography.labelSmall, color = textSec)
                            Text(mediaPar?.let { "%.0f ms".format(it) } ?: "Sin datos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = turquoiseColor)
                        }
                    }

                    if (mediaSec != null && mediaPar != null && mediaSec > 0) {
                        val ahorro = ((mediaSec - mediaPar) / mediaSec * 100).coerceIn(0.0, 100.0)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Ahorro de tiempo estimado", style = MaterialTheme.typography.labelSmall, color = textSec)
                                Text("${"%.1f".format(ahorro)}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = turquoiseColor)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(if (isDarkMode) Color(0xFF0F1A30) else Color(0xFFE2E8F0))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth((ahorro / 100).toFloat().coerceIn(0f, 1f))
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(turquoiseColor)
                                )
                            }
                        }
                        TechResult("🚀 Optimización IA: El uso de async/await reduce drásticamente el tiempo total al ejecutar operaciones independientes de red en paralelo sin bloquear hilos.", isDarkMode = isDarkMode)
                    } else {
                        Text(
                            "💡 Tip: Ejecuta ambos modos en la tarjeta '03' para generar la comparativa analítica de eficiencia.",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDarkMode) TechViolet else LightViolet
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "TECSUP · Entorno Académico Concurrente 2026",
                    style = MaterialTheme.typography.labelSmall,
                    color = textSec,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    accentColor: Color,
    isDarkMode: Boolean
) {
    val cardBg = if (isDarkMode) DarkCardBg else LightCardBg
    val cardBorder = if (isDarkMode) DarkCardBorder else LightCardBorder
    val textSec = if (isDarkMode) DarkTextSec else LightTextSec

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = cardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = textSec)
            Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = accentColor)
        }
    }
}

@Composable
private fun DashboardCard(
    titulo: String,
    isDarkMode: Boolean,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val cardBg = if (isDarkMode) DarkCardBg else LightCardBg
    val cardBorder = if (isDarkMode) DarkCardBorder else LightCardBorder
    val textMain = if (isDarkMode) DarkTextMain else LightTextMain

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(22.dp),
        color = cardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
        shadowElevation = if (isDarkMode) 4.dp else 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = titulo,
                    color = textMain,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ElectricBlue)
                )
            }
            HorizontalDivider(color = cardBorder, thickness = 1.dp)
            contenido()
        }
    }
}

@Composable
private fun TechResult(texto: String, isDarkMode: Boolean) {
    val successBg = if (isDarkMode) DarkSuccessBg else LightSuccessBg
    val successFg = if (isDarkMode) DarkSuccessFg else LightSuccessFg
    val iconTint = if (isDarkMode) MintGreen else Color(0xFF059669)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = successBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, iconTint.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = texto,
                color = successFg,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
