package dev.tiagooliveira.tripguru.auth

interface AuthRepository {
    suspend fun signUp(username: String, email: String, password: String): AuthResult<Unit>
    suspend fun signIn(username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}