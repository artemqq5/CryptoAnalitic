package com.student.cryptoanalitics.domain.models

data class CryptoCoinModel(
    val marketCap: String?,
    val volume24h: String?,
    val fdv: String?,
    val volMktCap: String?,
    val totalSupply: String?,
    val circulatingSupply: String?,
    val marketPrice: String?,
    val coinName: String?
)