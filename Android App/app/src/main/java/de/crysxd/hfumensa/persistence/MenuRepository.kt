package de.crysxd.hfumensa.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import de.crysxd.hfumensa.model.Dish
import de.crysxd.hfumensa.model.Dish.Companion.dateFormat
import de.crysxd.hfumensa.view.menu.Query
import timber.log.Timber
import java.util.*

class MenuRepository {

    fun getMenu(canteenId: String, from: Date, fromCache: Boolean = true, liveDataQuery: MutableLiveData<Query<Dish>> = MutableLiveData()): LiveData<Query<Dish>> {
        val firestore = FirebaseFirestore.getInstance()

        liveDataQuery.postValue(Query(Query.Status.ACTIVE, fromCache))

        firestore.collection("canteens").document(canteenId).collection("menu")
                .whereGreaterThan("date", dateFormat.format(from))
                .get(if (fromCache) {
                    Source.CACHE
                } else {
                    Source.SERVER
                })
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // If we received an empty reading from cache, load from server
                        if (it.result.documents.isEmpty() && it.result.metadata.isFromCache) {
                            getMenu(canteenId, from, false, liveDataQuery)
                        } else {
                            liveDataQuery.postValue(Query(Query.Status.COMPLETED, it.result.metadata.isFromCache, it.result.documents.mapNotNull {
                                it.toObject(Dish::class.java)
                            }, null))
                        }
                    } else {
                        Timber.e(it.exception)
                        liveDataQuery.postValue(Query(Query.Status.FAILED, it.result.metadata.isFromCache, emptyList(), it.exception))
                    }
                }

        return liveDataQuery
    }
}