package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.data.database.table.CoinsTable
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository

class AddCoinUseCase(private val cryptoDBRepository: CryptoDBRepository) {

    suspend fun addCoin(coin: CryptoCurrencyModel): Boolean {
        return cryptoDBRepository.addNewCoin(
            coin.toCoinsTable()
        ) != -1L
    }

    private fun CryptoCurrencyModel.toCoinsTable(): CoinsTable {
        val priceParsed = coinPrice.split("$")[1].replace(",", "").toFloat()

        return CoinsTable(
            coinName = coinName,
            coinPrice = priceParsed
        )
    }

}