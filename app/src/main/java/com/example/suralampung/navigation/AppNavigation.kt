package com.example.suralampung.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.suralampung.ui.screens.DetailBarangScreen
import com.example.suralampung.ui.screens.DetailPesananScreen
import com.example.suralampung.ui.screens.HasilPencarianScreen
import com.example.suralampung.ui.screens.HomeScreen
import com.example.suralampung.ui.screens.KeranjangScreen
import com.example.suralampung.ui.screens.LoginScreen
import com.example.suralampung.ui.screens.ProfilScreen
import com.example.suralampung.ui.screens.RegisterScreen
import com.example.suralampung.ui.screens.RiwayatScreen
import com.example.suralampung.ui.screens.SplashScreen
import com.example.suralampung.ui.screens.ChatPenjualScreen
import com.example.suralampung.ui.screens.TambahSumberDayaScreen
import com.example.suralampung.ui.screens.PengaturanScreen
import com.example.suralampung.ui.screens.PusatBantuanScreen
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
                onDetailClick = { nama -> navController.navigate("detail/$nama") },
                onAddClick = { navController.navigate("tambah_sumber_daya") },
                onKategoriClick = { kategori -> navController.navigate("hasil_pencarian/$kategori") }
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
            route = "hasil_pencarian/{kategori}",
            arguments = listOf(navArgument("kategori") { type = NavType.StringType })
        ) { backStackEntry ->
            val kategori = backStackEntry.arguments?.getString("kategori")
            HasilPencarianScreen(
                kategori = kategori,
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
                },
                onChatClick = { namaBarang ->
                    navController.navigate("chat?barang=$namaBarang")
                }
            )
        }

        composable("keranjang") {
            KeranjangScreen(
                onBack = {
                    navController.popBackStack()
                },
                onCheckoutClick = {
                    navController.navigate("detail_pesanan")
                }
            )
        }

        composable("detail_pesanan") {
            DetailPesananScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.navigate("riwayat") {
                        popUpTo("home") { inclusive = false }
                    }
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
                },
                onPengaturanClick = {
                    navController.navigate("pengaturan")
                },
                onBantuanClick = {
                    navController.navigate("pusat_bantuan")
                }
            )
        }

        composable(
            route = "chat?barang={barang}",
            arguments = listOf(navArgument("barang") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val barang = backStackEntry.arguments?.getString("barang")
            val penjualId = if (barang != null) 1 else null

            ChatPenjualScreen(
                onBack = {
                    navController.popBackStack()
                },
                initialPenjualId = penjualId,
                initialBarangNama = barang
            )
        }

        composable("tambah_sumber_daya") {
            TambahSumberDayaScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("pengaturan") {
            PengaturanScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("pusat_bantuan") {
            PusatBantuanScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}