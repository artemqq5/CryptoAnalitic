package com.student.cryptoanalitics.presentation.adapters.my_coins

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.student.cryptoanalitics.databinding.MycryptoItemBinding
import com.student.cryptoanalitics.domain.models.CryptoCoinModel

class CryptoCoinsPrivateAdapter(private val click: CryptoCoinsPrivateClick) :
    ListAdapter<CryptoCoinModel, CryptoCoinsPrivateAdapter.CryptoViewHolder>(
        DiffCallback
    ) {
    inner class CryptoViewHolder(private val binding: MycryptoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coin: CryptoCoinModel) {
            binding.coinName.text = coin.coinName
            binding.marketPrice.text = coin.marketPrice
            binding.totalSupply.text = coin.totalSupply
            Picasso.get().load(coin.coinImg).into(binding.coinImg)

            itemView.setOnClickListener {
                click.clickCoin(binding.radioButton, coin)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = MycryptoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CryptoCoinModel>() {
            override fun areItemsTheSame(
                oldItem: CryptoCoinModel,
                newItem: CryptoCoinModel
            ): Boolean {
                return oldItem.coinName == newItem.coinName
            }

            override fun areContentsTheSame(
                oldItem: CryptoCoinModel,
                newItem: CryptoCoinModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}