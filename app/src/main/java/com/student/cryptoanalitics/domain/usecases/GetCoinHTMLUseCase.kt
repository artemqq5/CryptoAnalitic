package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.repositories.CryptoHtmlRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class GetCoinHTMLUseCase(private val cryptoHtmlRepository: CryptoHtmlRepository) {

    suspend fun getCoinData(coinName: String): CryptoCoinModel? {
        return try {
                val response = cryptoHtmlRepository.getCoinHtml(coinName)
                if (response.isSuccessful && response.body() != null) {
                    parseHtmlCoin(response.body()!!)
                } else {
                    throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                return null
            }
        }

    private fun parseHtmlCoin(content: String): CryptoCoinModel? {
        return try {
            val document: Document = Jsoup.parse(content)

            val marketCap = document.selectFirst("dt:contains(Market cap) + dd span")?.text()
            val volume24h = document.selectFirst("dt:contains(Volume (24h)) + dd span")?.text()
            val fdv = document.selectFirst("dt:contains(FDV) + dd span")?.text()
            val volMktCap = document.selectFirst("dt:contains(Vol/Mkt Cap (24h)) + dd")?.text()
            val totalSupply = document.selectFirst("dt:contains(Total supply) + dd span")?.text()
            val circulatingSupply = document.selectFirst("dt:contains(Circulating supply) + dd span")?.text()

            val marketPrice = runCatching {
                document.selectFirst("span[data-test='text-cdp-price-display']")?.text()
                    ?: throw Exception("price is null")
            }.getOrElse { throw Exception("Failed to get market price: ${it.message}") }

            val coinName = runCatching {
                document.selectFirst("span[data-role='coin-name']")?.attr("title")
                    ?: throw Exception("name is null")
            }.getOrElse { throw Exception("Failed to get coin name: ${it.message}") }

            val coinImg = document.selectFirst("div.sc-65e7f566-0 img")?.attr("src").toString()


            val model = CryptoCoinModel(
                marketCap = marketCap,
                volume24h = volume24h,
                fdv = fdv,
                volMktCap = volMktCap,
                totalSupply = totalSupply,
                circulatingSupply = circulatingSupply,
                marketPrice = marketPrice,
                coinName = coinName,
                coinImg = coinImg
            )
            model
        } catch (e: Exception) {
            mylog(e)
            null
        }
    }


}