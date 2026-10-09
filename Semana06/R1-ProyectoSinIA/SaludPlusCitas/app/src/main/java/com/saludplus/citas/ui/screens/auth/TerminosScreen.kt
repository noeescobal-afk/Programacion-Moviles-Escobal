package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulTerminos = Color(0xFF246BFD)
private val AzulClaroTerminos = Color(0xFFE8F0FF)
private val FondoTerminos = Color(0xFFF5F7FB)

@Composable
fun TerminosScreen(
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { BarraSuperior(titulo = "Términos y condiciones", onVolverAtras = onVolverAtras) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AzulClaroTerminos),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(60.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.HealthAndSafety, null, tint = AzulTerminos, modifier = Modifier.size(32.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("Clínica SaludPlus", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = AzulTerminos)
                    Spacer(Modifier.height(3.dp))
                    Text("Términos y condiciones de uso", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTerminos),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Acerca de este prototipo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    Text("SaludPlus es un prototipo académico y demostrativo para la gestión de citas médicas y consulta de información de salud. Al utilizarlo, aceptas las pautas descritas a continuación.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(14.dp))
            SeccionTerminos("1. Uso de la aplicación", "SaludPlus está diseñada exclusivamente con fines educativos y de demostración. La información mostrada tiene propósito simulado y no sustituye el criterio médico profesional ni consultas de emergencia.")
            SeccionTerminos("2. Información del paciente", "El paciente es responsable de ingresar datos correctos al momento del registro e inicio de sesión para garantizar la correcta identificación en la gestión de sus citas.")
            SeccionTerminos("3. Gestión de citas", "Las reservas se realizan en función de los médicos y horarios disponibles cargados en la aplicación. Cada reserva asigna un turno exclusivo dentro del prototipo.")
            SeccionTerminos("4. Cancelación de citas", "El paciente puede cancelar sus citas desde la sección correspondiente. La cancelación libera el horario seleccionado para que pueda ser reservado nuevamente.")
            SeccionTerminos("5. Privacidad", "Los datos ingresados se administran únicamente en memoria durante la ejecución actual de la aplicación. No se envían ni almacenan en servidores ni bases de datos externas.", privacidad = true)
            SeccionTerminos("6. Responsabilidad del usuario", "El usuario se compromete a realizar un uso adecuado de las funcionalidades de agendamiento y respetuoso de las simulaciones presentadas.")

            Spacer(Modifier.height(10.dp))
            BotonPrincipal(texto = "Volver", onClick = onVolverAtras, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SeccionTerminos(titulo: String, contenido: String, privacidad: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTerminos),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (privacidad) {
                Icon(Icons.Filled.PrivacyTip, null, tint = AzulTerminos, modifier = Modifier.size(24.dp))
                Spacer(Modifier.height(8.dp))
            }
            Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AzulTerminos)
            Spacer(Modifier.height(5.dp))
            Text(contenido, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TerminosScreenPreview() {
    SaludPlusCitasTheme {
        TerminosScreen(onVolverAtras = {})
    }
}
