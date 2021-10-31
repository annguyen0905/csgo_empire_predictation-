package com.example.coinempireprophet.model

import com.google.gson.annotations.SerializedName


data class SeedModel(@field:SerializedName("announcement") val announcement: String,
              @field:SerializedName("current_seed") val current_seed: String )