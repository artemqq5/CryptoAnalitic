package com.student.cryptoanalitics


import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import com.student.cryptoanalitics.data.CryptoDBRepositoryImpl
import com.student.cryptoanalitics.data.CryptoHtmlHtmlRepositoryImpl
import com.student.cryptoanalitics.data.api.CryptoAPI
import com.student.cryptoanalitics.data.api.CryptoAPI.Companion.BASE_URL_CRYPTO
import com.student.cryptoanalitics.data.database.CryptoDataBase
import com.student.cryptoanalitics.data.database.CryptoDataBase.Companion.DATABASE_NAME
import com.student.cryptoanalitics.data.database.DAO
import com.student.cryptoanalitics.domain.repositories.CryptoDBRepository
import com.student.cryptoanalitics.domain.repositories.CryptoHtmlRepository
import com.student.cryptoanalitics.domain.usecases.AddCoinUseCase
import com.student.cryptoanalitics.domain.usecases.CheckCoinExistUseCase
import com.student.cryptoanalitics.domain.usecases.DeleteCoinUseCase
import com.student.cryptoanalitics.domain.usecases.GeminiUseCase
import com.student.cryptoanalitics.domain.usecases.GetCoinHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetCryptoCurrenciesHTMLUseCase
import com.student.cryptoanalitics.domain.usecases.GetPagedCoinsUseCase
import com.student.cryptoanalitics.domain.usecases.UpdateCoinsUseCase
import com.student.cryptoanalitics.presentation.vm.DeleteCoinsViewModel
import com.student.cryptoanalitics.presentation.vm.GeminiViewModel
import com.student.cryptoanalitics.presentation.vm.InsertCoinsViewModel
import com.student.cryptoanalitics.presentation.vm.LoadCryptoCoinsViewModel
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
                    databaseModule,
                    artificialIntelligenceModule
                )
            )
        }
    }

    private val repositoriesModule = module {
        single<CryptoHtmlRepository> { CryptoHtmlHtmlRepositoryImpl(cryptoAPI = get()) }
        single<CryptoDBRepository> { CryptoDBRepositoryImpl(db = get()) }
    }

    private val useCasesModule = module {
        factory {
            GetCoinHTMLUseCase(get<CryptoHtmlRepository>())
        }
        factory {
            GetCryptoCurrenciesHTMLUseCase(get<CryptoHtmlRepository>())
        }
        factory {
            CheckCoinExistUseCase(get<CryptoDBRepository>())
        }
        factory {
            AddCoinUseCase(get<CryptoDBRepository>())
        }
        factory {
            GetPagedCoinsUseCase(get<CryptoDBRepository>())
        }
        factory {
            UpdateCoinsUseCase(get<CryptoDBRepository>())
        }
        factory {
            DeleteCoinUseCase(get<CryptoDBRepository>())
        }
        factory {
            GeminiUseCase(get<GenerativeModel>())
        }
    }

    private val viewModelModule = module {
        viewModel {
            PublicCoinsViewModel(
                getCryptoCurrenciesHTMLUseCase = get(),
                checkCoinExistUseCase = get()
            )
        }

        viewModel {
            InsertCoinsViewModel(
                addCoinUseCase = get()
            )
        }
        viewModel {
            LoadCryptoCoinsViewModel(
                getPagedCoinsUseCase = get(),
                getCoinHTMLUseCase = get(),
                updateCoinsUseCase = get()
            )
        }
        viewModel {
            DeleteCoinsViewModel(
                deleteCoinUseCase = get()
            )
        }
        viewModel {
            GeminiViewModel(
                geminiUseCase = get()
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
                    db.execSQL("INSERT INTO coins (coinName, coinPrice) VALUES ('Bitcoin', '100000')")
                    db.execSQL("INSERT INTO coins (coinName, coinPrice) VALUES ('Ethereum', '4000')")
                }
            }).build()
        }

        single<DAO> {
            get<CryptoDataBase>().getDAO()
        }
    }

    private val artificialIntelligenceModule = module {
        single<GenerativeModel> {
            GenerativeModel(
                modelName = "gemini-1.5-flash-001",
                apiKey = BuildConfig.GEMINI_API_KEY,
                generationConfig = generationConfig {
                    temperature = 0.15f
                    topK = 32
                    topP = 1f
                    maxOutputTokens = 4096
                },
                safetySettings = listOf(
                    SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
                )
            )
        }
    }

    companion object {
        private const val MAIN_TAG_LOG = "mycryptoapp"
        fun mylog(args: Any) {
            Log.i(MAIN_TAG_LOG, "$args")
        }
    }
}