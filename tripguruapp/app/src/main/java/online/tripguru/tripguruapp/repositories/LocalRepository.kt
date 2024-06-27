package online.tripguru.tripguruapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.helpers.convertResponseToLocal
import online.tripguru.tripguruapp.localstorage.dao.LocalDao
import online.tripguru.tripguruapp.localstorage.database.AppDatabase
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.ImageResponse
import online.tripguru.tripguruapp.network.LocalRequest
import online.tripguru.tripguruapp.network.LocalResponse
import retrofit2.HttpException

class LocalRepository (
    appDatabase: AppDatabase,
    private val api: ApiInterface,
    private val userRepository: UserRepository
) {
    private val localDao: LocalDao = appDatabase.localDao()
    private var localSelected: LiveData<Local?> = MutableLiveData<Local?>(null)
    val localsForTrip = MutableLiveData<List<Local>>()

    fun getAllOfflineLocals(): LiveData<List<Local>> {
        return localDao.getLocals()
    }

    fun getAllOfflineLocalsForTrip(tripId: Int): LiveData<List<Local>> {
        return localDao.getLocalsForTrip(tripId)
    }

    suspend fun getAllLocalsForTrip(tripId: Int) : Resource<List<Local>> {
        return try {
            val response = api.getLocalsForTrip(userRepository.getUserToken(), tripId)
            val locals = response.map { convertResponseToLocal(it) }
            localsForTrip.postValue(locals)
            Resource.success(locals)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun refreshAllLocals(): Resource<List<LocalResponse>> {
        return try {
            val response = api.getUserLocals(userRepository.getUserToken())
            localDao.insertAll(response.map { convertResponseToLocal(it) })
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun getAllLocals(): Resource<List<Local>> {
        return try {
            val response = api.getLocals(userRepository.getUserToken())
            val locals = response.map { convertResponseToLocal(it) }
            Resource.success(locals)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun insertLocal(localRequest: LocalRequest) : Resource<Local> {
        return try {
            val response = api.createLocal(userRepository.getUserToken(), localRequest)
            val local = convertResponseToLocal(response)
            localDao.insertLocal(local)
            Resource.success(local)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun updateLocal(localRequest: LocalRequest) : Resource<Local> {
        return try {
            val response = api.updateLocal(userRepository.getUserToken(), localRequest.id!!, localRequest)
            val local = convertResponseToLocal(response)
            localDao.insertLocal(local)
            Resource.success(local)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun deleteLocal(id: Int) : Resource<Local> {
        return try {
            api.deleteLocal(userRepository.getUserToken(), id)
            localDao.deleteLocal(id)
            Resource.success(null)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun getLocalImages(localId: Int): Resource<List<ImageResponse>> {
        return try {
            val response = api.getLocalImages(userRepository.getUserToken(), localId)
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    suspend fun uploadImage(localId: Int, imageUriPart: MultipartBody.Part): Resource<ImageResponse> {
        return try {
            val response = api.uploadLocalImage(userRepository.getUserToken(), localId, imageUriPart)
            val local = localDao.getLocalById(localId)
            localDao.insertImage(local.id!!, local.images!!.plus(response.image))
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
        }
    }

    fun updateSelectedLocal(local: Local?) {
        localSelected.let {
            (it as MutableLiveData).value = local
        }
    }

    fun getSelectedLocal(): LiveData<Local?> {
        return localSelected
    }



}