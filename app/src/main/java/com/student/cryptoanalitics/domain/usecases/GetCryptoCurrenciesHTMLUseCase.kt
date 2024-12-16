package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrenciesModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import com.student.cryptoanalitics.domain.repositories.CryptoHtmlRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class GetCryptoCurrenciesHTMLUseCase(private val cryptoHtmlRepository: CryptoHtmlRepository) {

    fun getCoinsList(page: Int): Flow<CryptoCurrenciesModel?> {
        return flow {
            try {
                val response = cryptoHtmlRepository.getCryptoCurrenciesHtml(page)
                if (response.isSuccessful && response.body() != null) {
                    emit(parseHtmlCryptoCurrencies(response.body()!!))
                } else {
                    emit(null)
                    throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                emit(null)
                throw e
            }
        }
    }

    private fun parseHtmlCryptoCurrencies(content: String): CryptoCurrenciesModel? {
        return try {
            val document: Document = Jsoup.parse(content)

            val table = document.selectFirst("table.sc-936354b2-3.tLXcG.cmc-table")

            val rows = table?.select("tr") ?: return null

            val cryptoCoins = rows.mapNotNull { row ->
                val coinName = row.selectFirst("p.coin-item-name")?.text() // Назва монети
                val marketPrice = row.selectFirst("div.sc-b3fc6b7-0 span")?.text() // Ціна монети
                val coinImg = row.selectFirst("div.sc-65e7f566-0 img")?.attr("src").toString()

                if (coinName != null && marketPrice != null) {
                    CryptoCurrencyModel(
                        coinName = coinName,
                        coinPrice = marketPrice,
                        coinImg = coinImg
                    )
                } else {
                    null
                }
            }

//            mylog(cryptoCoins)
            CryptoCurrenciesModel(cryptoCoins)
        } catch (e: Exception) {
            mylog(e)
            null
        }
    }



}