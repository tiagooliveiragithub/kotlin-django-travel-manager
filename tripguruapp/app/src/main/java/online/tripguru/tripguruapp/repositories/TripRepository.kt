package online.tripguru.tripguruapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.helpers.convertResponseToTrip
import online.tripguru.tripguruapp.localstorage.dao.TripDao
import online.tripguru.tripguruapp.localstorage.database.AppDatabase
import online.tripguru.tripguruapp.localstorage.models.Trip
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

    fun getAllOfflineTrips(): LiveData<List<Trip>> {
        return tripDao.getTrips()
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

    suspend fun insertTrip(tripRequest: TripRequest) : Resource<Trip> {
        return try {
            val response = api.createTrip(userRepository.getUserToken(), tripRequest)
            val trip = convertResponseToTrip(response)
            tripDao.insertTrip(trip)
            Resource.success(trip)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun updateTrip(tripRequest: TripRequest) : Resource<Trip> {
        return try {
            val response = api.updateTrip(userRepository.getUserToken(), tripRequest.id!!, tripRequest)
            val trip = convertResponseToTrip(response)
            tripDao.insertTrip(trip)
            Resource.success(trip)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun deleteTrip(id: Int) : Resource<Trip> {
        return try {
            api.deleteTrip(userRepository.getUserToken(), id)
            tripDao.deleteTrip(id)
            Resource.success(null)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun shareTrip(id: Int, username: String) : Resource<Trip> {
        return try {
            val response = api.shareTrip(userRepository.getUserToken(), id, username)
            val trip = convertResponseToTrip(response)
            tripDao.insertTrip(trip)
            Resource.success(trip)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun removeShareTrip(id: Int, username: String) : Resource<Trip> {
        return try {
            val response = api.removeShareTrip(userRepository.getUserToken(), id, username)
            val trip = convertResponseToTrip(response)
            tripDao.insertTrip(trip)
            Resource.success(trip)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.toString())
        }
    }

    suspend fun uploadImage(tripId : Int, imageUriPart: MultipartBody.Part): Resource<String> {
        return try {
            val response = api.uploadTripImage(userRepository.getUserToken(), tripId, imageUriPart)
            Log.d("TripRepository", "Image uploaded: $response")
            tripDao.insertImage(tripId, response.image)
            Resource.success(response.image)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
            Resource.error(e.message())
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
            Resource.error(e.message ?: "Unexpected Error")
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

