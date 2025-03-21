package com.example.eveerdnieva.data.model

import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    @GET("v2/top-headlines?country=us&apiKey=6b0b4a86f6ab473681d43c3c01c304f9")
    suspend fun getNewsList(@Query("category") category:String): News
    companion object{
        const val URL="https://newsapi.org/"
    }
}