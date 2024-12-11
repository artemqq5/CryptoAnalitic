package com.student.cryptoanalitics.domain.usecases

import android.util.Log
import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrenciesModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import com.student.cryptoanalitics.domain.repositories.CryptoRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetCryptoCurrenciesHTMLUseCase(private val cryptoRepository: CryptoRepository) {

    fun getCoinsList(page: Int) {
        cryptoRepository.getCryptoCurrenciesHtml(page).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful && response.body() != null) {
                    val htmlContent: String = response.body()!!
                    parseHtmlCryptoCurrencies(htmlContent)
                } else {
                    Log.e("Error", "Response not successful")
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("Error", "Request failed: " + t.message)
            }
        })
    }

    private fun parseHtmlCryptoCurrencies(content: String): List<CryptoCurrencyModel>? {
        return try {
            val document: Document = Jsoup.parse(content)

            // Знаходимо таблицю з класом `sc-936354b2-3 tLXcG cmc-table`
            val table = document.selectFirst("table.sc-936354b2-3.tLXcG.cmc-table")

            // Отримуємо всі рядки таблиці
            val rows = table?.select("tr") ?: return null

            // Парсимо кожен рядок
            val cryptoCoins = rows.mapNotNull { row ->
                val coinName = row.selectFirst("p.coin-item-name")?.text() // Назва монети
                val marketPrice = row.selectFirst("div.sc-b3fc6b7-0 span")?.text() // Ціна монети

                // Перевіряємо, чи знайдено потрібні дані
                if (coinName != null && marketPrice != null) {
                    CryptoCurrencyModel(
                        coinName = coinName,
                        coinPrice = marketPrice
                    )
                } else {
                    null // Пропускаємо, якщо дані не знайдено
                }
            }

            // Логуємо результат
            mylog(cryptoCoins)
            cryptoCoins
        } catch (e: Exception) {
            mylog(e)
            null
        }
    }



}