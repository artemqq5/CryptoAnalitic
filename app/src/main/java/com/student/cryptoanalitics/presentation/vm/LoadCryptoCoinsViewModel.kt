package com.student.cryptoanalitics.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetPagedCoinsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadCryptoCoinsViewModel(
    private val getPagedCoinsUseCase: GetPagedCoinsUseCase,
    private val getCoinHTMLUseCase: GetCoinHTMLUseCase
) : ViewModel() {

    private val coinsFlowMutable: MutableStateFlow<List<CryptoCoinModel>> =
        MutableStateFlow(emptyList())
    val coinsFlow: StateFlow<List<CryptoCoinModel>> = coinsFlowMutable

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var currentPage = 1

    init {
        if (currentPage == 1) {
            loadCoinsData(true)
        }
    }

    fun loadCoinsData(isRefresh: Boolean = false) {
        if (_isLoading.value) return

        if (isRefresh) {
            currentPage = 1
            coinsFlowMutable.value = emptyList()
            _isRefreshing.value = true
        }


        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            getPagedCoinsUseCase.execute(currentPage).collect { listData ->

                val updatedCoinsList = mutableListOf<CryptoCoinModel>()

                listData.forEach { coinName ->
                    getCoinHTMLUseCase.getCoinData(coinName)?.let { coin ->
                        updatedCoinsList.add(coin)
                    }
                }

                val updatedList = coinsFlowMutable.value + updatedCoinsList
                coinsFlowMutable.value = updatedList.distinctBy { coin -> coin.marketPrice!!.split("$")[1].replace(",", "").toFloat() }
                    .sortedByDescending { coin -> coin.marketPrice!!.split("$")[1].replace(",", "").toFloat() }

                currentPage++

                _isLoading.value = false
                _isRefreshing.value = false
            }

        }
    }

}