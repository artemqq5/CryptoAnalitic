package com.student.cryptoanalitics.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.student.cryptoanalitics.data.database.table.CoinsTable

@Database(entities = [CoinsTable::class], version = 1, exportSchema = false)
abstract class CryptoDataBase : RoomDatabase() {
    abstract fun getDAO(): DAO

    companion object {
        const val DATABASE_NAME = "cryptodb"

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                db.execSQL("ALTER TABLE locationTable ADD COLUMN status_active INTEGER NOT NULL DEFAULT 0")
//            }
//        }

        @Volatile
        private var INSTANCE: CryptoDataBase? = null

        fun getDatabase(context: Context): CryptoDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CryptoDataBase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}