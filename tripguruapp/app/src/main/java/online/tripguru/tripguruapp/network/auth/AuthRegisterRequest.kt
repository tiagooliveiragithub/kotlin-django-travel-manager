package online.tripguru.tripguruapp.network.auth

data class AuthRegisterRequest(
    val username: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
)
