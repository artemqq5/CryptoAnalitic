package com.student.cryptoanalitics.domain.repositories

import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response

interface CryptoRepository {

    suspend fun getCoinHtml(coinName: String): Response<String>
    suspend fun getCryptoCurrenciesHtml(page: Int): Response<String>
}