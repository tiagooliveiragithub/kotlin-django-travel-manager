package online.tripguru.tripguru.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/users/register/")
    suspend fun signUp(
        @Body request: AuthSignupRequest
    )

    @POST("api/token/")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

}