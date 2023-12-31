package com.example.filmopedia

import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.filmopedia.data.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchInterface{

    @Headers(
        "X-RapidAPI-Key: 27e1e9f669msh2a7e5cad1e087f2p186441jsn5099011b7540",
        "X-RapidAPI-Host: moviesdatabase.p.rapidapi.com"
    )
    @GET("/titles/search/keyword/{key}")
    fun getMoviesSearch(@Path("key") key : String): Call<MovieResponse>

}