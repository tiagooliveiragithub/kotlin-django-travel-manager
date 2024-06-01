package online.tripguru.tripguruapp.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.ConnectivityLiveData
import online.tripguru.tripguruapp.network.EditUserRequest
import online.tripguru.tripguruapp.network.EditUserResponse
import online.tripguru.tripguruapp.network.GetUserInfoResponse
import online.tripguru.tripguruapp.network.LoginRequest
import online.tripguru.tripguruapp.network.LoginResponse
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.network.SignupRequest
import online.tripguru.tripguruapp.network.SignupResponse
import online.tripguru.tripguruapp.network.TokenVerifyRequest
import retrofit2.HttpException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiInterface,
    private val prefs: SharedPreferences,
    private val connectivityLiveData: ConnectivityLiveData
) {
    val isSignedIn = MutableLiveData<Boolean>()

    suspend fun signIn(loginRequest: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = api.signIn(loginRequest)
            prefs.edit()
                .putString("access", response.access)
                .apply()
            prefs.edit()
                .putString("refresh", response.refresh)
                .apply()
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message(), null)
            } else {
                Resource.error(e.message(), null)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "signIn: ${e.message}", e)
            Resource.error(e.toString(), null)
        }
    }

    suspend fun signUp(signupRequest: SignupRequest): Resource<SignupResponse> {
        return try {
            val response = api.signUp(signupRequest)
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message(), null)
            } else {
                Resource.error(e.message(), null)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "signUp: ${e.message}", e)
            Resource.error(e.toString(), null)
        }
    }

    suspend fun editUser(editUserRequest: EditUserRequest): Resource<EditUserResponse> {
        return try {
            val response = api.editUser(request = editUserRequest, token = getUserToken())
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message(), null)
            } else {
                Resource.error(e.message(), null)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "editUser: ${e.message}", e)
            Resource.error(e.toString(), null)
        }
    }

    suspend fun getUserInfo(): Resource<GetUserInfoResponse> {
        return try {
            val response = api.infoUser(token = getUserToken())
            Resource.success(response)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Resource.error(e.message(), null)
            } else {
                Resource.error(e.message(), null)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserInfo: ${e.message}", e)
            Resource.error(e.toString(), null)
        }
    }

    fun signOut() {
        try {
            prefs.edit()
                .remove("access")
                .apply()
            prefs.edit()
                .remove("refresh")
                .apply()
            isSignedIn.postValue(false)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signOut: ${e.message}", e)
        }
    }

     suspend fun isAuthorizedVerify(): LiveData<Boolean> {
         try {
            val token = prefs.getString("access", "") ?: ""
            api.verifyToken(TokenVerifyRequest(token = token))
            isSignedIn.postValue(true)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                isSignedIn.postValue(false)
            } else {
                isSignedIn.postValue(false)
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "isAuthorizedVerify: ${e.message}", e)
            isSignedIn.postValue(false)
        }
        return isSignedIn
    }

    fun isOnline(): LiveData<Boolean> {
        return connectivityLiveData
    }

    fun getUserToken(): String {
        val token = prefs.getString("access", "") ?: ""
        return "Bearer $token"
    }
}