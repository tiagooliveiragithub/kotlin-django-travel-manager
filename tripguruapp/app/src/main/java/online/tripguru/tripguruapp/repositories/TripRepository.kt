package online.tripguru.tripguruapp.repositories

import androidx.lifecycle.LiveData
import online.tripguru.tripguruapp.local.dao.LocalDao
import online.tripguru.tripguruapp.local.dao.TripDao
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {
    private val tripDao: TripDao = appDatabase.tripDao()
    private val localDao: LocalDao = appDatabase.localDao()
    private var localSelected: Local? = null
    private var tripSelected: Trip? = null
    val allTrips: LiveData<List<Trip>> = tripDao.getTrips()
    val allLocals: LiveData<List<Local>> = localDao.getLocals()

    fun insert(trip: Trip) {
        tripDao.insert(trip)
    }

    fun insertLocal(local: Local) {
        localDao.insert(local)
    }

    fun getLocalsForTrip(tripId: Int): LiveData<List<Local>> {
        return localDao.getLocalsForTrip(tripId)
    }

    fun getLocalById(localId: Int): LiveData<Local> {
        return localDao.getLocalById(localId)
    }

    fun setSelectedLocal(local: Local?) {
        localSelected = local
    }

    fun getSelectedLocal(): Local? {
        return localSelected
    }

    fun setSelectedTrip(trip: Trip?) {
        tripSelected = trip
    }

    fun getSelectedTrip(): Trip? {
        return tripSelected
    }



}
