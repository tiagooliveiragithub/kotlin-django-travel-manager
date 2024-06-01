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
import online.tripguru.tripguruapp.network.LocalRequest
import online.tripguru.tripguruapp.network.LocalResponse
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.network.TripRequest
import online.tripguru.tripguruapp.network.TripResponse
import online.tripguru.tripguruapp.repositories.LocalRepository
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val localRepository: LocalRepository
) : ViewModel() {
    val resultCreateTrip = MutableLiveData<Resource<TripResponse>>()
    val resultUpdateTrip = MutableLiveData<Resource<TripResponse>>()
    val resultDeleteTrip = MutableLiveData<Resource<TripResponse>>()
    val resultCreateLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultUpdateLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultDeleteLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultAllDataFetch = MutableLiveData<Resource<Boolean>>()

    fun getAllTrips() : LiveData<List<Trip>> {
        return tripRepository.getAllTrips()
    }

    fun getAllLocals() : LiveData<List<Local>> {
        return localRepository.getAllLocals()
    }

    fun getAllLocalsforSelectedTrip(): LiveData<List<Local>> {
        return localRepository.getLocalsForTrip(getSelectedTrip().value!!.id ?: 0)
    }

    fun refreshFetch() {
        resultAllDataFetch.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val resultTrips = tripRepository.refreshAllTrips()
            val resultLocals = localRepository.refreshAllLocals()
            if (resultTrips.status == Resource.Status.SUCCESS && resultLocals.status == Resource.Status.SUCCESS) {
                resultAllDataFetch.postValue(Resource.success(true))
            } else {
                resultAllDataFetch.postValue(Resource.error("Error fetching data", null))
            }
        }
    }

    fun insertTrip(name: String, description: String) {
        resultCreateTrip.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.insertTrip(TripRequest(id = null, name, description))
            resultCreateTrip.postValue(result)
        }
    }
    fun updateTrip(id: Int, name: String, description: String) {
        resultUpdateTrip.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.updateTrip(TripRequest(id, name, description))
            resultUpdateTrip.postValue(result)
        }
        tripRepository.updateSelectedTrip(Trip(id, name, description))
    }

    fun deleteTrip(id: Int) {
        resultUpdateTrip.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.deleteTrip(id)
            resultDeleteTrip.postValue(result)
        }
        tripRepository.updateSelectedTrip(null)
    }

    fun insertLocal(name: String, description: String) {
        resultCreateTrip.postValue(Resource.loading(null))
        val tripId = getSelectedTrip().value?.id ?: 0
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.insertLocal(LocalRequest(id = null, tripId = tripId, name, description))
            resultCreateLocal.postValue(result)
        }
    }

    fun updateLocal(id: Int, name: String, description: String) {
        resultUpdateLocal.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.updateLocal(LocalRequest(id, null, name, description))
            resultUpdateLocal.postValue(result)
        }
    }

    fun deleteLocal(id: Int) {
        resultDeleteLocal.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteLocal(id)
            resultDeleteLocal.postValue(Resource.success(null))
        }
        updateSelectedLocal(null)
    }

    fun updateSelectedTrip(trip: Trip?) {
        tripRepository.updateSelectedTrip(trip)
    }

    fun getSelectedTrip(): LiveData<Trip?> {
        return tripRepository.getSelectedTrip()
    }

    fun updateSelectedLocal(local: Local?) {
        localRepository.updateSelectedLocal(local)
    }

    fun getSelectedLocal(): LiveData<Local?> {
        return localRepository.getSelectedLocal()
    }
}