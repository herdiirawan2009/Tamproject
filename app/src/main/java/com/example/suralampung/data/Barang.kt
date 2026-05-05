package com.example.suralampung.data

import com.google.gson.annotations.SerializedName

data class Barang(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("harga")
    val harga: Int,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("image_url")
    val imageUrl: String
)