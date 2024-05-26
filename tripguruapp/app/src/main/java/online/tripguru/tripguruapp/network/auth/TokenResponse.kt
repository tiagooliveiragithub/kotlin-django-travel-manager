package online.tripguru.tripguruapp.network.auth

data class TokenResponse(
    val refresh: String,
    val access: String
)
