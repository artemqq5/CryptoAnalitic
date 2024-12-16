package com.student.cryptoanalitics.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.student.cryptoanalitics.data.database.table.CoinsTable

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: CoinsTable): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCoins(coins: List<CoinsTable>)

    @Delete
    suspend fun delete(coin: CoinsTable)

    @Query("SELECT * FROM coins ORDER BY coinPrice DESC LIMIT 5 OFFSET :offset COLLATE NOCASE")
    suspend fun getCoinsWithPagination(offset: Int): List<CoinsTable>

    @Query("SELECT * FROM coins WHERE coinName =:coinName COLLATE NOCASE")
    suspend fun getCoin(coinName: String): CoinsTable?

}