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
            .padding(24.dp)
    ) {


        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "Complete los datos del producto para registrarlo"
        )


        Spacer(
            modifier = Modifier.height(20.dp)
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
            modifier = Modifier.height(20.dp)
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
            modifier = Modifier.height(20.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {


            Column(
                modifier = Modifier.padding(16.dp)
            ) {


                Text(
                    text = "Resumen del producto",
                    style = MaterialTheme.typography.titleMedium
                )


                Spacer(
                    modifier = Modifier.height(10.dp)
                )


                Text(
                    text = "Nombre: $nombre"
                )


                Text(
                    text = "Precio: S/ $precio"
                )


                Text(
                    text = "Cantidad: $cantidad"
                )


                Text(
                    text = "Importe total: S/ %.2f".format(importe)
                )

            }

        }

    }

}
