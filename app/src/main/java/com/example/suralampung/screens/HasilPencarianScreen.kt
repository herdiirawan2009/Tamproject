package com.example.suralampung.screens

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.suralampung.data.Barang
import com.example.suralampung.data.RetrofitClient

@Composable
fun HasilPencarianScreen(onItemClick: (String) -> Unit, onBack: () -> Unit) {

    // State untuk menampung data dari API
    var listBarang by remember { mutableStateOf<List<Barang>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // State untuk fitur pencarian (Search)
    var searchQuery by remember { mutableStateOf("") }

    // Mengambil data dari API saat halaman dibuka
    LaunchedEffect(Unit) {
        try {
            listBarang = RetrofitClient.instance.getBarang()
            isLoading = false
            isError = false
        } catch (_: Exception) {
            isLoading = false
            isError = true
        }
    }

    // Logika untuk menyaring (filter) barang berdasarkan ketikan user
    val filteredList = listBarang.filter {
        it.nama.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // HEADER PENCARIAN & TOMBOL KEMBALI
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp),
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
                text = "Pencarian",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF212121)
            )
        }

        // KOLOM PENCARIAN (SEARCH BAR)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color(0xFF8B1C31)
                )
                Spacer(modifier = Modifier.width(12.dp))

                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color(0xFF8B1C31)),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text("Ketik nama sumber daya...", color = Color.Gray, fontSize = 15.sp)
                        }
                        innerTextField()
                    }
                )
            }
        }

        Text(
            text = "Menampilkan ${filteredList.size} hasil",
            fontSize = 13.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        // TAMPILAN LIST ATAU LOADING
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF8B1C31))
            }
        } else if (isError) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Gagal memuat data", color = Color.Red, fontWeight = FontWeight.Medium)
            }
        } else if (filteredList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Barang tidak ditemukan", color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        } else {
            // LIST HASIL PENCARIAN
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredList) { barang ->
                    Card(
                        onClick = { onItemClick(barang.nama) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // GAMBAR DARI API MENGGUNAKAN ASYNCIMAGE
                            AsyncImage(
                                model = barang.imageUrl,
                                contentDescription = barang.nama,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.LightGray)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // INFORMASI PRODUK
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = barang.nama,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF212121),
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Rp ${barang.harga}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF8B1C31)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Lokasi
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.LocationOn,
                                            contentDescription = null,
                                            tint = Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = barang.lokasi,
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }

                                    // Rating Dummy (Karena di HomeScreen rating tidak ada di constructor Barang)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFB300),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "4.9",
                                            color = Color(0xFF212121),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}