package com.student.cryptoanalitics.domain.usecases

import android.util.Log
import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.repositories.CryptoRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetCoinHTMLUseCase(private val cryptoRepository: CryptoRepository) {

    fun getCoinData(coinName: String) {
        cryptoRepository.getCoinHtml(coinName).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful && response.body() != null) {
                    val htmlContent: String = response.body()!!
                    parseHtmlCoin(htmlContent)
                } else {
                    Log.e("Error", "Response not successful")
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("Error", "Request failed: " + t.message)
            }
        })
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
            val marketPrice = document.selectFirst("span[data-test='text-cdp-price-display']")?.text()
            val coinName = document.selectFirst("span[data-role='coin-name']")?.attr("title")

            val model = CryptoCoinModel(
                marketCap = marketCap,
                volume24h = volume24h,
                fdv = fdv,
                volMktCap = volMktCap,
                totalSupply = totalSupply,
                circulatingSupply = circulatingSupply,
                marketPrice = marketPrice,
                coinName = coinName
            )
            mylog(model)
            model
        } catch (e: Exception) {
            mylog(e)
            null
        }
    }


}