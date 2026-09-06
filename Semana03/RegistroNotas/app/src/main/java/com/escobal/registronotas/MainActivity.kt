package com.escobal.registronotas

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
            RegistroNotasScreen()
        }
    }
}


@Composable
fun RegistroNotasScreen() {


    var notaFundamentos by remember {
        mutableStateOf(0)
    }


    var notaPOO by remember {
        mutableStateOf(0)
    }


    var notaMoviles by remember {
        mutableStateOf(0)
    }


    var notaBaseDatos by remember {
        mutableStateOf(0)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {


        Text(
            text = "Registro de Notas",
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "Calcula tu promedio ponderado del ciclo"
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Text(
            text = "Fundamentos de Programación (20%)"
        )


        Text(
            text = "Nota actual: $notaFundamentos"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "Programación Orientada a Objetos (25%)"
        )


        Text(
            text = "Nota actual: $notaPOO"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "Programación en Móviles (30%)"
        )


        Text(
            text = "Nota actual: $notaMoviles"
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "Base de Datos (25%)"
        )


        Text(
            text = "Nota actual: $notaBaseDatos"
        )

    }

}