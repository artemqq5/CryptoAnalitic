package com.student.cryptoanalitics.presentation.adapters.public_coins

import com.google.android.material.button.MaterialButton
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel

interface CryptoCurrencyPublicClick {
    fun addCrypto(button: MaterialButton, cryptoCurrencyModel: CryptoCurrencyModel)
}