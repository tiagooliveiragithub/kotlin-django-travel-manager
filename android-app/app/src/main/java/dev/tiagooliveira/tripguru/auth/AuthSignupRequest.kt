package dev.tiagooliveira.tripguru.auth;

data class AuthSignupRequest (
    val username: String,
    val email: String,
    val password: String,
)
