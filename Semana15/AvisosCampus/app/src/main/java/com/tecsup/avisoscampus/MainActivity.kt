package com.tecsup.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.avisoscampus.data.AvisosRepositoryMemoria
import com.tecsup.avisoscampus.ui.PantallaAvisos
import com.tecsup.avisoscampus.ui.theme.AvisosCampusTheme
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel
import com.tecsup.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = AvisosViewModelFactory(AvisosRepositoryMemoria())
        setContent {
            val vm: AvisosViewModel = viewModel(factory = factory)
            val modoTema by vm.modoTema.collectAsState()
            AvisosCampusTheme(modoTema = modoTema) {
                PantallaAvisos(vm)
            }
        }
    }
}
