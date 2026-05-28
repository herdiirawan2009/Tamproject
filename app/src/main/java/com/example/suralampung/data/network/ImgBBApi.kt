package com.example.suralampung.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

data class ImgBBResponse(
    val data: ImgBBData
)

data class ImgBBData(
    val url: String
)

interface ImgBBApi {
    @FormUrlEncoded
    @POST("1/upload")
    suspend fun uploadImage(
        @Query("key") apiKey: String,
        @Field("image") base64Image: String
    ): ImgBBResponse

    companion object {
        private const val BASE_URL = "https://api.imgbb.com/"

        val instance: ImgBBApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImgBBApi::class.java)
        }
    }
}
