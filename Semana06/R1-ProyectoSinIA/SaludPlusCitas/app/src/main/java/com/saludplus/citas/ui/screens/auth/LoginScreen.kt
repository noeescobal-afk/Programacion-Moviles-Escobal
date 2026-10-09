package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulLogin = Color(0xFF246BFD)
private val AzulClaroLogin = Color(0xFFE8F0FF)

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onCrearCuenta: () -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Iniciar sesión", onVolverAtras = onVolverAtras) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(82.dp).background(AzulClaroLogin, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.HealthAndSafety, null, tint = AzulLogin, modifier = Modifier.size(42.dp))
            }
            Spacer(Modifier.height(18.dp))
            Text("¡Bienvenido de nuevo!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Ingresa a tu cuenta para gestionar tus citas.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it; if (mensajeError.isNotEmpty()) mensajeError = "" },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Filled.Email, null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; if (mensajeError.isNotEmpty()) mensajeError = "" },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (mensajeError.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(mensajeError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(24.dp))
            BotonPrincipal(
                texto = "Iniciar sesión",
                onClick = {
                    val correoTrim = correo.trim()
                    if (correoTrim.isEmpty() || password.isEmpty()) {
                        mensajeError = "Completa tu correo y contraseña"
                        return@BotonPrincipal
                    }
                    val loginCorrecto = Repositorio.iniciarSesion(correo = correoTrim, password = password)
                    if (loginCorrecto) {
                        mensajeError = ""
                        onLoginExitoso()
                    } else {
                        mensajeError = "Correo o contraseña incorrectos"
                    }
                }
            )
            Spacer(Modifier.height(14.dp))
            TextButton(onClick = onCrearCuenta) {
                Text("¿No tienes una cuenta? Crear cuenta", color = AzulLogin, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SaludPlusCitasTheme {
        LoginScreen(onLoginExitoso = {}, onCrearCuenta = {}, onVolverAtras = {})
    }
}
