package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PantallaCrear(
    viewModel: PostsViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }

    val publicarEstado by viewModel.publicar.collectAsStateWithLifecycle()

    val isCargando = publicarEstado is UiState.Cargando
    val camposValidos = titulo.isNotBlank() && contenido.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isCargando
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            enabled = !isCargando
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.publicar(titulo, contenido)
            },
            enabled = camposValidos && !isCargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isCargando) {
                CircularProgressIndicator(modifier = Modifier.height(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Publicar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = publicarEstado) {
            is UiState.Exito -> {
                Text(
                    text = "Publicado con id ${state.datos.id}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
                LaunchedEffect(state) {
                    titulo = ""
                    contenido = ""
                }
            }
            is UiState.Error -> {
                Text(
                    text = state.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> {}
        }
    }
}
