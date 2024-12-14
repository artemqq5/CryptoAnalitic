package com.student.cryptoanalitics.data

import com.student.cryptoanalitics.data.api.CryptoAPI
import com.student.cryptoanalitics.domain.repositories.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class CryptoRepositoryImpl(private val cryptoAPI: CryptoAPI) : CryptoRepository {

    override suspend fun getCoinHtml(coinName: String): Response<String> {
        return cryptoAPI.getCoinHtmlByAPI(coinName)
    }

    override suspend fun getCryptoCurrenciesHtml(page: Int): Response<String> {
        return cryptoAPI.getCryptoCurrenciesHTMLByAPI(page)
    }
}