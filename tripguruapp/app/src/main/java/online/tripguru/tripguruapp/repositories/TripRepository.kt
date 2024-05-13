package online.tripguru.tripguruapp.repositories

import androidx.lifecycle.LiveData
import online.tripguru.tripguruapp.local.dao.TripDao
import online.tripguru.tripguruapp.models.Trip

class TripRepository(private val tripDao: TripDao) {
    val allTrips: LiveData<List<Trip>> = tripDao.getTrips()

    fun insert(trip: Trip) {
        tripDao.insert(trip)
    }
}