package com.tecsup.campusgps.ui

import androidx.compose.runtime.Composable
import com.tecsup.campusgps.viewmodel.MapaViewModel

/**
 * Delegado temporal de compatibilidad para PantallaMapa hacia TabMapa.
 */
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    TabMapa(vm)
}
