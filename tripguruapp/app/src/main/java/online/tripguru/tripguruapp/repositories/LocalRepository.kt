package online.tripguru.tripguruapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.local.dao.LocalDao
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.ApiInterface

class LocalRepository (
    appDatabase: AppDatabase,
    private val api: ApiInterface,
    private val userRepository: UserRepository
) {
    private val localDao: LocalDao = appDatabase.localDao()
    private var localSelected: LiveData<Local?> = MutableLiveData<Local?>()
    val allLocals: LiveData<List<Local>> = localDao.getLocals()

    fun updateSelectedLocal(local: Local) {
        localSelected.let {
            (it as MutableLiveData).value = local
        }
    }

    fun getSelectedLocal(): LiveData<Local?> {
        return localSelected
    }

}