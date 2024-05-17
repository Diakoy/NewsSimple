package com.example.newssimple.model.api

import com.example.newssimple.model.data.NewsData
import com.example.newssimple.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(API_KEY)
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String = "us",
        @Query("page") page: Int = 1
    ): Response<NewsData>

    @Headers(API_KEY)
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String ,
        @Query("page") pageNumber :Int = 1
    ) :Response<NewsData>


}