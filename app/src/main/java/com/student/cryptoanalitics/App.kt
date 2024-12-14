package com.student.cryptoanalitics

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.student.cryptoanalitics.data.CryptoRepositoryImpl
import com.student.cryptoanalitics.data.api.CryptoAPI
import com.student.cryptoanalitics.data.api.CryptoAPI.Companion.BASE_URL_CRYPTO
import com.student.cryptoanalitics.domain.repositories.CryptoRepository
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetCryptoCurrenciesHTMLUseCase
import com.student.cryptoanalitics.presentation.vm.PublicCoinsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
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
            modules(listOf(repositoriesModule, useCasesModule, networkModule, viewModelModule))
        }
    }

    private val repositoriesModule = module {
        singleOf(::CryptoRepositoryImpl) { bind<CryptoRepository>() }
    }

    private val useCasesModule = module {
        factory {
            GetCoinHTMLUseCase(get<CryptoRepository>())
        }
        factory {
            GetCryptoCurrenciesHTMLUseCase(get<CryptoRepository>())
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

    private val viewModelModule = module {
        viewModel { PublicCoinsViewModel(get()) }
    }

    companion object {
        private const val MAIN_TAG_LOG = "mycryptoapp"
        fun mylog(args: Any) {
            Log.i(MAIN_TAG_LOG, "$args")
        }
    }
}