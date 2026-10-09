package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulAuth = Color(0xFF246BFD)
private val AzulClaroAuth = Color(0xFFE8F0FF)
private val VerdeAuth = Color(0xFF168A65)

@Composable
fun SplashScreen(
    onCrearCuenta: () -> Unit,
    onIniciarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(112.dp).background(AzulClaroAuth, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(78.dp).background(AzulAuth, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.HealthAndSafety,
                    contentDescription = "SaludPlus",
                    tint = Color.White,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Clínica SaludPlus", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = AzulAuth, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tu salud, más cerca de ti", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = VerdeAuth, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Agenda y gestiona tus citas médicas de forma rápida, segura y sencilla.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(44.dp))
        Button(
            onClick = onCrearCuenta,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulAuth, contentColor = Color.White)
        ) {
            Text("Crear cuenta", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = onIniciarSesion) {
            Text("Ya tengo una cuenta · Iniciar sesión", color = AzulAuth, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SaludPlusCitasTheme {
        SplashScreen(onCrearCuenta = {}, onIniciarSesion = {})
    }
}
