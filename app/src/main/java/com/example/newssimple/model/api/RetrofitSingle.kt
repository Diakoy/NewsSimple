package com.example.newssimple.model.api

import com.example.newssimple.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingle {


  var apiService :ApiService ?= null
     get() {

         if (field == null){
             val retrofit = Retrofit
                 .Builder()
                 .baseUrl(BASE_URL)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
             field = retrofit.create(ApiService::class.java)
         }
         return field
     }


}