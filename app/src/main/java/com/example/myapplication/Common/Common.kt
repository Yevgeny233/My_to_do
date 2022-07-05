package com.example.myapplication.Common

import com.example.myapplication.Interface.RetrofitServices
import com.example.myapplication.Retrofit.RetrofitClient

object Common {
    val BASE_URL = "https://www.simplifiedcoding.net/demos/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getCleint(BASE_URL).create(RetrofitServices::class.java)
}