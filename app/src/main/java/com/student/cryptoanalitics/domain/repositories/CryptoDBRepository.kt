package com.student.cryptoanalitics.domain.repositories

import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response

interface CryptoDBRepository {
    suspend fun addNewCoin(coin: CryptoCoinModel)
    suspend fun checkCoinExist(coinName: String): CryptoCoinModel?
}