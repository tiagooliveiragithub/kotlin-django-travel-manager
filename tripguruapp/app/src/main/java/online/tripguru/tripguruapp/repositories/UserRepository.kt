package online.tripguru.tripguruapp.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.ConnectivityLiveData
import online.tripguru.tripguruapp.network.EditUserRequest
import online.tripguru.tripguruapp.network.EditUserResponse
import online.tripguru.tripguruapp.network.GetUserInfoResponse
import online.tripguru.tripguruapp.network.LoginRequest
import online.tripguru.tripguruapp.network.LoginResponse
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.network.SignupFormRequest
import online.tripguru.tripguruapp.network.SignupResponse
import online.tripguru.tripguruapp.network.TokenVerifyRequest
import retrofit2.HttpException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiInterface,
    private val prefs: SharedPreferences,
    private val connectivityLiveData: ConnectivityLiveData,
) {
    suspend fun signIn(loginRequest: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = api.signIn(loginRequest)
            prefs.edit()
                .putString("access", response.access)
                .apply()
            prefs.edit()
                .putString("refresh", response.refresh)
                .apply()
            prefs.edit()
                .putString("first_name", response.first_name)
                .apply()
            prefs.edit()
                .putString("last_name", response.last_name)
                .apply()
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message())
            } else {
                Resource.error(e.message())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "signIn: ${e.message}", e)
            Resource.error(e.toString())
        }
    }

    suspend fun signUp(signupFormRequest: SignupFormRequest): Resource<SignupResponse> {
        return try {
            val response = api.signUp(
                signupFormRequest.username,
                signupFormRequest.first_name,
                signupFormRequest.last_name,
                signupFormRequest.email,
                signupFormRequest.password,
                signupFormRequest.avatar)
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message())
            } else {
                Resource.error(e.message())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "signUp: ${e.message}", e)
            Resource.error(e.toString())
        }
    }

    suspend fun editUser(editUserRequest: EditUserRequest): Resource<EditUserResponse> {
        return try {
            val response = api.editUser(request = editUserRequest, token = getUserToken())
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message())
            } else {
                Resource.error(e.message())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "editUser: ${e.message}", e)
            Resource.error(e.toString())
        }
    }

    suspend fun getUserInfo(): Resource<GetUserInfoResponse> {
        return try {
            val response = api.infoUser(token = getUserToken())
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message())
            } else {
                Resource.error(e.message())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserInfo: ${e.message}", e)
            Resource.error(e.toString())
        }
    }

    fun signOut() : Resource<Unit> {
        return try {
            prefs.edit()
                .remove("access")
                .apply()
            prefs.edit()
                .remove("refresh")
                .apply()
            return Resource.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signOut: ${e.message}", e)
            Resource.error(e.toString())
        }
    }

     suspend fun isAuthorizedVerify() : Resource<LoginResponse> {
         return try {
             val token = prefs.getString("access", "") ?: ""
             api.verifyToken(TokenVerifyRequest(token = token))
             Resource.success(null)
        } catch(e: HttpException) {
             if(e.code() == 401) {
                 Log.e("AuthRepositoryImpl", "isAuthorizedVerify: Unauthorized")
                 Resource.error(e.message())
             } else {
                 Log.e("AuthRepositoryImpl", "isAuthorizedVerify: ${e.message}", e)
                 Resource.error(e.message())
             }
        } catch (e: Exception) {
             Log.e("AuthRepositoryImpl", "isAuthorizedVerify: ${e.message}", e)
             Resource.error(e.toString())
        }
    }

    fun isOnline(): LiveData<Boolean> {
        return connectivityLiveData
    }

    fun getUserToken(): String {
        return try {
            val token = prefs.getString("access", "") ?: ""
            "Bearer $token"
        }catch (e: Exception) {
            Log.e("UserRepository", "getUserToken: ${e.message}", e)
            ""
        }
    }

    fun getUserOfflineDetails(): String {
        return try {
            val firstname = prefs.getString("first_name", "") ?: ""
            val lastname = prefs.getString("last_name", "") ?: ""
            "$firstname $lastname"
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserLocalDetails: ${e.message}", e)
            ""
        }
    }
}