package org.patifiner.client

import android.app.Application
import org.koin.android.ext.koin.androidContext

class PatifinerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            KoinAppConfig(
                engine = Platform.engineFactory(),
                apiConfig = Platform.apiConfig(),
                appScope = Platform.appMainScope()
            )
        ) { androidContext(this@PatifinerApplication) }
    }

}
