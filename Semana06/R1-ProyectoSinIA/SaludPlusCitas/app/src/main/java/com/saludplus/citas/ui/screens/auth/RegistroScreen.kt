package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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

private val AzulRegistro = Color(0xFF246BFD)
private val AzulClaroRegistro = Color(0xFFE8F0FF)

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onIniciarSesion: () -> Unit,
    onVerTerminos: () -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior(titulo = "Crear cuenta", onVolverAtras = onVolverAtras) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(76.dp).background(AzulClaroRegistro, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.HealthAndSafety, null, tint = AzulRegistro, modifier = Modifier.size(38.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(5.dp))
            Text("Regístrate para agendar y gestionar tus citas médicas.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it; if (mensajeError.isNotEmpty()) mensajeError = "" }, label = { Text("Nombre completo") }, leadingIcon = { Icon(Icons.Filled.Person, null) }, singleLine = true, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(13.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it; if (mensajeError.isNotEmpty()) mensajeError = "" }, label = { Text("Correo electrónico") }, leadingIcon = { Icon(Icons.Filled.Email, null) }, singleLine = true, shape = RoundedCornerShape(14.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(13.dp))
            OutlinedTextField(value = password, onValueChange = { password = it; if (mensajeError.isNotEmpty()) mensajeError = "" }, label = { Text("Contraseña") }, leadingIcon = { Icon(Icons.Filled.Lock, null) }, singleLine = true, shape = RoundedCornerShape(14.dp), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(13.dp))
            OutlinedTextField(value = confirmarPassword, onValueChange = { confirmarPassword = it; if (mensajeError.isNotEmpty()) mensajeError = "" }, label = { Text("Confirmar contraseña") }, leadingIcon = { Icon(Icons.Filled.Lock, null) }, singleLine = true, shape = RoundedCornerShape(14.dp), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())

            if (mensajeError.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(mensajeError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(22.dp))
            BotonPrincipal(
                texto = "Crear cuenta",
                onClick = {
                    val nombreTrim = nombre.trim()
                    val correoTrim = correo.trim()

                    if (nombreTrim.isEmpty() || correoTrim.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
                        mensajeError = "Por favor, completa todos los campos"
                        return@BotonPrincipal
                    }
                    if (!correoTrim.contains("@") || !correoTrim.contains(".")) {
                        mensajeError = "Por favor, ingresa un correo electrónico válido"
                        return@BotonPrincipal
                    }
                    if (password.length < 6) {
                        mensajeError = "La contraseña debe tener al menos 6 caracteres"
                        return@BotonPrincipal
                    }
                    if (password != confirmarPassword) {
                        mensajeError = "Las contraseñas no coinciden"
                        return@BotonPrincipal
                    }

                    val registrado = Repositorio.registrarUsuario(nombre = nombreTrim, correo = correoTrim, password = password)
                    if (registrado) {
                        mensajeError = ""
                        onRegistroExitoso()
                    } else {
                        mensajeError = "El correo ya se encuentra registrado o no fue posible completar el registro"
                    }
                }
            )
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onIniciarSesion) {
                Text("¿Ya tienes una cuenta? Iniciar sesión", color = AzulRegistro, fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = onVerTerminos) {
                Text("Ver términos y condiciones", style = MaterialTheme.typography.labelMedium, color = AzulRegistro)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    SaludPlusCitasTheme {
        RegistroScreen(onRegistroExitoso = {}, onIniciarSesion = {}, onVerTerminos = {}, onVolverAtras = {})
    }
}
