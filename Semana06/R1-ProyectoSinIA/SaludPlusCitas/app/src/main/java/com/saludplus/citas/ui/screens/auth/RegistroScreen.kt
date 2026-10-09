package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

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
        topBar = {
            BarraSuperior(
                titulo = "Crear cuenta",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Regístrate para agendar tus citas médicas",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Nombre Completo
            CampoTexto(
                value = nombre,
                onValueChange = {
                    nombre = it
                    if (mensajeError.isNotEmpty()) mensajeError = ""
                },
                label = "Nombre completo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Correo Electrónico
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    if (mensajeError.isNotEmpty()) mensajeError = ""
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (mensajeError.isNotEmpty()) mensajeError = ""
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Confirmar Contraseña
            OutlinedTextField(
                value = confirmarPassword,
                onValueChange = {
                    confirmarPassword = it
                    if (mensajeError.isNotEmpty()) mensajeError = ""
                },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            // Mostrar Mensaje de Error si existe
            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Crear cuenta
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

                    val registrado = Repositorio.registrarUsuario(
                        nombre = nombreTrim,
                        correo = correoTrim,
                        password = password
                    )

                    if (registrado) {
                        mensajeError = ""
                        onRegistroExitoso()
                    } else {
                        mensajeError = "El correo ya se encuentra registrado o no fue posible completar el registro"
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Opción Iniciar Sesión
            TextButton(onClick = onIniciarSesion) {
                Text(
                    text = "¿Ya tienes una cuenta? Iniciar sesión",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Términos y Condiciones
            TextButton(onClick = onVerTerminos) {
                Text(
                    text = "Ver términos y condiciones",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    SaludPlusCitasTheme {
        RegistroScreen(
            onRegistroExitoso = {},
            onIniciarSesion = {},
            onVerTerminos = {},
            onVolverAtras = {}
        )
    }
}
