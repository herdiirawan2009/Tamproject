package com.example.suralampung.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var barangTerpilih: List<Map<String, Any>> = emptyList()
}