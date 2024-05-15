package online.tripguru.tripguruapp.repositories

import androidx.lifecycle.LiveData
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Trip
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {
    val allTrips: LiveData<List<Trip>> = appDatabase.tripDao().getTrips()
    suspend fun insert(trip: Trip) {
        appDatabase.tripDao().insert(trip)
    }
}