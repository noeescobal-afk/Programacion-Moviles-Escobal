package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.PostsViewModel
import com.tecsup.murocampus.ui.PostsViewModelFactory
import com.tecsup.murocampus.ui.theme.MuroCampusTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MuroCampusTheme {
                val viewModel: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(
                        PostRepositoryRemote(RetrofitClient.api)
                    )
                )

                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }

                BackHandler(enabled = idSeleccionado != null) {
                    idSeleccionado = null
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1565C0),
                                titleContentColor = Color.White
                            )
                        )
                    }
                ) { innerPadding ->
                    val modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)

                    if (idSeleccionado == null) {
                        PantallaLista(
                            viewModel = viewModel,
                            onPostClick = { postId ->
                                idSeleccionado = postId
                                viewModel.abrir(postId)
                            },
                            modifier = modifier
                        )
                    } else {
                        PantallaDetalle(
                            viewModel = viewModel,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}
