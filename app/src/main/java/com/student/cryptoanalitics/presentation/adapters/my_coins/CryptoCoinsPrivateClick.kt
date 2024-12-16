package com.student.cryptoanalitics.presentation.adapters.my_coins

import android.widget.RadioButton
import com.google.android.material.button.MaterialButton
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel

interface CryptoCoinsPrivateClick {
    fun clickCoin(radioButton: RadioButton, cryptoCoinModel: CryptoCoinModel)
}