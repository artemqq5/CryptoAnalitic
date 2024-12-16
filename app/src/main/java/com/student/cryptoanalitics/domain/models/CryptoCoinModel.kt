package com.student.cryptoanalitics.domain.models

data class CryptoCoinModel(
    val marketCap: String? = null,
    val volume24h: String? = null,
    val fdv: String? = null,
    val volMktCap: String? = null,
    val totalSupply: String? = null,
    val circulatingSupply: String? = null,
    val marketPrice: String? = null,
    val coinImg: String? = null,
    val coinName: String
)