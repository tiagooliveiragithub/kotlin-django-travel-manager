package online.tripguru.tripguruapp.network

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
        @Body request: LoginRequest
    ): LoginResponse

    @POST("api/token/verify/")
    suspend fun verifyToken(
        @Body request: TokenVerifyRequest
    )

    @POST("api/users/register/")
    suspend fun signUp(
        @Body request: SignupRequest
    ) : SignupResponse

    @PUT("api/users/edit/")
    suspend fun editUser(
        @Header("Authorization") token: String,
        @Body request: EditUserRequest
    ) : EditUserResponse

    @GET("api/users/info/")
    suspend fun infoUser(
        @Header("Authorization") token: String,
    ) : GetUserInfoResponse

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
        @Body trip: TripRequest
    ) : TripResponse

    @PUT("api/trips/{pk}/")
    suspend fun updateTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Body trip: TripRequest
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
        @Body local: LocalRequest
    ) : LocalResponse

    @PUT("api/spots/{pk}/")
    suspend fun updateLocal(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Body local: LocalRequest
    ) : LocalResponse

    @DELETE("api/spots/{pk}/")
    suspend fun deleteLocal(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    )
}