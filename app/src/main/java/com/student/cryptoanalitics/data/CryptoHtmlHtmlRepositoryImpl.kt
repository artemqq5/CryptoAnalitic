package com.student.cryptoanalitics.data

import com.student.cryptoanalitics.data.api.CryptoAPI
import com.student.cryptoanalitics.domain.repositories.CryptoHtmlRepository
import retrofit2.Response

class CryptoHtmlHtmlRepositoryImpl(private val cryptoAPI: CryptoAPI) : CryptoHtmlRepository {

    override suspend fun getCoinHtml(coinName: String): Response<String> {
        return cryptoAPI.getCoinHtmlByAPI(coinName)
    }

    override suspend fun getCryptoCurrenciesHtml(page: Int): Response<String> {
        return cryptoAPI.getCryptoCurrenciesHTMLByAPI(page)
    }

}