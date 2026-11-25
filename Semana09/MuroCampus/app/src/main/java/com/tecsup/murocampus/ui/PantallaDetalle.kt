package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PantallaDetalle(
    viewModel: PostsViewModel,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.detalle.collectAsStateWithLifecycle()

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
