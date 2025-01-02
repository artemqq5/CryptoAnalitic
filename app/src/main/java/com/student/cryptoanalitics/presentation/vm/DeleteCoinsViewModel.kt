package com.student.cryptoanalitics.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.cryptoanalitics.domain.usecases.DeleteCoinUseCase
import kotlinx.coroutines.launch

class DeleteCoinsViewModel(private val deleteCoinUseCase: DeleteCoinUseCase) : ViewModel() {

    fun deleteCoin(coinName: String) {
        viewModelScope.launch {
            deleteCoinUseCase.deleteCoinDB(coinName)
        }
    }

}