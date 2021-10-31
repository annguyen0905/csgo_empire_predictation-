package com.example.coinempireprophet.retrofit

import com.example.coinempireprophet.model.CoinHistoryModel
import com.example.coinempireprophet.model.SeedModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.ArrayList


interface Api {
    @GET("roulette")
    suspend fun getCurrentSeed(): Response<SeedModel>

    @GET("roulette/history?")
    suspend fun getCoinHistory(@Query("seed") seed: String): Response<CoinHistoryModel>


}