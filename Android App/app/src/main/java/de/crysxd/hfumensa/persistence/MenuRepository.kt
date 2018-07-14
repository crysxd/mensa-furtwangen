package de.crysxd.hfumensa.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import de.crysxd.hfumensa.model.Dish
import de.crysxd.hfumensa.model.Dish.Companion.dateFormat
import timber.log.Timber
import java.util.*

class MenuRepository {

    fun getMenu(canteenId: String, from: Date, fromCache: Boolean = true, liveDataResult: MutableLiveData<List<Dish>> = MutableLiveData(), liveDataError: MutableLiveData<Exception> = MutableLiveData()): Pair<LiveData<List<Dish>>, LiveData<Exception>> {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("canteens").document(canteenId).collection("menu")
                .whereGreaterThan("date", dateFormat.format(from))
                .get(if (fromCache) { Source.CACHE } else { Source.SERVER })
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // If we received an empty reading from cache, load from server
                        if (it.result.documents.isEmpty() && it.result.metadata.isFromCache) {
                            getMenu(canteenId, from, false, liveDataResult, liveDataError)
                        } else {
                            liveDataResult.postValue(it.result.documents.mapNotNull {
                                it.toObject(Dish::class.java)
                            })
                        }
                    } else {
                        Timber.e(it.exception)
                        liveDataError.postValue(it.exception)
                    }
                }

        return Pair(liveDataResult, liveDataError)
    }
}