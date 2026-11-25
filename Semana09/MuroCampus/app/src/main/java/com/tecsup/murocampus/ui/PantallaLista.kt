package com.tecsup.murocampus.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PantallaLista(
    viewModel: PostsViewModel,
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.lista.collectAsStateWithLifecycle()

    when (val state = estado) {
        is UiState.Cargando -> {
            Cargando(modifier = modifier)
        }
        is UiState.Error -> {
            ErrorConReintento(
                mensaje = state.mensaje,
                onReintentar = { viewModel.cargar() },
                modifier = modifier
            )
        }
        is UiState.Exito -> {
            LazyColumn(modifier = modifier) {
                items(state.datos) { post ->
                    TarjetaPost(post = post, onClick = { onPostClick(post.id) })
                }
            }
        }
    }
}
