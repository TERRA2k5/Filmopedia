package com.example.filmopedia

import com.example.filmopedia.data.MovieResponse
import com.example.filmopedia.data.RatingData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RatingInterface {

    @Headers(
        "X-RapidAPI-Key: 27e1e9f669msh2a7e5cad1e087f2p186441jsn5099011b7540",
        "X-RapidAPI-Host: moviesdatabase.p.rapidapi.com"
    )
    @GET("/titles/{id}/ratings")
    fun getRating(@Path("id") id : String) : Call<RatingData>
}