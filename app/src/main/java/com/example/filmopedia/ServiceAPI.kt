package com.example.filmopedia

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceAPI {

    companion object{

        private var retrofit: Retrofit? = null
        private val base = "https://moviesdatabase.p.rapidapi.com"


        fun getInstance(): Retrofit{
            if (retrofit == null){
                retrofit = Retrofit.Builder().baseUrl(base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!
        }
    }
}