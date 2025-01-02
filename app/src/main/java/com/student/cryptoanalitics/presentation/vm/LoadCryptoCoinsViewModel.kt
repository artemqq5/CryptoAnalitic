package com.student.cryptoanalitics.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetPagedCoinsUseCase
import com.student.cryptoanalitics.domain.usecases.UpdateCoinsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoadCryptoCoinsViewModel(
    private val getPagedCoinsUseCase: GetPagedCoinsUseCase,
    private val getCoinHTMLUseCase: GetCoinHTMLUseCase,
    private val updateCoinsUseCase: UpdateCoinsUseCase
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

    fun loadCoinsData(isRefresh: Boolean = false, immediate: Boolean = false) {
        if (_isLoading.value || _isRefreshing.value) if (!immediate) return


        if (isRefresh) {
            currentPage = 1
            coinsFlowMutable.value = emptyList()
            _isRefreshing.value = true
        } else {
            _isLoading.value = true
        }

        viewModelScope.launch(Dispatchers.IO) {
            getPagedCoinsUseCase.execute(currentPage).collect { listData ->

                val updatedCoinsList = mutableListOf<CryptoCoinModel>()

                listData.forEach { coinName ->
                    getCoinHTMLUseCase.getCoinData(coinName)?.let { coin ->
                        updatedCoinsList.add(coin)
                    }
                }

                // here updating database *********
                viewModelScope.launch(Dispatchers.IO) {
                    updateCoinsUseCase.updateCoinsDB(updatedCoinsList)
                }
                // here updating database *********

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