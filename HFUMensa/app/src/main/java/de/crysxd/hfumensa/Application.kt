package de.crysxd.hfumensa

import android.app.Application
import com.google.firebase.FirebaseApp
import timber.log.Timber

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Timber.plant(Timber.DebugTree())
    }
}