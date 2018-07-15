package de.crysxd.hfumensa.model

data class Canteen(var id: String = "",
    var name: String = "",
    var street: String = "",
    var zip: String = "",
    var place: String = "",
    var menuUrl: String = "",
    var description: String = "",
    var openingHours: String = "",
    var iconUrl: String = "",
    var latitude: Float = 0.0f,
    var longitude: Float = 0.0f)
