package com.escobal.lab03registroproductoconia

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
            RegistroProductoIAScreen()
        }
    }
}


@Composable
fun RegistroProductoIAScreen() {

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


    var mensaje by remember {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {


        Text(
            text = "Registro inteligente de producto",
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Text(
            text = "Versión mejorada con validación automática"
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

                when {

                    nombre.isBlank() -> {
                        mensaje = "⚠️ Ingrese el nombre del producto"
                    }


                    precio.toDoubleOrNull() == null -> {
                        mensaje = "⚠️ El precio debe ser numérico"
                    }


                    cantidad.toIntOrNull() == null -> {
                        mensaje = "⚠️ La cantidad debe ser un número entero"
                    }


                    else -> {

                        importe =
                            precio.toDouble() *
                                    cantidad.toInt()


                        mensaje =
                            "✅ Producto registrado correctamente"

                    }

                }

            },

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(
                text = "VALIDAR Y REGISTRAR"
            )

        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        if (mensaje.isNotEmpty()) {

            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyLarge
            )

        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {


            Column(
                modifier = Modifier.padding(20.dp)
            ) {


                Text(
                    text = "Resumen inteligente",
                    style = MaterialTheme.typography.titleLarge
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Text(
                    text = "Producto: $nombre"
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