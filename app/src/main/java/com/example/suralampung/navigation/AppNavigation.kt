package com.example.suralampung.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.suralampung.ui.screens.DetailBarangScreen
import com.example.suralampung.ui.screens.HasilPencarianScreen
import com.example.suralampung.ui.screens.HomeScreen
import com.example.suralampung.ui.screens.KeranjangScreen
import com.example.suralampung.ui.screens.LoginScreen
import com.example.suralampung.ui.screens.ProfilScreen
import com.example.suralampung.ui.screens.RegisterScreen
import com.example.suralampung.ui.screens.RiwayatScreen
import com.example.suralampung.ui.screens.SplashScreen
import com.example.suralampung.ui.screens.ChatPenjualScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("home") {
            HomeScreen(
                onCartClick = { navController.navigate("keranjang") },
                onHistoryClick = { navController.navigate("riwayat") },
                onProfileClick = { navController.navigate("profil") },
                onChatClick = { navController.navigate("chat") },
                onSeeAllClick = { navController.navigate("search") },
                onDetailClick = { nama -> navController.navigate("detail/$nama") }
            )
        }

        composable("search") {
            HasilPencarianScreen(
                onItemClick = { nama ->
                    navController.navigate("detail/$nama")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "detail/{nama}",
            arguments = listOf(navArgument("nama") { type = NavType.StringType })
        ) { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            DetailBarangScreen(
                barangNama = nama,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("keranjang") {
            KeranjangScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("riwayat") {
            RiwayatScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("profil") {
            ProfilScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("chat") {
            ChatPenjualScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
