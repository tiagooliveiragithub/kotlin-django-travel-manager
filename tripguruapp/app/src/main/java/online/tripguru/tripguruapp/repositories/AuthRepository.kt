package online.tripguru.tripguruapp.repositories

import online.tripguru.tripguruapp.network.auth.RequestResult

interface AuthRepository {
    suspend fun signUp(username: String, firstname: String, lastname: String, email: String, password: String): RequestResult<Unit>
    suspend fun signIn(username: String, password: String): RequestResult<Unit>
    suspend fun signOut(): RequestResult<Unit>
}