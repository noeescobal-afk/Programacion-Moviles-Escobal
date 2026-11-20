package com.tecsup.productos.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
<<<<<<< HEAD
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
=======
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
>>>>>>> origin/mejora-ia
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ErrorView
import java.util.Locale
=======
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.CabeceraProductos
import com.tecsup.productos.ui.components.Cargando
import com.tecsup.productos.ui.components.ColumnaProductos
import com.tecsup.productos.ui.components.ErrorView
import com.tecsup.productos.ui.components.FotoProducto
import java.text.NumberFormat
>>>>>>> origin/mejora-ia

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

<<<<<<< HEAD
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("Volver al catálogo")
        }
        Text("Detalle del producto", style = MaterialTheme.typography.titleMedium)

=======
    ColumnaProductos(modifier) {
        CabeceraProductos("Detalle", "PRODUCTOS") {
            TextButton(onClick = onVolver) { Text("Volver") }
        }
>>>>>>> origin/mejora-ia
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(actual.mensaje, viewModel::cargar)
            is UiState.Exito -> {
                val producto = actual.datos
<<<<<<< HEAD
                Text(producto.titulo, style = MaterialTheme.typography.headlineSmall)
                Text("Marca: ${producto.subtitulo}")
                producto.precio?.let { precio ->
                    val precioFormateado = String.format(Locale.getDefault(), "%.2f", precio)
                    Text("Precio: $precioFormateado")
                }
                producto.descripcion?.takeIf { it.isNotBlank() }?.let { descripcion ->
                    Text(descripcion, style = MaterialTheme.typography.bodyMedium)
=======
                FotoProducto(producto, Modifier.fillMaxWidth().height(260.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            producto.subtitulo, color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            producto.titulo, style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        producto.precio?.let { precio ->
                            val formato = NumberFormat.getNumberInstance().apply {
                                minimumFractionDigits = 2
                                maximumFractionDigits = 2
                            }
                            Text(
                                "Precio · ${formato.format(precio)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        producto.descripcion?.takeIf { it.isNotBlank() }?.let {
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
>>>>>>> origin/mejora-ia
                }
            }
        }
    }
}
