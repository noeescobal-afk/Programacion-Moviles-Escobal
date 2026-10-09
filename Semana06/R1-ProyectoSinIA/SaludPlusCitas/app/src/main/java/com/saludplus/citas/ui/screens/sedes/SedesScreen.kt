package com.saludplus.citas.ui.screens.sedes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.MensajeListaVacia
import com.saludplus.citas.ui.components.SaludPlusBottomBar
import com.saludplus.citas.ui.components.SedeCard
import com.saludplus.citas.ui.theme.SaludPlusCitasTheme

private val AzulHeroDeep = Color(0xFF0F2B5B)
private val AzulElectric = Color(0xFF246BFD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SedesScreen(
    onSedeSeleccionada: (Int) -> Unit,
    onInicio: () -> Unit,
    onDoctores: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier,
    onVolverAtras: (() -> Unit)? = null,
    onAbrirMenu: (() -> Unit)? = null
) {
    var textoBusqueda by remember { mutableStateOf("") }
    val sedes = Repositorio.buscarSedes(textoBusqueda)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sedes SaludPlus",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    if (onVolverAtras != null) {
                        IconButton(onClick = onVolverAtras) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver atrás",
                                tint = Color.White
                            )
                        }
                    } else if (onAbrirMenu != null) {
                        IconButton(onClick = onAbrirMenu) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulHeroDeep
                )
            )
        },
        bottomBar = {
            if (onVolverAtras == null) {
                SaludPlusBottomBar(
                    rutaActual = "sedes",
                    onNavegarInicio = onInicio,
                    onNavegarSedes = {},
                    onNavegarDoctores = onDoctores,
                    onNavegarResultados = onResultados,
                    onNavegarPerfil = onPerfil
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // HERO HEADER COMPACTO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzulHeroDeep)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Encuentra atención cerca de ti",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Infraestructura médica de primer nivel",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.HealthAndSafety,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // BUSCADOR SUPERPUESTO / CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    tonalElevation = 3.dp
                ) {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        placeholder = { Text("Buscar sede por nombre o dirección...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Buscar",
                                tint = AzulElectric
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectric,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "${sedes.size} sedes disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (sedes.isEmpty()) {
                    MensajeListaVacia(mensaje = "No se encontraron sedes coincidentes")
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(sedes, key = { it.id }) { sede ->
                            SedeCard(
                                sede = sede,
                                onVerMedicos = { onSedeSeleccionada(sede.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SedesScreenPreview() {
    SaludPlusCitasTheme {
        SedesScreen(
            onSedeSeleccionada = {},
            onInicio = {},
            onDoctores = {},
            onResultados = {},
            onPerfil = {}
        )
    }
}
