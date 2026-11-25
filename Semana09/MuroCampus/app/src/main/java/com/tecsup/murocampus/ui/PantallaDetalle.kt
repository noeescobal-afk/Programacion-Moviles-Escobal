package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PantallaDetalle(
    viewModel: PostsViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.detalle.collectAsStateWithLifecycle()
    val eliminarState by viewModel.eliminarState.collectAsStateWithLifecycle()

    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    LaunchedEffect(eliminarState) {
        if (eliminarState is UiState.Exito) {
            viewModel.limpiarEliminarState()
            onVolver()
        }
    }

    when (val state = estado) {
        is UiState.Cargando -> {
            Cargando(modifier = modifier)
        }
        is UiState.Error -> {
            ErrorConReintento(
                mensaje = state.mensaje,
                onReintentar = { viewModel.reintentarDetalle() },
                modifier = modifier
            )
        }
        is UiState.Exito -> {
            val detalleUi = state.datos
            val post = detalleUi.post
            val comentarios = detalleUi.comentarios

            val isEliminando = eliminarState is UiState.Cargando

            if (mostrarDialogoConfirmacion) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoConfirmacion = false },
                    title = { Text("Eliminar publicación") },
                    text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                mostrarDialogoConfirmacion = false
                                viewModel.eliminar(post.id)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogoConfirmacion = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Usuario ${post.userId}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = post.body,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { mostrarDialogoConfirmacion = true },
                        enabled = !isEliminando,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isEliminando) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onError
                            )
                        } else {
                            Text("Eliminar")
                        }
                    }

                    if (eliminarState is UiState.Error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (eliminarState as UiState.Error).mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Comentarios (${comentarios.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(comentarios) { comentario ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = comentario.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1565C0), // Correo en azul
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = comentario.body,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
