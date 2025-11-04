package org.patifiner.client

import android.app.Application

class PatifinerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Platform.initNapier()
    }

}