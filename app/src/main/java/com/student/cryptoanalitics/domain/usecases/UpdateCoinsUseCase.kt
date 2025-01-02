package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.data.database.table.CoinsTable
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository

class UpdateCoinsUseCase(private val repositoryDBRepository: CryptoDBRepository) {

    suspend fun updateCoinsDB(coins: List<CryptoCoinModel>) {
        val transformCoinsTypeList = coins.map { coin -> coin.toCoinsTable() }
        repositoryDBRepository.updatePriceCoin(transformCoinsTypeList)
    }

    private fun CryptoCoinModel.toCoinsTable(): CoinsTable {
        val priceParsed = marketPrice!!.split("$")[1].replace(",", "").toFloat()

        return CoinsTable(
            coinName = coinName,
            coinPrice = priceParsed
        )
    }

}