package org.patifiner.client

import android.app.Application
import android.os.StrictMode
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.patifiner.client.base.PtfLog
import org.patifiner.client.login.LoginUseCase
import org.patifiner.client.login.data.AuthRepository
import kotlin.system.measureTimeMillis

class PatifinerApplication : Application() {

    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready

    init {
        if (BuildConfig.IS_DEV) {
            setupStrictMode()
        }
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            val totalTime = measureTimeMillis {
                initKoin(
                    KoinAppConfig(
                        engine = Platform.engineFactory(),
                        apiConfig = Platform.apiConfig(),
                        appScope = Platform.appMainScope()
                    )
                ) { androidContext(this@PatifinerApplication) }

                // warm up complex instances in bg
                getKoin().get<HttpClient>()
                getKoin().get<AuthRepository>()
                getKoin().get<LoginUseCase>()
            }
            PtfLog.i { "Koin init took $totalTime ms" }
            _ready.value = true
        }
    }

    private fun setupStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().build())
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )
    }

}
