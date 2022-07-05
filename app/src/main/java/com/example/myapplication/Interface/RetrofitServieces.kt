package com.example.myapplication.Interface

import com.example.myapplication.Model.Movie
import retrofit2.Call
import retrofit2.http.GET

public interface RetrofitServices {
    @GET("marvel")
    fun getMovieList(): Call<MutableList<Movie>>
}