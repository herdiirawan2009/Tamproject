package com.example.suralampung.viewmodel

import androidx.lifecycle.ViewModel
import com.example.suralampung.data.model.Barang
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _listBarang = MutableStateFlow<List<Barang>>(emptyList())
    val listBarang: StateFlow<List<Barang>> = _listBarang

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    init {
        fetchBarang()
    }

    fun fetchBarang() {
        _isLoading.value = true
        _isError.value = false
        FirebaseFirestore.getInstance().collection("barang").get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { doc ->
                    Barang(
                        nama = doc.getString("nama") ?: "",
                        harga = (doc.getLong("harga") ?: 0L).toInt(),
                        lokasi = doc.getString("lokasi") ?: "",
                        deskripsi = doc.getString("deskripsi") ?: "",
                        imageUrl = doc.getString("image_url") ?: "",
                        id_penjual = doc.getString("id_penjual") ?: ""
                    )
                }
                _listBarang.value = list
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isError.value = true
                _isLoading.value = false
            }
    }
}
