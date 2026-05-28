package com.example.suralampung.data.network

import com.example.suralampung.data.model.Barang
import retrofit2.http.GET

interface ApiService {
    @GET("sumber_daya_lampung.json")
    suspend fun getBarang(): List<Barang>
}
