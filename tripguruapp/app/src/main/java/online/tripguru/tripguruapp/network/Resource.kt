package online.tripguru.tripguruapp.network

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val fields: Int?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String): Resource<T> {
            return Resource(Status.ERROR,null, message, null)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null, null)
        }

        fun <T> fields (fields: Int): Resource<T> {
            return Resource(Status.FIELDS, null, null, fields)
        }
    }
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        FIELDS
    }
}

