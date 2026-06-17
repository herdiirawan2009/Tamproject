package com.example.suralampung.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailPesananScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    dataPesanan: List<Map<String, Any>>
) {
    val context = LocalContext.current
    val listPesanan = dataPesanan
    val isLoading = false
    var isProcessing by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val totalHargaBarang = listPesanan.sumOf { item ->
        val hargaStr = item["harga"]?.toString() ?: "0"
        hargaStr.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0L
    }

    val ongkosKirim = 15000L
    val totalSemua = totalHargaBarang + ongkosKirim
    val formatRupiah = NumberFormat.getInstance(Locale("id", "ID"))

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = Color.White,
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Pesanan Berhasil!", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color(0xFF212121))
                }
            },
            text = {
                Text(
                    text = "Pesanan Anda sedang diproses oleh penjual. Anda dapat melihat status pesanan di halaman Riwayat.",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSuccess()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31))
                ) {
                    Text("Lihat Riwayat Pesanan")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        if (listPesanan.isEmpty() || isProcessing) return@Button
                        isProcessing = true
                        currentUser?.uid?.let { uid ->
                            val batch = db.batch()
                            val riwayatRef = db.collection("users").document(uid).collection("riwayat")
                            val keranjangRef = db.collection("users").document(uid).collection("keranjang")

                            val timestamp = System.currentTimeMillis()

                            listPesanan.forEach { item ->
                                val docId = item["id_barang"]?.toString() ?: item["nama"]?.toString() ?: ""
                                if (docId.isNotEmpty()) {
                                    val newHistoryDoc = riwayatRef.document()
                                    val historyData = item.toMutableMap()
                                    historyData["status"] = "Diproses"
                                    historyData["tanggal"] = timestamp

                                    batch.set(newHistoryDoc, historyData)
                                    batch.delete(keranjangRef.document(docId))
                                }
                            }

                            batch.commit()
                                .addOnSuccessListener {
                                    isProcessing = false
                                    showSuccessDialog = true
                                }
                                .addOnFailureListener {
                                    isProcessing = false
                                    Toast.makeText(context, "Gagal memproses pesanan", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading && !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Konfirmasi Pesanan", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(bottom = paddingValues.calculateBottomPadding())
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
                        contentDescription = "Kembali",
                        tint = Color(0xFF212121)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Detail Pesanan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF212121)
                )
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF8B1C31))
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Alamat Pengiriman", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Budi Santoso | 081234567890", fontWeight = FontWeight.Medium, color = Color(0xFF212121))
                        Text("Jl. Soekarno Hatta No. 10, Rajabasa, Bandar Lampung, Lampung", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Ringkasan Belanja", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        Spacer(modifier = Modifier.height(12.dp))

                        listPesanan.forEach { item ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(item["nama"]?.toString() ?: "", color = Color.Gray, modifier = Modifier.weight(1f), maxLines = 1)
                                Text("Rp ${formatRupiah.format(item["harga"]?.toString()?.replace(Regex("[^0-9]"), "")?.toLongOrNull() ?: 0L)}", fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Rincian Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal Produk", color = Color.Gray)
                            Text("Rp ${formatRupiah.format(totalHargaBarang)}", fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal Pengiriman", color = Color.Gray)
                            Text("Rp ${formatRupiah.format(ongkosKirim)}", fontWeight = FontWeight.Medium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Total Pembayaran", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Rp ${formatRupiah.format(totalSemua)}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF8B1C31))
                        }
                    }
                }
            }
        }
    }
}