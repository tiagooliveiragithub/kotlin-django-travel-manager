package online.tripguru.tripguruapp.repositories

import android.content.SharedPreferences
import android.util.Log
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.auth.AuthRegisterRequest
import online.tripguru.tripguruapp.network.auth.AuthRequest
import online.tripguru.tripguruapp.network.auth.RequestResult
import retrofit2.HttpException
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl(
    private val api: ApiInterface,
    private val prefs: SharedPreferences
): AuthRepository {

    override suspend fun signUp(username: String, firstname: String, lastname: String, email: String, password: String): RequestResult<Unit> {
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

    override suspend fun signIn(username: String, password: String): RequestResult<Unit> {
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



    override suspend fun signOut(): RequestResult<Unit> {
        return try {
            prefs.edit()
                .remove("access")
                .apply()
            prefs.edit()
                .remove("refresh")
                .apply()
            RequestResult.Authorized()
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
}