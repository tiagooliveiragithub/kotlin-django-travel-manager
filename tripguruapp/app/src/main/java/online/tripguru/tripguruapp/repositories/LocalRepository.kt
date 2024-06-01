package online.tripguru.tripguruapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.helpers.convertResponseToLocal
import online.tripguru.tripguruapp.local.dao.LocalDao
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.LocalRequest
import online.tripguru.tripguruapp.network.LocalResponse
import online.tripguru.tripguruapp.network.Resource
import retrofit2.HttpException

class LocalRepository (
    appDatabase: AppDatabase,
    private val api: ApiInterface,
    private val userRepository: UserRepository
) {
    private val localDao: LocalDao = appDatabase.localDao()
    private var localSelected: LiveData<Local?> = MutableLiveData<Local?>()

    fun getAllLocals(): LiveData<List<Local>> {
        return localDao.getLocals()
    }

    suspend fun insertLocal(localRequest: LocalRequest) : Resource<LocalResponse> {
        return try {
            val response = api.createLocal(userRepository.getUserToken(), localRequest)
            localDao.insertLocal(convertResponseToLocal(response))
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message(), null)
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error", null)
        }
    }

    suspend fun updateLocal(localRequest: LocalRequest) : Resource<LocalResponse> {
        return try {
            val response = api.updateLocal(userRepository.getUserToken(), localRequest.id!!, localRequest)
            localDao.insertLocal(convertResponseToLocal(response))
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message(), null)
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error", null)
        }
    }

    suspend fun deleteLocal(id: Int) : Resource<LocalResponse> {
        return try {
            api.deleteLocal(userRepository.getUserToken(), id)
            localDao.deleteLocal(id)
            updateSelectedLocal(null)
            Resource.success(null)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message(), null)
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error", null)
        }
    }

    suspend fun refreshAllLocals(): Resource<Boolean> {
        return try {
            localDao.deleteAll()
            val response = api.getLocals(userRepository.getUserToken())
            localDao.insertAll(response.map { convertResponseToLocal(it) })
            Resource.success(true)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
            Resource.error(e.message(), false)
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error", false)
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

    fun getLocalsForTrip(tripId: Int): LiveData<List<Local>> {
        return localDao.getLocalsForTrip(tripId)
    }

}