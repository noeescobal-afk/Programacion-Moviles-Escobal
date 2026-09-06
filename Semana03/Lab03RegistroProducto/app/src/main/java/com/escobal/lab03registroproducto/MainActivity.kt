package com.escobal.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun RegistroProductoScreen(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ){

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

    }

}