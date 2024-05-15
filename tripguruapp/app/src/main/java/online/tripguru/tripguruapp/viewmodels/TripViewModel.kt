package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    val allTrips: LiveData<List<Trip>> = repository.allTrips

    fun insert(name: String, startDate: String) {
        if (name.isEmpty() || startDate.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(Trip(tripName = name, startDate = startDate))
        }
    }
}