package com.example.suralampung.data.model

import com.google.gson.annotations.SerializedName

data class Barang(
    val id: String = "",

    @SerializedName("nama")
    val nama: String = "",

    @SerializedName("harga")
    val harga: Int = 0,

    @SerializedName("lokasi")
    val lokasi: String = "",

    @SerializedName("deskripsi")
    val deskripsi: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("id_penjual")
    val id_penjual: String = "",

    @SerializedName("nomor_penjual")
    val nomor_penjual: String = ""
)