package online.tripguru.tripguruapp.network.auth

sealed class RequestResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): RequestResult<T>(data)
    class Unauthorized<T>: RequestResult<T>()
    class UnknownError<T>: RequestResult<T>()
    class NoInternet<T>: RequestResult<T>()
}
