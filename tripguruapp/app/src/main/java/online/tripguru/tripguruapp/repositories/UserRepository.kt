package online.tripguru.tripguruapp.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.ConnectivityLiveData
import online.tripguru.tripguruapp.network.auth.AuthRegisterRequest
import online.tripguru.tripguruapp.network.auth.AuthRequest
import online.tripguru.tripguruapp.network.auth.RequestResult
import online.tripguru.tripguruapp.network.auth.TokenVerifyRequest
import retrofit2.HttpException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ApiInterface,
    private val prefs: SharedPreferences,
    private val connectivityLiveData: ConnectivityLiveData
) {
    private val isAuthorizedLiveData = MutableLiveData<Boolean>()

    suspend fun signUp(username: String, firstname: String, lastname: String, email: String, password: String): RequestResult<Unit> {
        return try {
            api.signUp(
                request = AuthRegisterRequest(
                    username = username,
                    first_name = firstname,
                    last_name = lastname,
                    email = email,
                    password = password
                )
            )
            signIn(username, password)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                RequestResult.Unauthorized()
            } else {
                RequestResult.UnknownError()
            }
        } catch (e: Exception) {
            RequestResult.UnknownError()
        }
    }

    suspend fun signIn(username: String, password: String): RequestResult<Unit> {
        return try {
            val response = api.signIn(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("access", response.access)
                .apply()
            prefs.edit()
                .putString("refresh", response.refresh)
                .apply()
            isAuthorizedLiveData.postValue(true)
            RequestResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                RequestResult.Unauthorized()
            } else {
                RequestResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signIn: ${e.message}", e)
            RequestResult.UnknownError()
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
            isAuthorizedLiveData.postValue(false)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signOut: ${e.message}", e)
        }

    }

     suspend fun isAuthorizedVerify(): LiveData<Boolean> {
         try {
            val token = prefs.getString("access", "") ?: ""
            api.verifyToken(request = TokenVerifyRequest(token = token))
            isAuthorizedLiveData.postValue(true)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                isAuthorizedLiveData.postValue(false)
            } else {
                isAuthorizedLiveData.postValue(false)
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "isAuthorizedVerify: ${e.message}", e)
            isAuthorizedLiveData.postValue(false)
        }
     return isAuthorizedLiveData
    }

    fun isOnline(): LiveData<Boolean> {
        return connectivityLiveData
    }

    fun isAuthorized(): LiveData<Boolean> {
        return isAuthorizedLiveData
    }

    fun getUserToken(): String {
        val token = prefs.getString("access", "") ?: ""
        if(token.isEmpty()) {
            isAuthorizedLiveData.postValue(false)
        }
        return "Bearer $token"
    }
}