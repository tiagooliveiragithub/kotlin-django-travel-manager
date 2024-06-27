package online.tripguru.tripguruapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @Multipart
    @POST("api/users/register/")
    suspend fun signUp(
        @Part("username") username: RequestBody,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part avatar: MultipartBody.Part
    ): SignupResponse

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

    @POST("api/trips/{pk}/add_user/{username}/")
    suspend fun shareTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Path("username") username: String
    ) : TripResponse

    @POST("api/trips/{pk}/remove_user/{username}/")
    suspend fun removeShareTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int,
        @Path("username") username: String
    ) : TripResponse

    @Multipart
    @POST("api/trips/{pk}/photo/")
    suspend fun uploadTripImage(
        @Header("Authorization") token: String,
        @Path("pk") tripId: Int,
        @Part image: MultipartBody.Part
    ) : ImageResponse

    @GET("api/spots/")
    suspend fun getUserLocals(
        @Header("Authorization") token: String
    ): List<LocalResponse>

    @GET("api/spots/all/")
    suspend fun getLocals(
        @Header("Authorization") token: String
    ): List<LocalResponse>

    @GET("api/trips/{pk}/locals/")
    suspend fun getLocalsForTrip(
        @Header("Authorization") token: String,
        @Path("pk") id: Int
    ): List<LocalResponse>

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

    @GET("api/spots/{spot_pk}/photos/")
    suspend fun getLocalImages(
        @Header("Authorization") token: String,
        @Path("spot_pk") spotId: Int
    ): List<ImageResponse>

    @Multipart
    @POST("api/spots/{spot_pk}/photos/")
    suspend fun uploadLocalImage(
        @Header("Authorization") token: String,
        @Path("spot_pk") localId: Int,
        @Part image: MultipartBody.Part
    ): ImageResponse
}