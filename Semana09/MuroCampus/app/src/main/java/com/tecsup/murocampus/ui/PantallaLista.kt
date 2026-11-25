package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PantallaLista(
    viewModel: PostsViewModel,
    onPostClick: (Int) -> Unit,
    onCrearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.lista.collectAsStateWithLifecycle()
    val usuarioFiltro by viewModel.usuarioFiltro.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = usuarioFiltro == null,
                        onClick = { viewModel.filtrar(null) },
                        label = { Text("Todos") }
                    )
                }
                items(10) { index ->
                    val userId = index + 1
                    FilterChip(
                        selected = usuarioFiltro == userId,
                        onClick = { viewModel.filtrar(userId) },
                        label = { Text("Usuario $userId") }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val state = estado) {
                    is UiState.Cargando -> {
                        Cargando()
                    }
                    is UiState.Error -> {
                        ErrorConReintento(
                            mensaje = state.mensaje,
                            onReintentar = { viewModel.cargar() }
                        )
                    }
                    is UiState.Exito -> {
                        if (state.datos.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Sin publicaciones",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(state.datos) { post ->
                                    TarjetaPost(post = post, onClick = { onPostClick(post.id) })
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onCrearClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Crear publicación")
        }
    }
}
