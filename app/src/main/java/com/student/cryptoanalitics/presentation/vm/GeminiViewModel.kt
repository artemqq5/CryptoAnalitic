package com.student.cryptoanalitics.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import com.student.cryptoanalitics.domain.usecases.GeminiUseCase
import com.student.cryptoanalitics.domain.usecases.GeminiUseCase.Companion.DEFAULT_RESPONSE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeminiViewModel(private val geminiUseCase: GeminiUseCase): ViewModel() {

    private val _analiseCoinGemini: MutableStateFlow<String?> = MutableStateFlow(null)
    val analiseCoinGemini: StateFlow<String?> = _analiseCoinGemini


    fun requestAnaliseCoin(coinModel: CryptoCoinModel) {
        viewModelScope.launch {
            geminiUseCase.requestGemini(coinModel).collect {
                _analiseCoinGemini.value = it.text ?: DEFAULT_RESPONSE
            }
        }
    }

}