package de.crysxd.hfumensa.model

class Canteen() {
    var id: Int = 0
    lateinit var name: String
    lateinit var street: String
    lateinit var zip: String
    lateinit var place: String
    lateinit var menuUrl: String
    lateinit var description: String
    lateinit var openingHours: String
    lateinit var iconUrl: String
    var latitude: Float = 0.0f
    var longitude: Float = 0.0f
}