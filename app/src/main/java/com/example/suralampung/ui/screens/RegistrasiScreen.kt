package com.example.suralampung.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.suralampung.ui.theme.BackgroundGray
import com.example.suralampung.ui.theme.PrimaryDark
import com.example.suralampung.ui.theme.PrimaryRed
import com.example.suralampung.ui.theme.SecondaryGold
import com.example.suralampung.ui.theme.TextPrimary
import com.example.suralampung.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

@Composable
fun RegisterScreen(navController: NavHostController) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PrimaryDark, PrimaryRed)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Daftar SuraLampung",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bangun akses sumber daya yang lebih terbuka.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFDE7A1)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Buat Akun Baru",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Lengkapi data berikut.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    RegisterField("Nama Lengkap", "Masukkan nama", nama) { nama = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField("Email", "Masukkan email", email) { email = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterPasswordField("Password", "Masukkan password", password) { password = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterPasswordField("Konfirmasi Password", "Ulangi password", konfirmasiPassword) { konfirmasiPassword = it }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(context, "Data tidak lengkap", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (password != konfirmasiPassword) {
                                Toast.makeText(context, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val userId = task.result?.user?.uid
                                        if (userId != null) {
                                            val userMap = hashMapOf(
                                                "nama" to nama,
                                                "email" to email,
                                                "created_at" to FieldValue.serverTimestamp()
                                            )

                                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                                .set(userMap)
                                                .addOnCompleteListener { firestoreTask ->
                                                    isLoading = false
                                                    if (firestoreTask.isSuccessful) {
                                                        Toast.makeText(context, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()
                                                        navController.navigate("home") {
                                                            popUpTo("login") { inclusive = true }
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, "Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryRed
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Text(
                                text = "Daftar",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sudah punya akun? Login",
                        color = SecondaryGold,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterField(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        color = TextPrimary,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryRed,
            unfocusedBorderColor = Color(0xFFD6D6D6),
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun RegisterPasswordField(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    // State untuk mengatur visibilitas diletakkan di sini
    var passwordVisible by remember { mutableStateOf(false) }

    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        color = TextPrimary,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        // Mengatur transformasi visual berdasarkan state
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        // Menambahkan ikon mata di sisi kanan
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryRed,
            unfocusedBorderColor = Color(0xFFD6D6D6),
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}