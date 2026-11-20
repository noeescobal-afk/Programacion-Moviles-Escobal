package com.tecsup.campusgps.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tecsup.campusgps.R

/**
 * Bienvenida visual de CampusGPS.
 *
 * La ilustracion ya incluye el logo, los textos y la barra decorativa.
 * No volver a dibujarlos encima: se duplicarian.
 * El cambio a la pantalla principal lo controla MainActivity.
 */
@Composable
fun PantallaBienvenida() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val opacidad by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 650),
        label = "AparicionBienvenidaTecsup"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B4394))
    ) {
        Image(
            painter = painterResource(R.drawable.tecsup_maps_bienvenida),
            contentDescription = "TECSUP MAPS. Explora tu campus. Tecnologia con sentido.",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(opacidad)
        )
    }
}
