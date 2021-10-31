package com.example.coinempireprophet.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class CoinHistoryModel(@field:SerializedName("rolls") val rolls: ArrayList<CoinResultModel>)


data class CoinResultModel(
    @field:SerializedName("coin") val coin: String,
    @field:SerializedName("time") val time: String
)