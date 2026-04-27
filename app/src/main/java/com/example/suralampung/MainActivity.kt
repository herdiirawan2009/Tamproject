package com.example.suralampung

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.suralampung.navigation.AppNavigation
import com.example.suralampung.ui.theme.SuraLampungTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SuraLampungTheme {

                val navController = rememberNavController()

                AppNavigation(navController)

            }
        }
    }
}