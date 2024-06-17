package online.tripguru.tripguruapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.helpers.convertResponseToTrip
import online.tripguru.tripguruapp.localstorage.dao.TripDao
import online.tripguru.tripguruapp.localstorage.database.AppDatabase
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.TripRequest
import online.tripguru.tripguruapp.network.TripResponse
import retrofit2.HttpException
import javax.inject.Inject

class TripRepository @Inject constructor(
    appDatabase: AppDatabase,
    private val api: ApiInterface,
    private val userRepository: UserRepository
) {
    private val tripDao: TripDao = appDatabase.tripDao()
    private var tripSelected: LiveData<Trip?> = MutableLiveData<Trip?>(null)

    fun getAllTrips(): LiveData<List<Trip>> {
        return tripDao.getTrips()
    }

    suspend fun insertTrip(tripRequest: TripRequest) : Resource<TripResponse> {
        return try {
            val response = api.createTrip(userRepository.getUserToken(), tripRequest)
            tripDao.insertTrip(convertResponseToTrip(response))
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun updateTrip(tripRequest: TripRequest) : Resource<TripResponse> {
        return try {
            val response = api.updateTrip(userRepository.getUserToken(), tripRequest.id!!, tripRequest)
            tripDao.insertTrip(convertResponseToTrip(response))
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun deleteTrip(id: Int) : Resource<TripResponse> {
        return try {
            api.deleteTrip(userRepository.getUserToken(), id)
            tripDao.deleteTrip(id)
            updateSelectedTrip(null)
            Resource.success(null)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun refreshAllTrips(): Resource<List<TripResponse>> {
        return try {
            tripDao.deleteAll()
            val response = api.getTrips(userRepository.getUserToken())
            tripDao.insertAll(response.map { convertResponseToTrip(it) })
            Resource.success(response)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            return Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            return Resource.error(e.toString())
        }
    }

    fun updateSelectedTrip(trip: Trip?) {
        tripSelected.let {
            (it as MutableLiveData).value = trip
        }
    }

    fun getSelectedTrip(): LiveData<Trip?> {
        return tripSelected
    }
}

