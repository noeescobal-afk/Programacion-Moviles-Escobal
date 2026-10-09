package com.saludplus.citas.ui.screens.auth

import android.util.Patterns
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(AzulClaroRegistro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.HealthAndSafety,
                    contentDescription = null,
                    tint = AzulRegistro,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Regístrate para agendar y gestionar tus citas médicas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nuevoValor ->
                    val caracteresPermitidos = nuevoValor.all {
                        it.isLetter() ||
                                it.isWhitespace() ||
                                it == '-' ||
                                it == '\''
                    }

                    if (caracteresPermitidos) {
                        nombre = nuevoValor
                    }

                    if (mensajeError.isNotEmpty()) {
                        mensajeError = ""
                    }
                },
                label = { Text("Nombre completo") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(13.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { nuevoValor ->
                    // El correo no debe contener espacios.
                    correo = nuevoValor.filterNot { it.isWhitespace() }
                    if (mensajeError.isNotEmpty()) {
                        mensajeError = ""
                    }
                },
                label = { Text("Correo electrónico") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(13.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { nuevoValor ->
                    // Evita espacios en la contraseña desde la entrada.
                    password = nuevoValor.filterNot { it.isWhitespace() }
                    if (mensajeError.isNotEmpty()) {
                        mensajeError = ""
                    }
                },
                label = { Text("Contraseña") },
                supportingText = {
                    Text("Mínimo 6 caracteres, con letras y números")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(13.dp))

            OutlinedTextField(
                value = confirmarPassword,
                onValueChange = { nuevoValor ->
                    confirmarPassword =
                        nuevoValor.filterNot { it.isWhitespace() }

                    if (mensajeError.isNotEmpty()) {
                        mensajeError = ""
                    }
                },
                label = { Text("Confirmar contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (mensajeError.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(22.dp))

            BotonPrincipal(
                texto = "Crear cuenta",
                onClick = {
                    val nombreTrim =
                        nombre.trim().replace(Regex("\\s+"), " ")
                    val correoTrim = correo.trim().lowercase()

                    val nombreValido = Regex(
                        "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+(?:[ '-][A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)+$"
                    )

                    when {
                        nombreTrim.isEmpty() ||
                                correoTrim.isEmpty() ||
                                password.isEmpty() ||
                                confirmarPassword.isEmpty() -> {
                            mensajeError =
                                "Por favor, completa todos los campos"
                        }

                        !nombreValido.matches(nombreTrim) -> {
                            mensajeError =
                                "Ingresa tu nombre y apellido usando únicamente letras"
                        }

                        !Patterns.EMAIL_ADDRESS
                            .matcher(correoTrim)
                            .matches() -> {
                            mensajeError =
                                "Ingresa un correo electrónico válido"
                        }

                        password.length < 6 -> {
                            mensajeError =
                                "La contraseña debe tener al menos 6 caracteres"
                        }

                        password.none { it.isLetter() } ||
                                password.none { it.isDigit() } -> {
                            mensajeError =
                                "La contraseña debe contener letras y números"
                        }

                        password != confirmarPassword -> {
                            mensajeError =
                                "Las contraseñas no coinciden"
                        }

                        Repositorio.obtenerUsuarios().any {
                            it.correo.equals(
                                correoTrim,
                                ignoreCase = true
                            )
                        } -> {
                            mensajeError =
                                "Este correo electrónico ya está registrado"
                        }

                        else -> {
                            val registrado =
                                Repositorio.registrarUsuario(
                                    nombre = nombreTrim,
                                    correo = correoTrim,
                                    password = password
                                )

                            if (registrado) {
                                mensajeError = ""
                                onRegistroExitoso()
                            } else {
                                mensajeError =
                                    "No fue posible completar el registro. Revisa los datos ingresados."
                            }
                        }
                    }
                }
            )

            Spacer(Modifier.height(10.dp))

            TextButton(onClick = onIniciarSesion) {
                Text(
                    text = "¿Ya tienes una cuenta? Iniciar sesión",
                    color = AzulRegistro,
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(onClick = onVerTerminos) {
                Text(
                    text = "Ver términos y condiciones",
                    style = MaterialTheme.typography.labelMedium,
                    color = AzulRegistro
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
