package com.student.cryptoanalitics.domain.repositories

import retrofit2.Response

interface CryptoHtmlRepository {

    suspend fun getCoinHtml(coinName: String): Response<String>
    suspend fun getCryptoCurrenciesHtml(page: Int): Response<String>
}