package com.tecsup.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF1456A0)
private val VerdeFondo = Color(0xFFE4F5E8)
private val VerdeTexto = Color(0xFF236B3A)

@Composable
fun PantallaTareas(vm: TareasViewModel) {
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

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Surface(color = Azul, shadowElevation = 4.dp) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 19.dp)) {
                Text("CentroTareas", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Hilos y corrutinas · Semana 14", color = Color(0xFFD7E8FF), style = MaterialTheme.typography.bodySmall)
            }
        }
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { toques++ }) { Text("Tócame: $toques") }
                Text("Prueba de vida de la interfaz", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Tarjeta("01  Tarea con progreso") {
                Text(mensaje, style = MaterialTheme.typography.bodyMedium)
                LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth().height(9.dp))
                Text("${(progreso * 100).toInt()} %", color = Azul, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = vm::tareaLarga, enabled = !corriendo) { Text("Iniciar") }
                    OutlinedButton(onClick = vm::cancelar, enabled = corriendo) { Text("Cancelar") }
                }
            }
            Tarjeta("02  Contar números primos") {
                OutlinedTextField(
                    value = numero, onValueChange = { if (it.length <= 9 && it.all(Char::isDigit)) numero = it },
                    label = { Text("N (límite superior)") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { numero.toIntOrNull()?.let(vm::contar) }, enabled = primos != PrimosUi.Calculando && (numero.toIntOrNull() ?: 0) >= 2) { Text("Contar") }
                    OutlinedButton(onClick = vm::cancelarPrimos, enabled = primos == PrimosUi.Calculando) { Text("Cancelar") }
                }
                when (val estado = primos) {
                    PrimosUi.Inicial -> Text("Ingresa un número y pulsa Contar")
                    PrimosUi.Calculando -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CircularProgressIndicator(Modifier.size(19.dp), strokeWidth = 2.dp)
                        Text("Calculando sin congelar la pantalla...")
                    }
                    is PrimosUi.Listo -> Resultado("${estado.cantidad} primos encontrados en ${estado.milisegundos} ms")
                    PrimosUi.Cancelado -> Text("Cálculo cancelado")
                }
            }
            Tarjeta("03  Dos cargas") {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Simular error")
                    Switch(checked = simularError, onCheckedChange = vm::cambiarSimularError)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = vm::cargarUnaPorUna, enabled = carga != CargaUi.Cargando) { Text("Una por una") }
                    Button(onClick = vm::cargarALaVez, enabled = carga != CargaUi.Cargando) { Text("A la vez") }
                }
                when (val estado = carga) {
                    CargaUi.Inicial -> Text("Compara ejecución secuencial y paralela")
                    CargaUi.Cargando -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CircularProgressIndicator(Modifier.size(19.dp), strokeWidth = 2.dp)
                        Text("Cargando datos simulados... (intento $intento/3)")
                    }
                    is CargaUi.Exito -> Resultado("${estado.modo} · ${estado.perfil}\n${estado.pedidos} pedidos · ${estado.milisegundos} ms")
                    is CargaUi.Error -> Column(Modifier.fillMaxWidth().background(Color(0xFFFCE8E8), RoundedCornerShape(12.dp)).padding(12.dp)) {
                        Text(estado.mensaje, color = Color(0xFFAA2020))
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = vm::cargarALaVez) { Text("Reintentar") }
                    }
                }
            }
            // Mejora 3 + detalle innovador: tablero de eficiencia sobre las mediciones reales.
            Tarjeta("04  Laboratorio de eficiencia · IA") {
                Text("Comparación de hasta 5 mediciones por modalidad", style = MaterialTheme.typography.bodySmall)
                val mediaSec = if (secuenciales.isEmpty()) null else secuenciales.average()
                val mediaPar = if (paralelos.isEmpty()) null else paralelos.average()
                Text("Secuencial: " + (mediaSec?.let { "%.0f ms".format(it) } ?: "Sin mediciones"))
                Text("Paralela: " + (mediaPar?.let { "%.0f ms".format(it) } ?: "Sin mediciones"))
                if (mediaSec != null && mediaPar != null && mediaSec > 0) {
                    val ahorro = ((mediaSec - mediaPar) / mediaSec * 100).coerceIn(0.0, 100.0)
                    LinearProgressIndicator(progress = { (ahorro / 100).toFloat() }, modifier = Modifier.fillMaxWidth())
                    Resultado("Ahorro estimado de tiempo: ${"%.1f".format(ahorro)} %")
                } else Text("Ejecuta ambas cargas para comparar el ahorro real.")
            }
            Text("Datos ficticios · Sin conexión a Internet", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(3.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, color = Azul, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            HorizontalDivider(color = Color(0xFFE5EBF4))
            contenido()
        }
    }
}

@Composable
private fun Resultado(texto: String) {
    Text(texto, modifier = Modifier.fillMaxWidth().background(VerdeFondo, RoundedCornerShape(12.dp)).padding(12.dp), color = VerdeTexto, style = MaterialTheme.typography.bodyMedium)
}
