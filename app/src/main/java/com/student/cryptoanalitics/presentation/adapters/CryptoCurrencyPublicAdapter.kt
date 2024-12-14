package com.student.cryptoanalitics.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.student.cryptoanalitics.databinding.CryptopublicItemBinding
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel

class CryptoCurrencyPublicAdapter(val click: CryptoCurrencyPublicClick) :
    ListAdapter<CryptoCurrencyModel, CryptoCurrencyPublicAdapter.CryptoCurrencyViewHolder>(
        CoinsDiffCallback
    ) {

    inner class CryptoCurrencyViewHolder(private val binding: CryptopublicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crypto: CryptoCurrencyModel) {
            binding.coinName.text = crypto.coinName
            binding.marketPrice.text = crypto.coinPrice
            Picasso.get().load(crypto.coinImg).into(binding.cryptoImg)

            click.addCrypto(binding.addCrypto, crypto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoCurrencyViewHolder {
        val binding =
            CryptopublicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoCurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoCurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object CoinsDiffCallback : DiffUtil.ItemCallback<CryptoCurrencyModel>() {
        override fun areItemsTheSame(oldItem: CryptoCurrencyModel, newItem: CryptoCurrencyModel): Boolean {
            return oldItem.coinName == newItem.coinName
        }

        override fun areContentsTheSame(oldItem: CryptoCurrencyModel, newItem: CryptoCurrencyModel): Boolean {
            return oldItem == newItem
        }
    }
}





