package com.student.cryptoanalitics.domain.repositories

import retrofit2.Call

interface CryptoRepository {

    fun getCoinHtml(coinName: String): Call<String>
    fun getCryptoCurrenciesHtml(page: Int): Call<String>

}