package online.tripguru.tripguruapp.network

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

data class SignupRequest(
    val username: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String
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
    val email: String
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



