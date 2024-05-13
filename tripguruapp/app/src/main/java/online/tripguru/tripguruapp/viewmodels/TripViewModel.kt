package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.repositories.TripRepository

class TripViewModel(private val repository: TripRepository) : ViewModel() {
    val allTrips: LiveData<List<Trip>> = repository.allTrips

    fun insert(trip: Trip) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(trip)
    }
}