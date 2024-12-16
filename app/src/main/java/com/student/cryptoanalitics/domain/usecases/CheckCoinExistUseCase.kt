package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.App.Companion.mylog
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository
import java.util.Locale

class CheckCoinExistUseCase(private val cryptoDBRepository: CryptoDBRepository) {

    suspend fun checkCoinExist(coinName: String): Boolean {
        return cryptoDBRepository.checkCoinExist(coinName) != null
    }

}
