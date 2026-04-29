package com.example.suralampung.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- MODEL DATA ---
data class Penjual(val id: Int, val nama: String, val pesanTerakhir: String)
data class ChatMessage(val text: String, val isFromMe: Boolean)

@Composable
fun ChatPenjualScreen(onBack: () -> Unit = {}) {
    // State untuk melacak penjual mana yang sedang diajak chat
    // Jika null, berarti sedang berada di halaman daftar penjual
    var selectedPenjual by remember { mutableStateOf<Penjual?>(null) }

    // Tema Warna
    val primaryColor = Color(0xFF8B1C31)
    val backgroundColor = Color(0xFFF8F9FA)
    val surfaceColor = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF212121)

    // Daftar Dummy Penjual
    val daftarPenjual = remember {
        listOf(
            Penjual(1, "Bapak Budi (Pertanian)", "Stok jagung aman mas."),
            Penjual(2, "Ibu Siti (Peternakan)", "Sapi potong bisa dikirim besok ya."),
            Penjual(3, "Toko Tani Makmur", "Terima kasih sudah memesan pupuk di toko kami.")
        )
    }

    // --- LOGIKA PERPINDAHAN HALAMAN ---
    if (selectedPenjual == null) {
        // 1. TAMPILAN DAFTAR PENJUAL
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 16.dp)
        ) {
            // Memberi jarak kosong di atas agar sejajar
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBack, // Kembali ke layar sebelumnya (Home)
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = primaryColor),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("← Kembali", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Pesan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(daftarPenjual) { penjual ->
                    ItemPenjual(
                        penjual = penjual,
                        surfaceColor = surfaceColor,
                        textPrimary = textPrimary,
                        onClick = { selectedPenjual = penjual } // Masuk ke chat saat di-klik
                    )
                }
            }
        }
    } else {
        // 2. TAMPILAN RUANG CHAT DENGAN PENJUAL TERPILIH
        RuangChatView(
            penjual = selectedPenjual!!,
            primaryColor = primaryColor,
            backgroundColor = backgroundColor,
            surfaceColor = surfaceColor,
            textPrimary = textPrimary,
            onBack = { selectedPenjual = null } // Mengosongkan state untuk kembali ke daftar penjual
        )
    }
}

// --- KOMPONEN DAFTAR PENJUAL ---
@Composable
fun ItemPenjual(penjual: Penjual, surfaceColor: Color, textPrimary: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = "Profil", tint = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = penjual.nama,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = penjual.pesanTerakhir,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// --- KOMPONEN RUANG CHAT ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuangChatView(
    penjual: Penjual,
    primaryColor: Color,
    backgroundColor: Color,
    surfaceColor: Color,
    textPrimary: Color,
    onBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    val messages = remember {
        listOf(
            ChatMessage("Halo, apakah sumber daya ini masih tersedia?", true),
            ChatMessage("Halo kak! Iya, stoknya masih banyak.", false),
            ChatMessage(penjual.pesanTerakhir, false) // Pesan terakhir dinamis menyesuaikan penjual
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        // Memberi jarak kosong di atas agar sejajar
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = primaryColor),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("← Kembali", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profil", tint = surfaceColor)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = penjual.nama, // Nama otomatis menyesuaikan
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Online",
                    fontSize = 12.sp,
                    color = primaryColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message, primaryColor = primaryColor, surfaceColor = surfaceColor, textPrimary = textPrimary)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Ketik pesan...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { /* Logika Kirim Pesan */ },
                modifier = Modifier
                    .size(48.dp)
                    .background(primaryColor, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Kirim Pesan",
                    tint = Color.White
                )
            }
        }
    }
}

// --- KOMPONEN BUBBLE PESAN ---
@Composable
fun ChatBubble(message: ChatMessage, primaryColor: Color, surfaceColor: Color, textPrimary: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (message.isFromMe) primaryColor else surfaceColor,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isFromMe) 16.dp else 4.dp,
                        bottomEnd = if (message.isFromMe) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
                .fillMaxWidth(0.75f)
        ) {
            Text(
                text = message.text,
                color = if (message.isFromMe) Color.White else textPrimary,
                fontSize = 14.sp
            )
        }
    }
}