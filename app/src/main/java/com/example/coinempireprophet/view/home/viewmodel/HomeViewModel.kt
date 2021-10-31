package com.example.coinempireprophet.view.home.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinempireprophet.model.CoinHistoryModel
import com.example.coinempireprophet.model.SeedModel
import com.example.coinempireprophet.retrofit.BetService
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    private val betService = BetService().getCurrentSeed()
    val usersLoadError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    var job: Job? = null
    var job2: Job? = null
    val currentSeed = MutableLiveData<SeedModel>()
    var isUpdate = MutableLiveData<Boolean>()
    val coinListHistory = MutableLiveData<CoinHistoryModel>()
    private val _balance: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val balance: LiveData<Int>
        get() = this._balance

    private val _initialBet: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val initialBet: LiveData<Int>
        get() = this._initialBet

    fun setValueForLiveData(balance: Int, initialBet: Int) {
        _balance.postValue(balance)
        _initialBet.postValue(initialBet)
    }

    fun getCurrentSeed() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = betService.getCurrentSeed()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    usersLoadError.value = null
                    loading.value = false
                    currentSeed.postValue(
                        SeedModel(
                            announcement = response.body()?.announcement.toString(),
                            current_seed = response.body()?.current_seed.toString()
                        )
                    )
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    fun getCoinHistory(seed: String) {
        job2 = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = betService.getCoinHistory(seed)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    coinListHistory.postValue(response.body())
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        usersLoadError.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}