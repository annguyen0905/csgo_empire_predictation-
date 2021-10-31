package com.example.coinempireprophet.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coinempireprophet.R
import kotlinx.android.synthetic.main.coin_item.view.*

class CoinAdapter(private var coins: ArrayList<String>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
    fun updateCoinList(newCoins: ArrayList<String>) {
        coins.clear()
        coins.addAll(newCoins)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CoinViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
    )

    override fun getItemCount() = coins.size
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(coins[position])
    }

    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.imgCoin
        fun bind(coin: String) {
            if (coin == "ct") {
                imageView.setImageResource(R.drawable.ctcoin)
            } else if (coin == "t") {
                imageView.setImageResource(R.drawable.tcoin)
            } else {
                imageView.setImageResource(R.drawable.bonus)
            }
        }
    }
}