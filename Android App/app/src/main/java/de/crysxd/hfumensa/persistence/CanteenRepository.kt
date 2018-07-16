package de.crysxd.hfumensa.persistence

import de.crysxd.hfumensa.model.Canteen

class CanteenRepository: AbstractFirestoreRepository<Canteen>(Canteen::class.java) {

    fun getCanteens(fromCache: Boolean = true) = performQuery(
            query = firestore.collection("canteens"),
            fromCache = fromCache)

}