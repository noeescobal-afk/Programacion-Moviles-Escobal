package com.tecsup.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.tecsup.avisoscampus.model.PermisoUi
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
    val permiso by vm.permiso.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

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
                val (estado, color) = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir" to Color.Gray
                    PermisoUi.Concedido -> "Permitido" to Color(0xFF2E7D32)
                    PermisoUi.Denegado -> "Denegado" to Color.Red
                }

                Text("Estado: $estado", color = color)

                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= 33) {
                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        vm.permisoRespondido(true)
                    }
                }) {
                    Text("Permitir notificaciones")
                }
            }

            Tarjeta("Avisos") {
                Button(onClick = { vm.avisarAhora() }) {
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
