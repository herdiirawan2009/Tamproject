package com.example.suralampung.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suralampung.ui.screens.Barang
import com.example.suralampung.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                _listBarang.value = RetrofitClient.instance.getBarang()
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}
