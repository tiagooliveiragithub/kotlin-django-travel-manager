package online.tripguru.tripguruapp.network

import online.tripguru.tripguruapp.network.auth.AuthRegisterRequest
import online.tripguru.tripguruapp.network.auth.AuthRequest
import online.tripguru.tripguruapp.network.auth.TokenResponse
import online.tripguru.tripguruapp.network.auth.TokenVerifyRequest
import online.tripguru.tripguruapp.network.trip.LocalResponse
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

    @GET("api/trips/")
    suspend fun getTrips(
        @Header("Authorization") token: String
    ): List<TripResponse>

    @GET("api/trips/{pk}")
    suspend fun getTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    ): TripResponse

    @POST("api/trips/")
    suspend fun createTrip(
        @Header("Authorization") token: String,
        @Body trip: TripResponse
    ) : TripResponse

    @PUT("api/trips/{pk}/")
    suspend fun updateTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Body trip: TripResponse
    ) : TripResponse

    @DELETE("api/trips/{pk}/")
    suspend fun deleteTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    )

    @GET("api/spots/")
    suspend fun getLocals(
        @Header("Authorization") token: String
    ): List<LocalResponse>

    @GET("api/spots/{pk}")
    suspend fun getLocal(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    ): LocalResponse

    @POST("api/spots/")
    suspend fun createLocal(
        @Header("Authorization") token: String,
        @Body local: LocalResponse
    ) : LocalResponse

    @PUT("api/spots/{pk}/")
    suspend fun updateLocal(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Body local: LocalResponse
    ) : LocalResponse

    @DELETE("api/spots/{pk}/")
    suspend fun deleteLocal(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    )
}