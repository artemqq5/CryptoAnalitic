package com.student.cryptoanalitics

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.student.cryptoanalitics.data.CryptoDBRepositoryImpl
import com.student.cryptoanalitics.data.CryptoRepositoryImpl
import com.student.cryptoanalitics.data.api.CryptoAPI
import com.student.cryptoanalitics.data.api.CryptoAPI.Companion.BASE_URL_CRYPTO
import com.student.cryptoanalitics.data.database.CryptoDataBase
import com.student.cryptoanalitics.data.database.CryptoDataBase.Companion.DATABASE_NAME
import com.student.cryptoanalitics.data.database.DAO
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository
import com.student.cryptoanalitics.domain.repositories.CryptoRepository
import com.student.cryptoanalitics.domain.usecases.CheckCoinExistUseCase
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetCryptoCurrenciesHTMLUseCase
import com.student.cryptoanalitics.presentation.vm.PublicCoinsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    repositoriesModule,
                    useCasesModule,
                    viewModelModule,
                    networkModule,
                    databaseModule
                )
            )
        }
    }

    private val repositoriesModule = module {
        single<CryptoRepository> { CryptoRepositoryImpl(cryptoAPI = get()) }
        single<CryptoDBRepository> { CryptoDBRepositoryImpl(db = get()) }
    }

    private val useCasesModule = module {
        factory {
            GetCoinHTMLUseCase(get<CryptoRepository>())
        }
        factory {
            GetCryptoCurrenciesHTMLUseCase(get<CryptoRepository>())
        }
        factory {
            CheckCoinExistUseCase(get<CryptoDBRepository>())
        }
    }

    private val viewModelModule = module {
        viewModel {
            PublicCoinsViewModel(
                getCryptoCurrenciesHTMLUseCase = get(),
                checkCoinExistUseCase = get()
            )
        }
    }

    private val networkModule = module {
        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl(BASE_URL_CRYPTO)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        single<CryptoAPI> {
            get<Retrofit>().create(CryptoAPI::class.java)
        }
    }

    private val databaseModule = module {
        single<CryptoDataBase> {
            Room.databaseBuilder(
                get(),
                CryptoDataBase::class.java,
                DATABASE_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO coins (coinName, coinPrice) VALUES ('Bitcoin', '50000')")
                    db.execSQL("INSERT INTO coins (coinName, coinPrice) VALUES ('Ethereum', '4000')")
                }
            }).build()
        }

        single<DAO> {
            get<CryptoDataBase>().getDAO()
        }
    }

    companion object {
        private const val MAIN_TAG_LOG = "mycryptoapp"
        fun mylog(args: Any) {
            Log.i(MAIN_TAG_LOG, "$args")
        }
    }
}