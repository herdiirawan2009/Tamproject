package com.example.suralampung.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder

@Composable
fun PusatBantuanScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val adminPhone = "6283809802930"

    var showChatOptions by remember { mutableStateOf(false) }
    var namaUser by remember { mutableStateOf("User") }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        namaUser = document.getString("nama") ?: "User"
                    }
                }
        }
    }

    if (showChatOptions) {
        AlertDialog(
            onDismissRequest = { showChatOptions = false },
            title = {
                Text(text = "Pilih Topik Kendala", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column {
                    val issues = listOf(
                        "Akun tidak bisa upload produk",
                        "Lupa password atau gagal login",
                        "Fitur pencarian tidak berfungsi",
                        "Laporan bug atau error lainnya"
                    )

                    issues.forEach { issue ->
                        Text(
                            text = issue,
                            fontSize = 16.sp,
                            color = Color(0xFF212121),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showChatOptions = false
                                    try {
                                        val message = "Hai admin, aku $namaUser, kendalaku: $issue."
                                        val encodedMessage = URLEncoder.encode(message, "UTF-8")
                                        val url = "https://wa.me/$adminPhone?text=$encodedMessage"
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Gagal membuka WhatsApp", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(vertical = 12.dp)
                        )
                        HorizontalDivider(color = Color(0xFFF8F9FA), thickness = 2.dp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChatOptions = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFF212121)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Pusat Bantuan",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF212121)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hubungi Admin Langsung",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                ActionMenuItem(
                    icon = Icons.Default.Call,
                    title = "Telepon Admin",
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+$adminPhone"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Gagal membuka Panggilan", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                HorizontalDivider(color = Color(0xFFF8F9FA), thickness = 2.dp)

                ActionMenuItem(
                    icon = Icons.Default.Info,
                    title = "Chat WhatsApp",
                    iconColor = Color(0xFF25D366),
                    onClick = { showChatOptions = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Informasi & Solusi Cepat",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "1. Cara Menambah Sumber Daya", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF8B1C31))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Masuk ke halaman utama (Home), lalu tekan ikon tambah (+) yang tersedia untuk mulai mengunggah data sumber daya baru Anda.", fontSize = 14.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "2. Barang Tidak Ditemukan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF8B1C31))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Pastikan penulisan kata kunci di kolom pencarian sudah benar. Jika barang memang belum tersedia, layar akan menampilkan pesan barang kosong.", fontSize = 14.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "3. Mengubah Profil & Sandi", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF8B1C31))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Silakan buka menu 'Profil', lalu masuk ke menu 'Pengaturan' atau 'Edit Profil' untuk memperbarui data diri dan keamanan akun.", fontSize = 14.sp, color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(Color(0xFFFFF9E6), RoundedCornerShape(12.dp))
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ActionMenuItem(
    icon: ImageVector,
    title: String,
    iconColor: Color = Color(0xFF8B1C31),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF8F9FA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = Color(0xFF212121), fontWeight = FontWeight.Bold)
    }
}