package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulHeroDeep = Color(0xFF0F2B5B)
private val AzulElectric = Color(0xFF246BFD)
private val AzulIce = Color(0xFFEBF2FE)

@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onCrearCuenta: () -> Unit,
    onVolverAtras: () -> Unit,
    modifier: Modifier = Modifier
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarPassword by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var usuarioPendiente by remember { mutableStateOf<Usuario?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AzulHeroDeep)
    ) {
        // Elementos gráficos decorativos de la Hero Section
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = size.width * 0.45f,
                center = Offset(size.width * 0.85f, size.height * 0.12f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.04f),
                radius = size.width * 0.35f,
                center = Offset(size.width * 0.1f, size.height * 0.28f)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // HERO SECTION (38% alto)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.38f)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.HealthAndSafety,
                                contentDescription = null,
                                tint = AzulHeroDeep,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "SaludPlus",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tu salud, más cerca de ti",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // FORMULARIO SUPERPUESTO
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.62f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido de nuevo!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Inicia sesión para gestionar tus citas médicas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Campo de Correo
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            if (mensajeError.isNotEmpty()) mensajeError = ""
                        },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                tint = AzulElectric
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectric,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            focusedContainerColor = AzulIce.copy(alpha = 0.3f)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (mensajeError.isNotEmpty()) mensajeError = ""
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = AzulElectric
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                                Icon(
                                    imageVector = if (mostrarPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = "Mostrar contraseña",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectric,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            focusedContainerColor = AzulIce.copy(alpha = 0.3f)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mensajeError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = mensajeError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Botón de Ingreso
                    Button(
                        onClick = {
                            val correoTrim = correo.trim()
                            if (correoTrim.isEmpty() || password.isEmpty()) {
                                mensajeError = "Completa tu correo y contraseña"
                                return@Button
                            }
                            val usuarioValido = Repositorio.validarCredenciales(correo = correoTrim, password = password)
                            if (usuarioValido != null) {
                                mensajeError = ""
                                usuarioPendiente = usuarioValido
                            } else {
                                mensajeError = "Correo o contraseña incorrectos"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulElectric,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ingresar",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onCrearCuenta) {
                        Text(
                            text = "¿No tienes una cuenta? Crear cuenta",
                            style = MaterialTheme.typography.labelLarge,
                            color = AzulElectric,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // Diálogo M3 de confirmación
    if (usuarioPendiente != null) {
        AlertDialog(
            onDismissRequest = {
                usuarioPendiente = null
            },
            title = {
                Text(
                    text = "Confirmar inicio de sesión",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Deseas ingresar a SaludPlus?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val usuario = usuarioPendiente
                        if (usuario != null) {
                            Repositorio.establecerUsuarioActual(usuario)
                            usuarioPendiente = null
                            onLoginExitoso()
                        }
                    }
                ) {
                    Text(
                        text = "Ingresar",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulElectric
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        usuarioPendiente = null
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SaludPlusCitasTheme {
        LoginScreen(onLoginExitoso = {}, onCrearCuenta = {}, onVolverAtras = {})
    }
}
