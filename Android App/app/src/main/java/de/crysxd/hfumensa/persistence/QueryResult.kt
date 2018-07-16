package de.crysxd.hfumensa.persistence

class QueryResult<T>(val status: Status, val fromCache: Boolean, val result: List<T> = emptyList(), val exception: Exception? = null) {
    enum class Status {
        COMPLETED,
        FAILED,
        ACTIVE
    }
}
