package com.example.eveerdnieva

import com.example.eveerdnieva.data.model.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val interceptor:HttpLoggingInterceptor=HttpLoggingInterceptor().apply {
        level=HttpLoggingInterceptor.Level.BODY
    }
    private val client:OkHttpClient=OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    val api: Api =Retrofit.Builder()
        .baseUrl(Api.URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(Api::class.java)
}