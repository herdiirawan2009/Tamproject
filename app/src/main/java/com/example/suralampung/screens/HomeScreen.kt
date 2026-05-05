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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.suralampung.data.Barang
import com.example.suralampung.data.RetrofitClient

data class Kategori(val nama: String)

@Composable
fun HomeScreen(
    onCartClick: () -> Unit,
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onSplashClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onSeeAllClick: () -> Unit,
    onDetailClick: (String) -> Unit = {}
) {
    val daftarKategori = listOf(
        Kategori("Pertanian"),
        Kategori("Perikanan"),
        Kategori("Peternakan"),
        Kategori("Kerajinan")
    )

    var listBarang by remember { mutableStateOf<List<Barang>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        item {
            HeaderHome(onProfileClick = onProfileClick)

            QuickMenu(
                onHistoryClick = onHistoryClick,
                onCartClick = onCartClick,
                onChatClick = onChatClick,
                onSplashClick = onSplashClick,
                onAddClick = onAddClick,
                onSeeAllClick = onSeeAllClick
            )

            SearchBarDummy()
            BannerPromo(onSeeAllClick = onSeeAllClick)
            SectionTitle("Kategori")
            KategoriRow(daftarKategori)
            SectionTitle("Rekomendasi")
        }
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8B1C31))
                }
            }
        }
        else if (isError || listBarang.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Gagal memuat data. Pastikan internet menyala.",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        else {
            items(listBarang) { item ->
                CardProduk(item = item, onDetailClick = { onDetailClick(item.nama) })
            }
        }
    }
}

@Composable
fun QuickMenu(
    onHistoryClick: () -> Unit,
    onCartClick: () -> Unit,
    onChatClick: () -> Unit,
    onSplashClick: () -> Unit,
    onAddClick: () -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MenuIcon(Icons.Default.History, "Riwayat", onHistoryClick)
            MenuIcon(Icons.Default.ShoppingCart, "Keranjang", onCartClick)
            MenuIcon(Icons.Default.Email, "Chat", onChatClick)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MenuIcon(Icons.Default.Add, "Tambah", onAddClick)
            MenuIcon(Icons.Default.Search, "Cari", onSeeAllClick) // Tombol Cari/Detail Produk List
            MenuIcon(Icons.Default.Refresh, "Splash", onSplashClick)
        }
    }
}

@Composable
fun MenuIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = Color(0xFF8B1C31))
        }
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HeaderHome(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8B1C31))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Halo, Warga Lampung!",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Lampung, Indonesia",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        IconButton(onClick = { onProfileClick() }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profil",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchBarDummy() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Cari sumber daya...", color = Color.Gray)
    }
}

@Composable
fun BannerPromo(onSeeAllClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB300))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Transparan & Mudah",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B1C31)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Temukan sumber daya rakyat Lampung dengan cepat.",
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onSeeAllClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Lihat Semua", color = Color.White)
            }
        }
    }
}

@Composable
fun SectionTitle(judul: String) {
    Text(
        text = judul,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun KategoriRow(list: List<Kategori>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(list) { kategori ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = kategori.nama,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = Color(0xFF8B1C31),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CardProduk(item: Barang, onDetailClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onDetailClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.nama,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nama,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rp ${item.harga}",
                    color = Color(0xFF8B1C31),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.lokasi, color = Color.Gray, fontSize = 13.sp)
                }
            }

            // Tombol Detail di setiap kartu
            Button(
                onClick = onDetailClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B1C31)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Detail", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
