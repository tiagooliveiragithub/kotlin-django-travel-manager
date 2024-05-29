package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.repositories.LocalRepository
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val localRepository: LocalRepository
) : ViewModel() {
    var allLocals: LiveData<List<Local>> = localRepository.allLocals
    var allTrips: LiveData<List<Trip>> = tripRepository.allTrips

    fun refreshAllTrips() {
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.refreshAllTrips()
        }
    }

    fun insertTrip(name: String, description: String) {
        val trip = Trip(name = name, description = description)
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.insertTrip(trip)
        }
        updateSelectedTrip(trip)
    }
    fun updateTrip(id: Int, name: String, description: String) {
        val trip = Trip(id = id, name = name, description = description)
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.updateTrip(trip)
        }
        updateSelectedTrip(trip)
    }
    fun deleteTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.deleteTrip(trip)
        }
    }

    fun updateSelectedTrip(trip: Trip?) {
        tripRepository.updateSelectedTrip(trip)
    }

    fun getSelectedTrip(): LiveData<Trip?> {
        return tripRepository.getSelectedTrip()
    }

    fun updateSelectedLocal(local: Local) {
        localRepository.updateSelectedLocal(local)
    }

    fun getSelectedLocal(): LiveData<Local?> {
        return localRepository.getSelectedLocal()
    }
}