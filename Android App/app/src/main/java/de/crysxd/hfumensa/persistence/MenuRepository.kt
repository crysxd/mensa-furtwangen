package de.crysxd.hfumensa.persistence

import com.google.firebase.firestore.FirebaseFirestore
import de.crysxd.hfumensa.model.Dish
import de.crysxd.hfumensa.model.Dish.Companion.dateFormat
import java.util.*

class MenuRepository : AbstractFirestoreRepository<Dish>(Dish::class.java) {

    fun getMenu(canteenId: String, from: Date, fromCache: Boolean = true) = performQuery(
            query = firestore.collection("canteens").document(canteenId).collection("menu")
                    .whereGreaterThan("date", dateFormat.format(from)),
            fromCache = fromCache)

}