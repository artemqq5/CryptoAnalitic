package com.student.cryptoanalitics.domain.repositories

import androidx.paging.PagingSource
import com.student.cryptoanalitics.data.database.table.CoinsTable
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response

interface CryptoDBRepository {
    suspend fun getAllCoinPagination(offset: Int): List<CoinsTable>
    suspend fun addNewCoin(coin: CoinsTable): Long
    suspend fun checkCoinExist(coinName: String): CoinsTable?
}