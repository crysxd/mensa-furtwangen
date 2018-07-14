package de.crysxd.hfumensa.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import de.crysxd.hfumensa.model.Dish
import de.crysxd.hfumensa.model.Dish.Companion.dateFormat
import timber.log.Timber
import java.util.*

class MenuRepository {

    fun getMenu(canteenId: String, from: Date): Pair<LiveData<List<Dish>>, LiveData<Exception>> {
        val firestore = FirebaseFirestore.getInstance()
        val liveDataResult = MutableLiveData<List<Dish>>()
        val liveDataError = MutableLiveData<Exception>()

        firestore.collection("canteens").document(canteenId).collection("menu")
                .whereGreaterThan("date", dateFormat.format(from))
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        liveDataResult.postValue(it.result.documents.mapNotNull {
                            it.toObject(Dish::class.java)
                        })
                    } else {
                        Timber.e(it.exception)
                        liveDataError.postValue(it.exception)
                    }
                }

        return Pair(liveDataResult, liveDataError)
    }
}