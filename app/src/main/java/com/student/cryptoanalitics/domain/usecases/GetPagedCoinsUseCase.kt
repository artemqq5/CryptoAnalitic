package com.student.cryptoanalitics.domain.usecases

import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPagedCoinsUseCase(private val repositoryDBRepository: CryptoDBRepository) {

    fun execute(page: Int): Flow<List<String>> {
        return flow {
            emit(repositoryDBRepository.getAllCoinPagination(page * 5 - 5).map { it.coinName })
        }
    }

}