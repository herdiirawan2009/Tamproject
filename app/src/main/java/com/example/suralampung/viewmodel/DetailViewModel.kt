package com.example.suralampung.viewmodel

import androidx.lifecycle.ViewModel
import com.example.suralampung.data.model.Barang
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailViewModel : ViewModel() {
    private val _barang = MutableStateFlow<Barang?>(null)
    val barang: StateFlow<Barang?> = _barang

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    fun fetchDetail(barangNama: String?) {
        if (barangNama == null) {
            _isLoading.value = false
            _isError.value = true
            return
        }

        _isLoading.value = true
        _isError.value = false
        FirebaseFirestore.getInstance().collection("barang")
            .whereEqualTo("nama", barangNama).get()
            .addOnSuccessListener { result ->
                val doc = result.documents.firstOrNull()
                if (doc != null) {
                    _barang.value = Barang(
                        id = doc.id,
                        nama = doc.getString("nama") ?: "",
                        harga = (doc.getLong("harga") ?: 0L).toInt(),
                        lokasi = doc.getString("lokasi") ?: "",
                        deskripsi = doc.getString("deskripsi") ?: "",
                        imageUrl = doc.getString("image_url") ?: "",
                        id_penjual = doc.getString("id_penjual") ?: ""
                    )
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isError.value = true
                _isLoading.value = false
            }
    }
}
