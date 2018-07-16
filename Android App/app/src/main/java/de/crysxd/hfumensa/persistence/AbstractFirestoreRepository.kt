package de.crysxd.hfumensa.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import timber.log.Timber

abstract class AbstractFirestoreRepository<T: Any>(private val documentClass: Class<T>) {

    protected val firestore = FirebaseFirestore.getInstance()

    protected fun performQuery(query: Query, fromCache: Boolean = true, result: MutableLiveData<QueryResult<T>> = MutableLiveData()): LiveData<QueryResult<T>> {
        result.postValue(QueryResult(QueryResult.Status.ACTIVE, fromCache))

        query.get(if (fromCache) {
            Source.CACHE
        } else {
            Source.SERVER
        }).addOnCompleteListener {
            if (it.isSuccessful) {
                // If we received an empty reading from cache, load from server
                if (it.result.documents.isEmpty() && it.result.metadata.isFromCache) {
                    performQuery(query, false, result)
                } else {
                    result.postValue(QueryResult(QueryResult.Status.COMPLETED, it.result.metadata.isFromCache, it.result.documents.mapNotNull {
                        it.toObject(documentClass)
                    }, null))
                }
            } else {
                Timber.e(it.exception)
                result.postValue(QueryResult(QueryResult.Status.FAILED, it.result.metadata.isFromCache, emptyList(), it.exception))
            }
        }

        return result
    }
}