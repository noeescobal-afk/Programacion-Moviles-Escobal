package com.escobal.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio() {
    var seleccionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos") + categoriasTienda
    val visibles = if (seleccionada == "Todos") productosTienda
    else productosTienda.filter { it.categoria == seleccionada }

    Column {
        LazyRow(
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { cat ->
                FilterChip(
                    selected = cat == seleccionada,
                    onClick = { seleccionada = cat },
                    label = { Text(cat) }
                )
            }
        }
        LazyColumn {
            visibles.groupBy { it.categoria }.forEach { (cat, lista) ->
                item {
                    Text(
                        cat,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(lista, key = { it.id }) { TarjetaProducto(it) }
            }
        }
    }
}