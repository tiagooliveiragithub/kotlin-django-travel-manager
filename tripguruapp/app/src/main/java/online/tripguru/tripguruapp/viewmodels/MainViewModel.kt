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
    private val localRepository: LocalRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val resultCreateTrip = MutableLiveData<Resource<TripResponse>>()
    val resultDeleteTrip = MutableLiveData<Resource<TripResponse>>()
    val resultCreateLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultDeleteLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultAllDataFetch = MutableLiveData<Resource<Boolean>>()

    // Fetch data

    fun getAllTrips() : LiveData<List<Trip>> {
        return tripRepository.getAllTrips()
    }

    fun getAllLocals() : LiveData<List<Local>> {
        return localRepository.getAllLocals()
    }

    fun getAllLocalsForSelectedTrip(): LiveData<List<Local>> {
        return localRepository.getLocalsForTrip(getSelectedTrip().value!!.id ?: 0)
    }

    fun refreshFetch() {
        resultAllDataFetch.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val resultTripsFetch = tripRepository.refreshAllTrips()
            val resultLocalsFetch = localRepository.refreshAllLocals()
            if (resultTripsFetch.status == Resource.Status.SUCCESS && resultLocalsFetch.status == Resource.Status.SUCCESS) {
                resultAllDataFetch.postValue(Resource.success(true))
            } else {
                resultAllDataFetch.postValue(Resource.error(context.getString(R.string.error_fetching_data_label)))
            }
        }
    }

    // Trip CRUD

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

    // Local CRUD

    fun insertLocal(name: String, description: String) {
        resultCreateLocal.postValue(Resource.loading())
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }
        val tripId = getSelectedTrip().value?.id ?: 0
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.insertLocal(LocalRequest(id = null, tripId = tripId, name, description))
            resultCreateLocal.postValue(result)
        }
    }

    fun updateLocal(id: Int, name: String, description: String) {
        if(name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }
        resultCreateLocal.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.updateLocal(LocalRequest(id, null, name, description))
            resultCreateLocal.postValue(result)
        }
    }

    fun deleteLocal(id: Int) {
        resultDeleteLocal.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteLocal(id)
            resultDeleteLocal.postValue(Resource.success(null))
        }
        updateSelectedLocal(null)
    }

    // Selected trip and local

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