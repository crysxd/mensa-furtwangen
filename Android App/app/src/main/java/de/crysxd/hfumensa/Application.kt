package de.crysxd.hfumensa

import android.app.Application
import androidx.work.*
import com.google.firebase.FirebaseApp
import de.crysxd.hfumensa.work.UpdateDatabaseCacheWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup Firebase
        FirebaseApp.initializeApp(this)

        // Setup logging
        Timber.plant(Timber.DebugTree())

        // Schedule database update
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build()
        val workRequest = PeriodicWorkRequest.Builder(UpdateDatabaseCacheWorker::class.java, 24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance()?.enqueueUniquePeriodicWork("database-update", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}