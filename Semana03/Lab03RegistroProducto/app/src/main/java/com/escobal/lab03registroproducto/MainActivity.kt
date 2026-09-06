package com.escobal.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegistroProductoScreen()
        }
    }
}


@Composable
fun RegistroProductoScreen() {

    var nombre by remember {
        mutableStateOf("")
    }

    var precio by remember {
        mutableStateOf("")
    }

    var cantidad by remember {
        mutableStateOf("")
    }

    var importe by remember {
        mutableStateOf(0.0)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Top
    ) {


        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineLarge
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Text(
            text = "Complete los datos del producto para registrarlo",
            style = MaterialTheme.typography.bodyMedium
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            label = {
                Text("Nombre del producto")
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        OutlinedTextField(
            value = precio,
            onValueChange = {
                precio = it
            },
            label = {
                Text("Precio")
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        OutlinedTextField(
            value = cantidad,
            onValueChange = {
                cantidad = it
            },
            label = {
                Text("Cantidad")
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        Button(
            onClick = {

                importe =
                    (precio.toDoubleOrNull() ?: 0.0) *
                            (cantidad.toIntOrNull() ?: 0)

            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "AGREGAR PRODUCTO"
            )

        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {


            Column(
                modifier = Modifier.padding(20.dp)
            ) {


                Text(
                    text = "Resumen del producto",
                    style = MaterialTheme.typography.titleLarge
                )


                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                Text(
                    text = "Producto: $nombre"
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text = "Precio unitario: S/ $precio"
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text = "Cantidad: $cantidad"
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Text(
                    text = "Total: S/ %.2f".format(importe),
                    style = MaterialTheme.typography.titleMedium
                )

            }

        }

    }

}