package com.student.cryptoanalitics.data.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoAPI {

    companion object {
        const val BASE_URL_CRYPTO = "https://coinmarketcap.com/"
    }

    @GET("currencies/{coin}")
    suspend fun getCoinHtmlByAPI(
        @Path("coin") coinName: String
    ): Response<String>

    @GET("/")
    suspend fun getCryptoCurrenciesHTMLByAPI(
        @Query("page") page: Int
    ): Response<String>

}