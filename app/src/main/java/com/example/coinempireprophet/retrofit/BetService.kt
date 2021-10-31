package com.example.coinempireprophet.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BetService() {
    private val BASE_URL = "https://csgoempire.com/api/v2/metadata/";
    fun getCurrentSeed(): Api{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}