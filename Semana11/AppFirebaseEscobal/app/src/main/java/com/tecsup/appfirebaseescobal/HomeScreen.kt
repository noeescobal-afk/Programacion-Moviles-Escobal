
package com.tecsup.appfirebaseescobal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email ?: "Usuario"

    val azul = Color(0xFF0077C8)
    val azulOscuro = Color(0xFF102C56)
    val fondo = Color(0xFFF5F9FE)
    val azulClaro = Color(0xFFD9ECFF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
    ) {

        // CABECERA AZUL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(azul)
        ) {
            // DECORACIÓN DE LA CABECERA
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 95.dp, y = (-110).dp)
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF168DD7))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-100).dp, y = 110.dp)
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0064AD))
            )
        }

        // DECORACIÓN INFERIOR
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 145.dp)
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(45.dp))

            // LOGO TECSUP
            Image(
                painter = painterResource(id = R.drawable.tecsup),
                contentDescription = "Logo Tecsup",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Noe Escobal Huayana",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(45.dp))

            // TARJETA DE BIENVENIDA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ICONO DE USUARIO
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(azulClaro),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "●",
                            fontSize = 44.sp,
                            color = azul
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Bienvenido/a",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = azulOscuro
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Sesión iniciada como:",
                        fontSize = 14.sp,
                        color = Color(0xFF60738F)
                    )

                    Spacer(Modifier.height(12.dp))

                    // CORREO DEL USUARIO AUTENTICADO
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(azulClaro)
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = azulOscuro,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    // CERRAR SESIÓN
                    Button(
                        onClick = {
                            auth.signOut()
                            onLogout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = azul
                        )
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(70.dp))

            // PIE DE PÁGINA
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

            Spacer(Modifier.height(35.dp))
        }
    }
}
