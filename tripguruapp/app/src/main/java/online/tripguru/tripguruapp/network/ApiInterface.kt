package online.tripguru.tripguruapp.network

import online.tripguru.tripguruapp.network.auth.AuthRegisterRequest
import online.tripguru.tripguruapp.network.auth.AuthRequest
import online.tripguru.tripguruapp.network.auth.TokenResponse
import online.tripguru.tripguruapp.network.auth.TokenVerifyRequest
import online.tripguru.tripguruapp.network.trip.TripResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @POST("api/token/")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @POST("api/token/verify/")
    suspend fun verifyToken(
        @Body request: TokenVerifyRequest
    )

    @POST("api/users/register/")
    suspend fun signUp(
        @Body request: AuthRegisterRequest
    )

    @GET("api/travels/")
    suspend fun getTrips(
        @Header("Authorization") token: String
    ): List<TripResponse>

    @GET("api/travels/{pk}")
    suspend fun getTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    ): TripResponse

    @POST("api/travels/")
    suspend fun createTrip(
        @Header("Authorization") token: String,
        @Body trip: TripResponse
    )

    @PUT("api/travels/{pk}/")
    suspend fun updateTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Body trip: TripResponse
    )

    @DELETE("api/travels/{pk}/")
    suspend fun deleteTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    )
}