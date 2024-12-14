package com.student.cryptoanalitics.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.cryptoanalitics.domain.models.currencies.CryptoCurrenciesModel
import com.student.cryptoanalitics.domain.usecases.CheckCoinExistUseCase
import com.student.cryptoanalitics.domain.usecases.GetCryptoCurrenciesHTMLUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PublicCoinsViewModel(
    private val getCryptoCurrenciesHTMLUseCase: GetCryptoCurrenciesHTMLUseCase,
    private val checkCoinExistUseCase: CheckCoinExistUseCase
): ViewModel() {

    private val _coins = MutableStateFlow<CryptoCurrenciesModel?>(CryptoCurrenciesModel(emptyList()))
    val coins: StateFlow<CryptoCurrenciesModel?> = _coins

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1

    init {
        if (currentPage == 1) {
            loadCoins()
        }
    }

    fun loadCoins() {
        if (_isLoading.value) return
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            getCryptoCurrenciesHTMLUseCase.getCoinsList(currentPage).collect{ result ->
                result?.let {
                    if (it.coinList.isNotEmpty()) {
                        _coins.value = CryptoCurrenciesModel(coinList = (_coins.value?.coinList.orEmpty() + it.coinList))
                        currentPage++
                    }
                }

                _isLoading.value = false
            }
        }

    }

    fun coinExist(coinName: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(checkCoinExistUseCase.checkCoinExist(coinName))
        }
    }

}