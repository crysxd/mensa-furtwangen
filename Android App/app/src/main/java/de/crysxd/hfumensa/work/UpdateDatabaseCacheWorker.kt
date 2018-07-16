package de.crysxd.hfumensa.work

import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.Worker
import de.crysxd.hfumensa.SELECTED_MENSA_SETTING
import de.crysxd.hfumensa.persistence.CanteenRepository
import de.crysxd.hfumensa.persistence.MenuRepository
import de.crysxd.hfumensa.persistence.QueryResult
import timber.log.Timber
import java.util.*
import java.util.concurrent.CountDownLatch

class UpdateDatabaseCacheWorker : Worker() {

    val handler = Handler(Looper.getMainLooper())

    override fun doWork(): Result {
        Timber.d("Updating database cache")

        val queries = mutableListOf<LiveData<QueryResult<*>>>()

        // Update canteen idndex
        queries.add(CanteenRepository().getCanteens(false) as LiveData<QueryResult<*>>)

        // Update menu for selected canteen
        val selectedCanteenId = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(SELECTED_MENSA_SETTING, "")
        if (!selectedCanteenId.isEmpty()) {
            queries.add(MenuRepository().getMenu(selectedCanteenId, Date(), false) as LiveData<QueryResult<*>>)
        }

        // Await all queries
        val countDownLatch = CountDownLatch(queries.size)
        queries.forEach {
            awaitQuery(it, countDownLatch)
        }
        countDownLatch.await()

        return if (queries.count { it.value?.status == QueryResult.Status.FAILED } > 0) {
            Timber.d("One or more updates failed, retrying later")
            Result.RETRY
        } else {
            Timber.d("Database updates successful")
            Result.SUCCESS
        }
    }

    private fun awaitQuery(query: LiveData<QueryResult<*>>, countDownLatch: CountDownLatch) {
        var observer: Observer<QueryResult<*>>? = null
        observer = Observer {
            if (arrayOf(QueryResult.Status.FAILED, QueryResult.Status.COMPLETED).contains(it.status)) {
                countDownLatch.countDown()
                query.removeObserver(observer!!)
            }
        }

        handler.post {
            query.observeForever(observer)
        }
    }
}