package com.student.cryptoanalitics.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrencyModel
import com.student.cryptoanalitics.domain.usecases.AddCoinUseCase
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsertCoinsViewModel(
    private val addCoinUseCase: AddCoinUseCase,
) : ViewModel() {

    fun addNewCoin(coin: CryptoCurrencyModel, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(addCoinUseCase.addCoin(coin))
        }
    }

}