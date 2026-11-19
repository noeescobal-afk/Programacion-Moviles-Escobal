package com.tecsup.campusgps.ui

// ARCHIVO: ListaLugares.kt
// Lista declarativa de sitios ordenados; comunica la selección al ViewModel.
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia
@Composable fun ListaLugares(lugares: List<LugarConDistancia>, seleccionado: Int?, onClick: (Int)->Unit, modifier: Modifier=Modifier) {
    LazyColumn(modifier=modifier,contentPadding=PaddingValues(horizontal=12.dp,vertical=4.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
        items(lugares,key={it.lugar.id}) { item ->
            val activo = item.lugar.id==seleccionado
            Card(modifier=Modifier.fillMaxWidth().clickable { onClick(item.lugar.id) },
                colors=CardDefaults.cardColors(containerColor=if(activo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                border=if(activo) BorderStroke(1.dp,MaterialTheme.colorScheme.primary) else null,
                shape=RoundedCornerShape(10.dp)) {
                Row(Modifier.fillMaxWidth().padding(10.dp),verticalAlignment=Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(item.lugar.nombre,style=MaterialTheme.typography.labelLarge)
                        Text(item.lugar.categoria,style=MaterialTheme.typography.bodySmall)
                    }
                    Text(item.metros?.let(::formatearDistancia) ?: "Sin calcular",style=MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
