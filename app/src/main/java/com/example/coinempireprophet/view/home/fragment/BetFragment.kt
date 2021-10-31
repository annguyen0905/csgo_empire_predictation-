package com.example.coinempireprophet.view.home.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinempireprophet.R
import com.example.coinempireprophet.adapter.CoinAdapter
import com.example.coinempireprophet.databinding.FragmentBetBinding
import com.example.coinempireprophet.view.home.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_bet.*


class BetFragment : Fragment() {
    private val coinListAdapter = CoinAdapter(arrayListOf())

    private var isFirstTime = true

    private var previousResult = ""

    private var _binding: FragmentBetBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private var myCoinList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        val delay: Long = 5000 // 1000 milliseconds == 1 second
        Handler(
            Looper.getMainLooper()
        ).postDelayed(object : Runnable {
            override fun run() {
                viewModel.getCurrentSeed()
                Handler(Looper.getMainLooper()).postDelayed(this, delay)
            }
        }, delay)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBetBinding.inflate(inflater, container, false)
        binding.rvCoins.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
            adapter = coinListAdapter
        }
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.currentSeed.observe(this.viewLifecycleOwner, Observer { data ->
            viewModel.getCoinHistory(data.current_seed)
        })



        viewModel.coinListHistory.observe(this.viewLifecycleOwner, Observer { data ->
            if (myCoinList.size < data.rolls.size) {
                this.playSound()
                viewModel.isUpdate.postValue(true)
            }
            myCoinList.clear()
            for (coin in data.rolls) {
                myCoinList.add(coin.coin)
            }
            coinListAdapter.updateCoinList(myCoinList)
            calculateLogic(myCoinList)

        })


        viewModel.isUpdate.observe(this.viewLifecycleOwner, Observer { data ->
            binding.view1.visibility = View.VISIBLE
            binding.view2.visibility = View.VISIBLE
            if (isFirstTime) {
                isFirstTime = false
            } else {
                if (data == true) {
                    val handledCoinList = myCoinList
                    if (previousResult == handledCoinList[handledCoinList.size - 1]) {
                        this.playSoundOnWin()
                    }
                }
            }

        })
    }

    private fun calculateLogic( coinList: ArrayList<String>) {
        var isFound = false
        var isSkip = false
        var seed = 2
        var result = ""
        val size = coinList.size
        var handledCoinList = coinList
        handledCoinList.reverse()
        do {
            var prePattern = ""
            var comparePattern = ""
            try {
                prePattern = handledCoinList[size - 1] + coinList[size - 2] + coinList[size - 3]

                comparePattern =
                    coinList[size - seed] + coinList[size - (seed + 1)] + coinList[size - (seed + 2)]

            } catch (Exception: IndexOutOfBoundsException) {
                isSkip = true
                Toast.makeText(
                    requireContext(),
                    "Unable to find the possibility or not enough data to calculate please try again later",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (!isSkip) {
                if (prePattern == comparePattern) {
                    isFound = true
                    result = coinList[size - (seed - 1)]
                    previousResult = result
                    when (result) {
                        "t" -> {
                            binding.imgResult.setImageResource(R.drawable.tcoin)
                            binding.txtPos.text =
                                getString(R.string.round, (size - (seed - 1)).toString())
                        }
                        "ct" -> {
                            binding.imgResult.setImageResource(R.drawable.ctcoin)
                            binding.txtPos.text =
                                getString(R.string.round, (size - (seed - 1)).toString())
                        }
                        else -> {
                            binding.imgResult.setImageResource(R.drawable.bonus)
                            binding.txtPos.text = getString(R.string.round, (size - (seed - 1)).toString())
                        }
                    }
                } else {
                    seed += 1
                }
            }
        } while (!isFound && !isSkip)
    }

    private fun playSound() {
        val mp: MediaPlayer = MediaPlayer.create(requireContext(), R.raw.rollend)
        mp.start()
    }

    private fun playSoundOnWin() {
        val mp: MediaPlayer = MediaPlayer.create(requireContext(), R.raw.rollwin)
        mp.start()
    }
}