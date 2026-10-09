package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

@Composable
fun TerminosScreen(
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Términos y condiciones",
                onVolverAtras = onVolverAtras
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Encabezado
            Text(
                text = "SaludPlus",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Términos y condiciones de uso",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Introducción
            Text(
                text = "Bienvenido a SaludPlus. La presente aplicación es un prototipo académico y demostrativo para la gestión de citas médicas y consulta de información de salud. Al utilizar este servicio, aceptas las pautas descritas a continuación.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sección 1
            SeccionTerminos(
                titulo = "1. Uso de la aplicación",
                contenido = "SaludPlus está diseñada exclusivamente con fines educativos y de demostración. La información mostrada tiene propósito simulado y no sustituye el criterio médico profesional ni consultas de emergencia."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección 2
            SeccionTerminos(
                titulo = "2. Información del paciente",
                contenido = "El paciente es responsable de ingresar datos correctos al momento del registro e inicio de sesión para garantizar la correcta identificación en la gestión de sus citas."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección 3
            SeccionTerminos(
                titulo = "3. Gestión de citas",
                contenido = "Las reservas de citas se realizan en función de los médicos y horarios disponibles cargados en la aplicación. Cada reserva asigna un turno exclusivo dentro del prototipo."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección 4
            SeccionTerminos(
                titulo = "4. Cancelación de citas",
                contenido = "El paciente puede cancelar sus citas agendadas desde la sección correspondiente. La cancelación liberará de inmediato el horario seleccionado para que pueda ser reservado nuevamente."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección 5
            SeccionTerminos(
                titulo = "5. Privacidad",
                contenido = "Dado el carácter estrictamente académico de esta aplicación, los datos ingresados se administran únicamente en memoria durante la ejecución actual de la app. No se envían ni almacenan datos en servidores ni bases de datos externas."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección 6
            SeccionTerminos(
                titulo = "6. Responsabilidad del usuario",
                contenido = "El usuario se compromete a realizar un uso adecuado de las funcionalidades de agendamiento y respetuoso de las simulaciones presentadas."
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botón final Volver
            BotonPrincipal(
                texto = "Volver",
                onClick = onVolverAtras,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SeccionTerminos(
    titulo: String,
    contenido: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contenido,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TerminosScreenPreview() {
    SaludPlusCitasTheme {
        TerminosScreen(
            onVolverAtras = {}
        )
    }
}
