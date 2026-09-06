package com.escobal.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


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



    var redondear by remember {
        mutableStateOf(false)
    }


    var confirmado by remember {
        mutableStateOf(false)
    }


    var promedioPonderado by remember {
        mutableStateOf(0.0)
    }


    var promedioFinal by remember {
        mutableStateOf(0)
    }


    var observacion by remember {
        mutableStateOf("")
    }


    var mensaje by remember {
        mutableStateOf("Asigna las notas y confirma para calcular")
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


        Slider(
            value = notaFundamentos.toFloat(),
            onValueChange = {
                notaFundamentos = it.toInt()
            },
            valueRange = 0f..20f,
            steps = 19
        )


        Text(
            text = "Nota elegida: $notaFundamentos"
        )



        Spacer(
            modifier = Modifier.height(16.dp)
        )



        Text(
            text = "Programación Orientada a Objetos (25%)"
        )


        Slider(
            value = notaPOO.toFloat(),
            onValueChange = {
                notaPOO = it.toInt()
            },
            valueRange = 0f..20f,
            steps = 19
        )


        Text(
            text = "Nota elegida: $notaPOO"
        )



        Spacer(
            modifier = Modifier.height(16.dp)
        )



        Text(
            text = "Programación en Móviles (30%)"
        )


        Slider(
            value = notaMoviles.toFloat(),
            onValueChange = {
                notaMoviles = it.toInt()
            },
            valueRange = 0f..20f,
            steps = 19
        )


        Text(
            text = "Nota elegida: $notaMoviles"
        )



        Spacer(
            modifier = Modifier.height(16.dp)
        )



        Text(
            text = "Base de Datos (25%)"
        )


        Slider(
            value = notaBaseDatos.toFloat(),
            onValueChange = {
                notaBaseDatos = it.toInt()
            },
            valueRange = 0f..20f,
            steps = 19
        )


        Text(
            text = "Nota elegida: $notaBaseDatos"
        )



        Spacer(
            modifier = Modifier.height(20.dp)
        )



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Text(
                text = "Redondear promedio final"
            )


            Switch(
                checked = redondear,

                onCheckedChange = {
                    redondear = it
                }
            )

        }



        Spacer(
            modifier = Modifier.height(12.dp)
        )



        Row {


            Checkbox(
                checked = confirmado,

                onCheckedChange = {
                    confirmado = it
                }
            )


            Text(
                text = "Confirmo que las notas son correctas"
            )

        }



        Spacer(
            modifier = Modifier.height(20.dp)
        )



        Button(

            enabled = confirmado,

            onClick = {


                promedioPonderado =
                    notaFundamentos * 0.20 +
                            notaPOO * 0.25 +
                            notaMoviles * 0.30 +
                            notaBaseDatos * 0.25



                promedioFinal =
                    if (redondear) {
                        promedioPonderado.roundToInt()
                    } else {
                        promedioPonderado.toInt()
                    }



                observacion =
                    when (promedioFinal) {

                        in 17..20 -> "EXCELENTE"

                        in 13..16 -> "APROBADO"

                        in 10..12 -> "EN RECUPERACIÓN"

                        else -> "DESAPROBADO"

                    }



                mensaje =
                    "✓ Promedio calculado correctamente"

            },

            modifier = Modifier.fillMaxWidth()

        ) {


            Text(
                text = "CALCULAR PROMEDIO"
            )

        }



        Spacer(
            modifier = Modifier.height(20.dp)
        )



        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {


            Column(
                modifier = Modifier.padding(16.dp)
            ) {


                Text(
                    text = "Resultado",
                    style = MaterialTheme.typography.titleMedium
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                Text(
                    text = mensaje
                )


                Text(
                    text = "Promedio ponderado: %.2f".format(promedioPonderado)
                )


                Text(
                    text = "Promedio final: $promedioFinal"
                )


                Text(
                    text = "Observación: $observacion"
                )


            }

        }


    }

}