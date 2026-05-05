package com.example.suralampung.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun HasilPencarianScreen(onItemClick: (String) -> Unit, onBack: () -> Unit) {

    val daftarBarang = listOf(
        Barang("Kopi Robusta Lampung","Perkebunan","Rp 55.000","Bandar Lampung","UMKM Tani Lampung","Tersedia","4.9","Kopi robusta khas Lampung"),
        Barang("Lada Hitam Lampung","Rempah","Rp 38.000","Lampung Timur","Komunitas Petani","Tersedia","4.8","Lada hitam berkualitas"),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF8B1C31)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("← Kembali", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Hasil Pencarian",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(daftarBarang) { barang ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1E6E8).copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = barang.nama, fontWeight = FontWeight.Bold)
                        Text(text = barang.harga)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onItemClick(barang.nama) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31))
                        ) {
                            Text("Lihat Detail", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}