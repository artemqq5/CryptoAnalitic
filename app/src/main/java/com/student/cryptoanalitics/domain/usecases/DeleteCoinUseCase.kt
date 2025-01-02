package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository

class DeleteCoinUseCase(private val repositoryDBRepository: CryptoDBRepository) {

    suspend fun deleteCoinDB(coinName: String) {
        repositoryDBRepository.deleteCoin(coinName)
    }

}