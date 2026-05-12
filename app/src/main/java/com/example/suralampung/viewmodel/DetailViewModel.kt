package com.example.suralampung.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suralampung.ui.screens.Barang
import com.example.suralampung.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = RetrofitClient.instance.getBarang()
                val found = list.find { it.nama == barangNama }
                _barang.value = found
                _isError.value = found == null
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}
