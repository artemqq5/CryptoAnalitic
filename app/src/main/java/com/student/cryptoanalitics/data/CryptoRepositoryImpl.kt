package com.student.cryptoanalitics.data

import com.student.cryptoanalitics.data.api.CryptoAPI
import com.student.cryptoanalitics.domain.repositories.CryptoRepository
import retrofit2.Call

class CryptoRepositoryImpl(private val cryptoAPI: CryptoAPI) : CryptoRepository {

    override fun getCoinHtml(coinName: String): Call<String> {
        return cryptoAPI.getCoinHtmlByAPI(coinName)
    }

    override fun getCryptoCurrenciesHtml(page: Int): Call<String> {
        return cryptoAPI.getCryptoCurrenciesHTMLByAPI(page)
    }

}