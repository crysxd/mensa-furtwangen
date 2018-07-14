package de.crysxd.hfumensa.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import de.crysxd.hfumensa.model.Canteen
import timber.log.Timber

class CanteenRepository {

    fun getCanteens(): Pair<LiveData<List<Canteen>>, LiveData<Exception>> {
        val firestore = FirebaseFirestore.getInstance()
        val liveDataResult = MutableLiveData<List<Canteen>>()
        val liveDataError = MutableLiveData<Exception>()

        firestore.collection("canteens").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        liveDataResult.postValue(it.result.documents.mapNotNull {
                            it.toObject(Canteen::class.java)
                        })
                    } else {
                        Timber.e(it.exception)
                        liveDataError.postValue(it.exception)
                    }
                }

        return Pair(liveDataResult, liveDataError)
    }
}