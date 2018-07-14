package de.crysxd.hfumensa.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.crysxd.hfumensa.model.Canteen
import timber.log.Timber

class CanteenRepository {

    fun getCanteens(): Pair<LiveData<List<Canteen>>, LiveData<Exception>> {
        val database = FirebaseDatabase.getInstance()
        val liveDataResult = MutableLiveData<List<Canteen>>()
        val liveDataError = MutableLiveData<Exception>()
        database.reference.child("canteens").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                error.toException().let {
                    Timber.e(it)
                    liveDataError.postValue(it)
                }
            }

            override fun onDataChange(data: DataSnapshot) {
                liveDataResult.postValue(data.children.map {
                    it.getValue(Canteen::class.java)
                }.filter {
                    it != null
                }.map {
                    it!!
                }.toList())
            }
        })
        return Pair(liveDataResult, liveDataError)
    }
}