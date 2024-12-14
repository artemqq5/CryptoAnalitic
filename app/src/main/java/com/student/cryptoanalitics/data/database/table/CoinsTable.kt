package com.student.cryptoanalitics.data.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "coins",
    indices = [Index(value = ["coinName"], unique = true)])
data class CoinsTable(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "coinName") val coinName: String,
    @ColumnInfo(name = "coinPrice") val coinPrice: String,
    @ColumnInfo(name = "marketCap") val marketCap: String?,
    @ColumnInfo(name = "volume24h") val volume24h: String?,
    @ColumnInfo(name = "fdv") val fdv: String?,
    @ColumnInfo(name = "volMktCap") val volMktCap: String?,
    @ColumnInfo(name = "totalSupply") val totalSupply: String?,
    @ColumnInfo(name = "circulatingSupply") val circulatingSupply: String?,
)