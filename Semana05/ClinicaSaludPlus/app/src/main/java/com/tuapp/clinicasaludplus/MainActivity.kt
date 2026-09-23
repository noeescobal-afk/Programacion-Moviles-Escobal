package com.tuapp.clinicasaludplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tuapp.clinicasaludplus.navigation.ClinicNavigation
import com.tuapp.clinicasaludplus.ui.theme.ClinicaSaludPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClinicaSaludPlusTheme {
                ClinicNavigation()
            }
        }
    }
}
