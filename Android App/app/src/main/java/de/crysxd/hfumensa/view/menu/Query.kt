package de.crysxd.hfumensa.view.menu

class Query<T>(val status: Status, val fromCache: Boolean, val result: List<T> = emptyList(), val exception: Exception? = null) {
    enum class Status {
        COMPLETED,
        FAILED,
        ACTIVE
    }
}
