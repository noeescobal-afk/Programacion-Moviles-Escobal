
package com.tecsup.appfirebaseescobal

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val azul = Color(0xFF0077C8)
    val azulOscuro = Color(0xFF102C56)
    val fondo = Color(0xFFF5F9FE)
    val azulClaro = Color(0xFFD9ECFF)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
    ) {

        // DECORACIÓN AZUL SUPERIOR
        Box(
            modifier = Modifier
                .offset(x = (-100).dp, y = (-130).dp)
                .size(290.dp)
                .clip(CircleShape)
                .background(azulClaro)
        )

        Box(
            modifier = Modifier
                .offset(x = (-120).dp, y = (-155).dp)
                .size(245.dp)
                .clip(CircleShape)
                .background(azul)
        )

        // DECORACIÓN INFERIOR
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 110.dp, y = 145.dp)
                .size(290.dp)
                .clip(CircleShape)
                .background(azulClaro)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 145.dp, y = 165.dp)
                .size(265.dp)
                .clip(CircleShape)
                .background(azul)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(72.dp))

            // NOMBRE DEL ESTUDIANTE
            Text(
                text = "Noe Escobal Huayana",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = azulOscuro,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // LOGO TECSUP
            Image(
                painter = painterResource(id = R.drawable.tecsup),
                contentDescription = "Logo Tecsup",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(Modifier.height(30.dp))

            // TARJETA DE INICIO DE SESIÓN
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulOscuro
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Accede a tu cuenta para continuar",
                        fontSize = 13.sp,
                        color = Color(0xFF60738F)
                    )

                    Spacer(Modifier.height(26.dp))

                    // CORREO ELECTRÓNICO
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = azul,
                            focusedLabelColor = azul,
                            cursorColor = azul,
                            unfocusedBorderColor = Color(0xFFD1DEED)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))

                    // CONTRASEÑA
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation =
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = azul,
                            focusedLabelColor = azul,
                            cursorColor = azul,
                            unfocusedBorderColor = Color(0xFFD1DEED)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    // BOTÓN INGRESAR
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Complete todos los campos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                isLoading = true

                                auth.signInWithEmailAndPassword(
                                    email.trim(),
                                    password
                                ).addOnCompleteListener { task ->

                                    isLoading = false

                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Inicio exitoso",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        onLoginSuccess()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error: ${task.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = azul
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Ingresar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    HorizontalDivider(
                        color = Color(0xFFE4ECF5)
                    )

                    Spacer(Modifier.height(12.dp))

                    TextButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate aquí",
                            color = azul,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            // PIE DE PÁGINA DEL LABORATORIO
            Text(
                text = "PROGRAMACIÓN MÓVIL",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = azulOscuro
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Juan León S. - Tecsup",
                fontSize = 12.sp,
                color = Color(0xFF60738F)
            )

            Spacer(Modifier.height(36.dp))
        }
    }
}
