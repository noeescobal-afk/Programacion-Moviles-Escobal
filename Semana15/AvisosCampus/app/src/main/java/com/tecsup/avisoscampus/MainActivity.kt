package com.tecsup.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MaterialTheme
import com.tecsup.avisoscampus.data.AvisosRepositoryMemoria
import com.tecsup.avisoscampus.viewmodel.*
import com.tecsup.avisoscampus.ui.PantallaAvisos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = AvisosViewModelFactory(AvisosRepositoryMemoria())

        setContent {
            MaterialTheme {
                val vm: AvisosViewModel = viewModel(factory = factory)
                PantallaAvisos(vm)
            }
        }
    }
}
