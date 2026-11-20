package com.tecsup.productos.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
<<<<<<< HEAD
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
=======
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
>>>>>>> origin/mejora-ia
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
<<<<<<< HEAD
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView
=======
import com.tecsup.productos.ui.components.AvisoCatalogo
import com.tecsup.productos.ui.components.BloqueVacio
import com.tecsup.productos.ui.components.CabeceraProductos
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView
import com.tecsup.productos.ui.components.PaginaProductos
import com.tecsup.productos.ui.components.TarjetaProducto
>>>>>>> origin/mejora-ia

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

<<<<<<< HEAD
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("Volver al catálogo")
        }
        Text("Mis favoritos", style = MaterialTheme.typography.headlineSmall)
        aviso?.let { mensaje ->
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }

        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) {
                    Text("Aún no tienes favoritos. Marca un producto desde el catálogo.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(actual.datos, key = { it.id }) { producto ->
                            Card(
                                onClick = { onAbrirDetalle(producto.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(producto.titulo, style = MaterialTheme.typography.titleMedium)
                                    Text(producto.subtitulo, style = MaterialTheme.typography.bodyMedium)
                                    TextButton(onClick = { viewModel.quitarFavorito(producto.id) }) {
                                        Text("Quitar favorito")
                                    }
                                }
                            }
=======
    PaginaProductos(modifier) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 640.dp).fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item("cabecera") {
                CabeceraProductos("Mis favoritos", "PRODUCTOS") {
                    TextButton(onClick = onVolver) { Text("Volver") }
                }
            }
            aviso?.let { mensaje ->
                item("aviso") { AvisoCatalogo(mensaje) }
            }
            when (val actual = estado) {
                UiState.Cargando -> item("cargando") { Cargando() }
                is UiState.Error -> item("error") { ErrorView(actual.mensaje, viewModel::cargar) }
                is UiState.Exito -> {
                    if (actual.datos.isEmpty()) {
                        item("vacio") {
                            BloqueVacio("Aún no hay favoritos", "Toca el corazón de un producto para guardarlo.")
                        }
                    } else {
                        items(actual.datos, key = { it.id }) { producto ->
                            TarjetaProducto(
                                producto = producto,
                                onAbrir = { onAbrirDetalle(producto.id) },
                                onFavorito = { viewModel.quitarFavorito(producto.id) },
                                modifier = Modifier.animateItem()
                            )
>>>>>>> origin/mejora-ia
                        }
                    }
                }
            }
        }
    }
}
