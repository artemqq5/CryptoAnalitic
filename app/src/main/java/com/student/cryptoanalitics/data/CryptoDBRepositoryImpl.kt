package com.student.cryptoanalitics.data

import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.data.database.DAO
import com.student.cryptoanalitics.data.database.table.CoinsTable
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository

class CryptoDBRepositoryImpl(private val db: DAO) : CryptoDBRepository {
    override suspend fun addNewCoin(coin: CryptoCoinModel) {
        return db.insert(coin.toCoinsTable())
    }

    override suspend fun checkCoinExist(coinName: String): CryptoCoinModel? {
        db.insert(
            CoinsTable(
                coinName = "coinName",
                coinPrice = "coinPrice",
                marketCap = "marketCap",
                volume24h = "volume24h",
                fdv = "fdv",
                volMktCap = "volMktCap",
                totalSupply = "totalSupply",
                circulatingSupply = "circulatingSupply"
            )
        )

        val allCoins = db.getAllCoins()
        mylog("All coins in DB: ${allCoins}")

        val res = db.getCoin(coinName)
        mylog(coinName)
        mylog(res.toString())
        return res?.toCryptoCoinModel()
    }


    private fun CoinsTable.toCryptoCoinModel(): CryptoCoinModel {
        return CryptoCoinModel(
            coinName = coinName,
            marketPrice = coinPrice,
            marketCap = marketCap,
            volume24h = volume24h,
            fdv = fdv,
            volMktCap = volMktCap,
            totalSupply = totalSupply,
            circulatingSupply = circulatingSupply
        )
    }

    private fun CryptoCoinModel.toCoinsTable(): CoinsTable {
        return CoinsTable(
            coinName = coinName,
            coinPrice = marketPrice,
            marketCap = marketCap,
            volume24h = volume24h,
            fdv = fdv,
            volMktCap = volMktCap,
            totalSupply = totalSupply,
            circulatingSupply = circulatingSupply
        )
    }
}