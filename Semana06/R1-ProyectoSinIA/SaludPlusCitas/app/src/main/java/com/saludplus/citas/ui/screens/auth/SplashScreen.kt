package com.saludplus.citas.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.R
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme
import kotlinx.coroutines.delay

private val AzulDeepSplash = Color(0xFF0F2B5B)
private val AzulElectricSplash = Color(0xFF246BFD)
private val AzulIceHero = Color(0xFFEBF2FE)

@Composable
fun SplashScreen(
    onCrearCuenta: () -> Unit,
    onIniciarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 0 = Splash Animado, 1 = Pantalla Bienvenida
    var fasePantalla by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(1800)
        fasePantalla = 1
    }

    Crossfade(
        targetState = fasePantalla,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "TransiciónSplashBienvenida",
        modifier = modifier.fillMaxSize()
    ) { fase ->
        if (fase == 0) {
            SplashAnimado()
        } else {
            BienvenidaHeroScreen(
                onCrearCuenta = onCrearCuenta,
                onIniciarSesion = onIniciarSesion
            )
        }
    }
}

@Composable
private fun SplashAnimado() {
    var animarLogo by remember { mutableStateOf(false) }
    var animarTexto by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        animarLogo = true
        delay(350)
        animarTexto = true
    }

    val escalaLogo by animateFloatAsState(
        targetValue = if (animarLogo) 1f else 0.5f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "EscalaLogo"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulDeepSplash),
        contentAlignment = Alignment.Center
    ) {
        // Formas abstractas decorativas de fondo
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.03f),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.1f, size.height * 0.8f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .scale(escalaLogo)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.HealthAndSafety,
                        contentDescription = null,
                        tint = AzulDeepSplash,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = animarTexto,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(500)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SaludPlus",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tu salud, más cerca de ti",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BienvenidaHeroScreen(
    onCrearCuenta: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulIceHero)
    ) {
        // ZONA SUPERIOR: HERO SECTION CON ILUSTRACIÓN Y HEALTH CHIPS (~58%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.58f)
        ) {
            // Fondo decorativo abstracto
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = AzulElectricSplash.copy(alpha = 0.08f),
                    radius = size.width * 0.45f,
                    center = Offset(size.width * 0.9f, size.height * 0.15f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Clínica SaludPlus",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulDeepSplash,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Tu salud, más cerca de ti",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulElectricSplash,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ILUSTRACIÓN PRINCIPAL PROTAGONISTA CON CHIPS FLOTANTES
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ilustracion_saludplus),
                        contentDescription = "Ilustración SaludPlus",
                        modifier = Modifier
                            .fillMaxWidth(0.88f)
                            .height(240.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Floating Health Chip 1: Arriba Izquierda
                    HealthChip(
                        icono = Icons.Filled.CheckCircle,
                        texto = "Atención segura",
                        colorIcono = Color(0xFF168A65),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 10.dp)
                    )

                    // Floating Health Chip 2: Arriba Derecha
                    HealthChip(
                        icono = Icons.Filled.CalendarMonth,
                        texto = "Citas rápidas",
                        colorIcono = AzulElectricSplash,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp)
                    )

                    // Floating Health Chip 3: Abajo Centro
                    HealthChip(
                        icono = Icons.Filled.VerifiedUser,
                        texto = "Datos protegidos",
                        colorIcono = Color(0xFF168A65),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 6.dp)
                    )
                }
            }
        }

        // ZONA INFERIOR: ACCIONES SOBRE SUPERFICIE ELEVADA (~42%)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.42f),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tu salud en un solo lugar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Agenda y gestiona tus citas médicas de forma rápida, segura y sencilla.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onCrearCuenta,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulElectricSplash,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Crear cuenta",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(onClick = onIniciarSesion) {
                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AzulElectricSplash
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthChip(
    icono: ImageVector,
    texto: String,
    colorIcono: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SaludPlusCitasTheme {
        SplashScreen(
            onCrearCuenta = {},
            onIniciarSesion = {}
        )
    }
}
