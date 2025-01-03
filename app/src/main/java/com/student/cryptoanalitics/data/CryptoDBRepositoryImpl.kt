package com.student.cryptoanalitics.data

import androidx.paging.PagingSource
import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.data.database.DAO
import com.student.cryptoanalitics.data.database.table.CoinsTable
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository

class CryptoDBRepositoryImpl(private val db: DAO) : CryptoDBRepository {

    override suspend fun getAllCoinPagination(offset: Int): List<CoinsTable> {
        val response = db.getCoinsWithPagination(offset)
        mylog(response)
        return response
    }

    override suspend fun addNewCoin(coin: CoinsTable): Long {
        return db.insert(coin)
    }

    override suspend fun checkCoinExist(coinName: String): CoinsTable? {
        return db.getCoin(coinName)
    }

    override suspend fun updatePriceCoin(coins: List<CoinsTable>) {
        db.updateCoins(coins)
    }

    override suspend fun deleteCoin(coinName: String) {
        db.delete(coinName)
    }

}