package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {
    var allTrips: LiveData<List<Trip>> = repository.allTrips

    fun insert(name: String, startDate: String) {
        if (name.isEmpty() || startDate.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(Trip(null,name, startDate))
        }
    }

    fun insertLocal(name: String) {
        if (name.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLocal(Local(null, getSelectedTrip()?.id, name))
        }
    }

    fun getLocalsForTrip(): LiveData<List<Local>> {
        val tripId = getSelectedTrip()?.id
        if (tripId == null) {
            return MutableLiveData()
        }
        return repository.getLocalsForTrip(tripId)
    }

    fun setSelectedLocal(local: Local?) {
        repository.setSelectedLocal(local)
    }

    fun getSelectedLocal(): Local? {
        return repository.getSelectedLocal()
    }

    fun setSelectedTrip(trip: Trip?) {
        repository.setSelectedTrip(trip)
    }

    fun getSelectedTrip(): Trip? {
        return repository.getSelectedTrip()
    }


}