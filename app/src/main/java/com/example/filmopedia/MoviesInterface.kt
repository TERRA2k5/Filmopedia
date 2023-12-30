package com.example.filmopedia

import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.MoviesData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface MoviesInterface {

    @Headers(
        "X-RapidAPI-Key: 27e1e9f669msh2a7e5cad1e087f2p186441jsn5099011b7540",
        "X-RapidAPI-Host: moviesdatabase.p.rapidapi.com"
    )
    @GET("/titles")
        fun getMoviesList(@Query("page") page: Int, @Query("list") list: String ):Call<MovieResponse>


}