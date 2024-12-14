package com.student.cryptoanalitics.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.student.cryptoanalitics.domain.models.CryptoCoinModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: CryptoCoinModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCoins(coins: List<CryptoCoinModel>)

    @Delete
    suspend fun delete(coin: CryptoCoinModel)

    @Query("SELECT * FROM coins")
    suspend fun getCoinsWithPagination(): PagingSource<Int, CryptoCoinModel>

}