package com.example.suralampung

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.example.suralampung.navigation.AppNavigation
import com.example.suralampung.ui.theme.SuraLampungTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings

class MainActivity : ComponentActivity() {
    companion object {
        private var backgroundTime: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        FirebaseFirestore.getInstance().firestoreSettings = settings
        enableEdgeToEdge()
        setContent {
            SuraLampungTheme {
                val navController = rememberNavController()
                DisposableEffect(Unit) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_STOP) {
                            backgroundTime = System.currentTimeMillis()
                        } else if (event == Lifecycle.Event.ON_START) {
                            val currentTime = System.currentTimeMillis()
                            if (backgroundTime != 0L && (currentTime - backgroundTime) > 300000) {
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            }
                            backgroundTime = 0
                        }
                    }
                    lifecycle.addObserver(observer)
                    onDispose {
                        lifecycle.removeObserver(observer)
                    }
                }
                AppNavigation(navController)
            }
        }
    }
}