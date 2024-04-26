package dev.tiagooliveira.tripguru.auth

data class TokenResponse(
    val refresh: String,
    val access: String
)
