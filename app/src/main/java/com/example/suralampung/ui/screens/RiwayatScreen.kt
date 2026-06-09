package com.example.suralampung.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ItemRiwayat(item: Map<String, Any>, onDetailClick: () -> Unit) {
    val status = item["status"]?.toString() ?: "Diproses"
    val isSelesai = status == "Selesai"
    val badgeColor = if (isSelesai) Color(0xFF4CAF50) else Color(0xFFFFB300)
    val badgeBg = badgeColor.copy(alpha = 0.15f)
    val statusIcon = if (isSelesai) Icons.Rounded.CheckCircle else Icons.Rounded.AccessTime

    val nama = item["nama"]?.toString() ?: ""
    val hargaStr = item["harga"]?.toString() ?: "0"
    val gambar = item["gambar"]?.toString() ?: ""
    val timestamp = item["tanggal"] as? Long ?: 0L

    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
    val tanggalStr = if (timestamp > 0) sdf.format(Date(timestamp)) else "-"

    val formatRupiah = NumberFormat.getInstance(Locale("id", "ID"))
    val hargaLong = hargaStr.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0L
    val hargaFormatted = formatRupiah.format(hargaLong)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = gambar,
                        contentDescription = nama,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = nama,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF212121),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tanggalStr,
                            color = Color.Gray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFF0F0F0))
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Belanja",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Rp $hargaFormatted",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF8B1C31)
                    )
                }

                Row(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = status,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onDetailClick,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Lihat Detail", color = Color(0xFF8B1C31), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun RiwayatScreen(onBack: () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sedang Diproses", "Selesai")

    var listDitampilkan by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    var selectedItemForDetail by remember { mutableStateOf<Map<String, Any>?>(null) }
    var selectedItemForConfirmation by remember { mutableStateOf<Map<String, Any>?>(null) }

    DisposableEffect(tabIndex) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val status = if (tabIndex == 0) "Diproses" else "Selesai"
        val registration = if (uid != null) {
            isLoading = true
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("riwayat")
                .whereEqualTo("status", status)
                .orderBy("tanggal", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        isError = true
                        isLoading = false
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        listDitampilkan = snapshot.documents.map { doc ->
                            val data = doc.data?.toMutableMap() ?: mutableMapOf()
                            data["docId"] = doc.id
                            data
                        }
                        isLoading = false
                        isError = false
                    }
                }
        } else {
            isLoading = false
            isError = true
            null
        }
        onDispose {
            registration?.remove()
        }
    }

    val formatRupiah = NumberFormat.getInstance(Locale("id", "ID"))
    val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))

    if (selectedItemForDetail != null) {
        val item = selectedItemForDetail!!
        val isSelesai = item["status"]?.toString() == "Selesai"
        val statusColor = if (isSelesai) Color(0xFF4CAF50) else Color(0xFFFFB300)

        val hargaStr = item["harga"]?.toString() ?: "0"
        val hargaLong = hargaStr.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0L
        val hargaFormatted = formatRupiah.format(hargaLong)

        val timestamp = item["tanggal"] as? Long ?: 0L
        val tanggalStr = if (timestamp > 0) sdf.format(Date(timestamp)) else "-"

        AlertDialog(
            onDismissRequest = { selectedItemForDetail = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF1E6E8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = null,
                            tint = Color(0xFF8B1C31)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Detail Pesanan",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color(0xFF212121)
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Nama Produk", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = item["nama"]?.toString() ?: "", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Tanggal Pemesanan", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = tanggalStr, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212121))

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFE0E0E0))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Total Harga", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Rp $hargaFormatted", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B1C31))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Status Pesanan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        Box(
                            modifier = Modifier
                                .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item["status"]?.toString() ?: "Diproses",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (!isSelesai) {
                    Button(
                        onClick = {
                            selectedItemForConfirmation = item
                            selectedItemForDetail = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tandai Pesanan Selesai", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                    }
                } else {
                    Button(
                        onClick = { selectedItemForDetail = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tutup", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            },
            dismissButton = {
                if (!isSelesai) {
                    TextButton(
                        onClick = { selectedItemForDetail = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tutup Kembali", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                }
            }
        )
    }

    if (selectedItemForConfirmation != null) {
        val item = selectedItemForConfirmation!!
        AlertDialog(
            onDismissRequest = { selectedItemForConfirmation = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFFFF8E1), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Konfirmasi Selesai",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color(0xFF212121)
                    )
                }
            },
            text = {
                Text(
                    text = "Apakah Anda yakin pesanan ini sudah selesai dan diterima dengan baik? Tindakan ini tidak dapat dibatalkan.",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val docId = item["docId"]?.toString()
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid != null && docId != null) {
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(uid)
                                .collection("riwayat")
                                .document(docId)
                                .update("status", "Selesai")
                        }
                        selectedItemForConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ya, Yakin", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { selectedItemForConfirmation = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Batal", color = Color.Gray, fontWeight = FontWeight.Medium)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                text = "Riwayat Transaksi",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF212121)
            )
        }

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color.White,
            contentColor = Color(0xFF8B1C31)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (tabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (tabIndex == index) Color(0xFF8B1C31) else Color.Gray
                        )
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF8B1C31))
                }
            } else if (isError) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Koneksi gagal, silakan coba lagi", color = Color.Red)
                }
            } else if (listDitampilkan.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (tabIndex == 0) "Belum ada pesanan yang diproses" else "Belum ada pesanan selesai",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
                ) {
                    items(listDitampilkan) { item ->
                        ItemRiwayat(
                            item = item,
                            onDetailClick = {
                                selectedItemForDetail = item
                            }
                        )
                    }
                }
            }
        }
    }
}
