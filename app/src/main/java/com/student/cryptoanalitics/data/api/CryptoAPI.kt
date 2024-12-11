package com.student.cryptoanalitics.data.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoAPI {

    companion object {
        const val BASE_URL_CRYPTO = "https://coinmarketcap.com/"
    }

    @GET("currencies/{coin}")
    fun getCoinHtmlByAPI(
        @Path("coin") coinName: String
    ): Call<String>

    @GET("/")
    fun getCryptoCurrenciesHTMLByAPI(
        @Query("page") page: Int
    ): Call<String>


}