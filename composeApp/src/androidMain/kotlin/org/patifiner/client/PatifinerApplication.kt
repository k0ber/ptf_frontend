package org.patifiner.client

import android.app.Application

class PatifinerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Platform.initNapier()

        initKoin(
            KoinAppConfig(
                engine = Platform.engineFactory(),
                apiConfig = Platform.apiConfig(),
                appScope = Platform.appMainScope()
            )
        ) {
            Platform.setup(context = this@PatifinerApplication)
        }
    }

}
