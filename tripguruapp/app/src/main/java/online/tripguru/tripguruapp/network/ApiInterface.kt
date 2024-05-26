package online.tripguru.tripguruapp.network

import online.tripguru.tripguruapp.network.auth.AuthRegisterRequest
import online.tripguru.tripguruapp.network.auth.AuthRequest
import online.tripguru.tripguruapp.network.auth.TokenResponse
import online.tripguru.tripguruapp.network.trip.TripResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("api/users/register/")
    suspend fun signUp(
        @Body request: AuthRegisterRequest
    )

    @POST("api/token/")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @GET("api/travels/")
    suspend fun getTrips(
        @Header("Authorization") token: String
    ): List<TripResponse>
}