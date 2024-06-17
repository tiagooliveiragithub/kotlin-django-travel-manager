package online.tripguru.tripguruapp.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.TripRequest
import online.tripguru.tripguruapp.network.TripResponse
import online.tripguru.tripguruapp.repositories.LocalRepository
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val localRepository: LocalRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val resultRefreshTrips = MutableLiveData<Resource<List<TripResponse>>>()
    val resultCreateTrip = MutableLiveData<Resource<TripResponse>>()
    val resultDeleteTrip = MutableLiveData<Resource<TripResponse>>()

    fun refreshAllTrips() {
        resultRefreshTrips.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.refreshAllTrips()
            resultRefreshTrips.postValue(result)
        }
    }

    fun getAllOfflineTrips(): LiveData<List<Trip>> {
        return tripRepository.getAllTrips()
    }

    fun insertTrip(name: String, description: String) {
        resultCreateTrip.postValue(Resource.loading())
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.insertTrip(TripRequest(id = null, name, description))
            resultCreateTrip.postValue(result)
        }
    }

    fun updateTrip(id: Int, name: String, description: String) {
        resultCreateTrip.postValue(Resource.loading())
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.updateTrip(TripRequest(id, name, description))
            resultCreateTrip.postValue(result)
        }
        tripRepository.updateSelectedTrip(Trip(id, name, description))
    }

    fun deleteTrip(id: Int) {
        resultCreateTrip.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.deleteTrip(id)
            resultDeleteTrip.postValue(result)
        }
        tripRepository.updateSelectedTrip(null)
    }

    fun updateSelectedTrip(trip: Trip?) {
        tripRepository.updateSelectedTrip(trip)
    }

    fun getSelectedTrip(): LiveData<Trip?> {
        return tripRepository.getSelectedTrip()
    }
}