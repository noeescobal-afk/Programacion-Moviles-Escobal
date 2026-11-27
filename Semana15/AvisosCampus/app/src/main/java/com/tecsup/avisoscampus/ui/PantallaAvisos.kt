package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel
import com.tecsup.avisoscampus.notificacion.Notificador

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            Notificador.mostrar(contexto, aviso)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565A0),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Tarjeta("Permiso de notificaciones") {
                Text("Estado: Sin pedir")
                Button(onClick = {}, enabled = false) {
                    Text("Permitir notificaciones")
                }
            }

            Tarjeta("Avisos") {
                Button(onClick = {}, enabled = false) {
                    Text("Mostrar aviso")
                }
            }

            Tarjeta("Recordatorio") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {}, enabled = false) {
                        Text("Recordar en 10 s")
                    }
                    OutlinedButton(onClick = {}, enabled = false) {
                        Text("Cancelar")
                    }
                }
            }

            Tarjeta("Receptor de anuncios") {
                Row {
                    Text("Escuchar el modo avión", Modifier.weight(1f))
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        enabled = false
                    )
                }
            }

            Tarjeta("Historial") {
                Text("Sin avisos todavía")
            }
        }
    }
}

@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                titulo,
                color = Color(0xFF1565A0),
                style = MaterialTheme.typography.titleMedium
            )
            contenido()
        }
    }
}
