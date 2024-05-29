package online.tripguru.tripguruapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import online.tripguru.tripguruapp.helpers.convertResponseToTrip
import online.tripguru.tripguruapp.helpers.convertTripToResponse
import online.tripguru.tripguruapp.local.dao.TripDao
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.ApiInterface
import retrofit2.HttpException
import javax.inject.Inject

class TripRepository @Inject constructor(
    appDatabase: AppDatabase,
    private val api: ApiInterface,
    private val userRepository: UserRepository
) {
    private val tripDao: TripDao = appDatabase.tripDao()
    private var tripSelected: LiveData<Trip?> = MutableLiveData<Trip?>()
    val allTrips: LiveData<List<Trip>> = tripDao.getTrips()


    fun updateSelectedTrip(trip: Trip?) {
        tripSelected.let {
            (it as MutableLiveData).value = trip
        }
    }

    fun getSelectedTrip(): LiveData<Trip?> {
        return tripSelected
    }

    suspend fun refreshAllTrips(): LiveData<List<Trip>> {
        if (userRepository.isOnline().value == true) {
            try {
                tripDao.deleteAll()
                val response = api.getTrips(userRepository.getUserToken())
                tripDao.insertAll(response.map { convertResponseToTrip(it) })
            } catch (e: HttpException) {
                Log.e("TripRepository", "Error: ${e.message()}")
            }
        }
        return allTrips
    }

    suspend fun insertTrip(trip: Trip) {
        try {
            val tripResponse = convertTripToResponse(trip)
            api.createTrip(userRepository.getUserToken(), tripResponse)
            tripDao.insertTrip(trip)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
        }
    }

    suspend fun updateTrip(trip: Trip) {
        try {
            val tripResponse = convertTripToResponse(trip)
            api.updateTrip(userRepository.getUserToken(), trip.id!!, tripResponse)
            tripDao.insertTrip(trip)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
        }
    }

    suspend fun deleteTrip(trip: Trip) {
        try {
            api.deleteTrip(userRepository.getUserToken(), trip.id!!)
            tripDao.deleteTrip(trip.id!!)
        } catch (e: HttpException) {
            Log.e("TripRepository", "Error: ${e.message()}")
        } catch (e: Exception) {
            Log.e("TripRepository", "Unexpected Error: ${e.message}")
        }
    }

}

