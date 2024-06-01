package online.tripguru.tripguruapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.helpers.convertLocalToResponse
import online.tripguru.tripguruapp.helpers.convertResponseToLocal
import online.tripguru.tripguruapp.local.dao.LocalDao
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.ApiInterface
import retrofit2.HttpException

class LocalRepository (
    appDatabase: AppDatabase,
    private val api: ApiInterface,
    private val userRepository: UserRepository
) {
    private val localDao: LocalDao = appDatabase.localDao()
    private var localSelected: LiveData<Local?> = MutableLiveData<Local?>()
    var allLocals: LiveData<List<Local>> = localDao.getLocals()

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

    suspend fun refreshAllLocals(): LiveData<List<Local>> {
        if (userRepository.isOnline().value == true) {
            try {
                localDao.deleteAll()
                val response = api.getLocals(userRepository.getUserToken())
                localDao.insertAll(response.map { convertResponseToLocal(it) })
            } catch (e: HttpException) {
                Log.e("LocalRepository", "Error: ${e.message()}")
            }
        }
        return allLocals
    }

    suspend fun insertLocal(local: Local) {
        try {
            val localRequest = convertLocalToResponse(local)
            val localResponse = api.createLocal(userRepository.getUserToken(), localRequest)
            localDao.insertLocal(convertResponseToLocal(localResponse))
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
        }
    }

    suspend fun updateLocal(local: Local) {
        try {
            val localRequest = convertLocalToResponse(local)
            val localResponse = api.updateLocal(userRepository.getUserToken(), local.id!!, localRequest)
            localDao.insertLocal(convertResponseToLocal(localResponse))
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
        }
    }

    suspend fun deleteLocal(local: Local) {
        try {
            api.deleteLocal(userRepository.getUserToken(), local.id!!)
            localDao.deleteLocal(local.id!!)
            updateSelectedLocal(null)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
        }
    }



}