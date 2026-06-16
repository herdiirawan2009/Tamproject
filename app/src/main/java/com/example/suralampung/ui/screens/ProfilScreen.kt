package com.example.suralampung.ui.screens

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfilScreen(
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {},
    onPengaturanClick: () -> Unit = {},
    onBantuanClick: () -> Unit = {}
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var editNama by remember { mutableStateOf("") }
    var isLoadingUpdate by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        nama = document.getString("nama") ?: ""
                        email = document.getString("email") ?: ""
                    }
                }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(text = "Edit Profil", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNama,
                        onValueChange = { editNama = it },
                        label = { Text("Nama Lengkap") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isLoadingUpdate = true
                        currentUser?.uid?.let { uid ->
                            firestore.collection("users").document(uid)
                                .update("nama", editNama)
                                .addOnSuccessListener {
                                    nama = editNama
                                    isLoadingUpdate = false
                                    showEditDialog = false
                                    Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    isLoadingUpdate = false
                                    Toast.makeText(context, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    enabled = !isLoadingUpdate && editNama.isNotBlank()
                ) {
                    Text(if (isLoadingUpdate) "Menyimpan..." else "Simpan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditDialog = false },
                    enabled = !isLoadingUpdate
                ) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = Color.White,
            title = {
                Text(text = "Keluar Akun", fontWeight = FontWeight.Bold, color = Color(0xFF212121))
            },
            text = {
                Text("Apakah kamu yakin ingin keluar dari akun ini?", color = Color.Gray)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }
                ) {
                    Text("Yakin", color = Color(0xFF8B1C31), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    text = "Profil Saya",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF212121)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1E6E8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Foto Profil",
                            modifier = Modifier.size(70.dp),
                            tint = Color(0xFF8B1C31).copy(alpha = 0.5f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = nama,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = email,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pengaturan Akun",
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
                    MenuItem(
                        icon = Icons.Default.AccountCircle,
                        title = "Edit Profil",
                        onClick = {
                            editNama = nama
                            showEditDialog = true
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF8F9FA), thickness = 2.dp)
                    MenuItem(
                        icon = Icons.Default.Settings,
                        title = "Pengaturan",
                        onClick = onPengaturanClick
                    )
                    HorizontalDivider(color = Color(0xFFF8F9FA), thickness = 2.dp)
                    MenuItem(
                        icon = Icons.Default.Info,
                        title = "Pusat Bantuan",
                        onClick = onBantuanClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tindakan Akun",
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
                MenuItem(
                    icon = Icons.Default.ExitToApp,
                    title = "Keluar dari Akun",
                    textColor = Color(0xFF8B1C31),
                    onClick = { showLogoutDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    textColor: Color = Color(0xFF212121),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF8F9FA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF8B1C31),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Detail",
            tint = Color.LightGray,
            modifier = Modifier.size(24.dp)
        )
    }
}