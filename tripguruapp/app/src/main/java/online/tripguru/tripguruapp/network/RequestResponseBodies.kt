package online.tripguru.tripguruapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class LoginRequest(
    val username: String,
    val password: String
)
data class LoginResponse(
    val refresh: String,
    val access: String,
    val first_name: String,
    val last_name: String
)

data class TokenVerifyRequest (
    val token: String,
)

data class SignupFormRequest(
    val username: RequestBody,
    val first_name: RequestBody,
    val last_name: RequestBody,
    val email: RequestBody,
    val password: RequestBody,
    val avatar: MultipartBody.Part
)
data class SignupResponse(
    val username : String,
    val first_name: String,
    val last_name: String,
    val email: String
)

data class EditUserRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
)
data class EditUserResponse(
    val first_name: String,
    val last_name: String,
    val email: String,
)

data class GetUserInfoResponse(
    val first_name: String,
    val last_name: String,
    val email: String,
    val last_accessed: String,
    val avatar: String
)

data class TripRequest(
    val id: Int?,
    val name: String,
    val description: String,
)

data class TripResponse(
    val id: Int,
    val name: String,
    val description: String,
)

data class LocalRequest(
    val id: Int?,
    val tripId: Int?,
    val name: String,
    val description: String,
)

data class LocalResponse(
    val id: Int,
    val tripId: Int,
    val name: String,
    val description: String,
)

data class LocalImageFormRequest(
    val spot_pk: Int,
    val avatar: MultipartBody.Part
)

data class LocalImageResponse(
    val id: Int,
    val image: String,
    val created_at: String,
    val spot: Int
)



