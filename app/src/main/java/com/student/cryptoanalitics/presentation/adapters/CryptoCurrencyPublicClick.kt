package com.student.cryptoanalitics.presentation.adapters

import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel

interface CryptoCurrencyPublicClick {
    fun addCrypto(cryptoCurrencyModel: CryptoCurrencyModel) {
    }
}