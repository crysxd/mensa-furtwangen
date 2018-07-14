package de.crysxd.hfumensa.model

import java.text.SimpleDateFormat
import java.util.*

class Dish {
    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }

    lateinit var title: String
    lateinit var date: String
    lateinit var description: String
    var msc = false
    var vegan = false
    var veganOption = false
    var vegetarian = false
    lateinit var priceStudents: String
    lateinit var priceEmployees: String
    lateinit var priceGuests: String
    lateinit var alergens: List<Set>
    lateinit var additives: List<Set>

    class Set {
        lateinit var description: String
        lateinit var key: String
    }
}