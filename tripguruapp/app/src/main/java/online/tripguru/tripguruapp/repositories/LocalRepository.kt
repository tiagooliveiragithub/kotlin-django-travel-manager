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
            val localResponse = convertLocalToResponse(local)
            api.createLocal(userRepository.getUserToken(), localResponse)
            localDao.insertLocal(local)
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
        }
    }

    suspend fun updateLocal(local: Local) {
        try {
            val localResponse = convertLocalToResponse(local)
            api.updateLocal(userRepository.getUserToken(), local.id!!, localResponse)
            localDao.insertLocal(local)
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
        } catch (e: HttpException) {
            Log.e("LocalRepository", "Error: ${e.message()}")
        } catch (e: Exception) {
            Log.e("LocalRepository", "Unexpected Error: ${e.message}")
        }
    }



}