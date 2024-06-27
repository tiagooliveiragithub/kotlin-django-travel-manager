package online.tripguru.tripguruapp.viewmodels

import android.content.Context
import android.net.Uri
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
import online.tripguru.tripguruapp.helpers.getFileFromUri
import online.tripguru.tripguruapp.localstorage.models.Trip
import online.tripguru.tripguruapp.network.TripRequest
import online.tripguru.tripguruapp.network.TripResponse
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val resultRefreshTrips = MutableLiveData<Resource<List<TripResponse>>>()
    val resultCreateTrip = MutableLiveData<Resource<Trip>>()
    val resultDeleteTrip = MutableLiveData<Resource<Trip>>()
    val resultShareTrip = MutableLiveData<Resource<Trip>?>()

    fun getAllOfflineTrips(): LiveData<List<Trip>> {
        return tripRepository.getAllOfflineTrips()
    }

    fun refreshAllTrips() {
        resultRefreshTrips.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.refreshAllTrips()
            resultRefreshTrips.postValue(result)
        }

    }

    fun insertTrip(name: String, description: String, startDate: String, endDate: String, imageUri: Uri?) {
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }

        resultCreateTrip.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.insertTrip(TripRequest(null, name, description, startDate, endDate))

            if (imageUri != null && result.data != null) {
                tripRepository.uploadImage(result.data.id!!, getFileFromUri(context, imageUri, "image"))
            }

            resultCreateTrip.postValue(result)
        }
    }

    fun updateTrip(name: String, description: String, startDate: String, endDate: String, imageUri: Uri?) {
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }

        resultCreateTrip.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.updateTrip(TripRequest(getSelectedTrip().value?.id, name, description, startDate, endDate))

            if (imageUri != null && result.data != null) {
                tripRepository.uploadImage(result.data.id!!, getFileFromUri(context, imageUri, "image"))
            }

            resultCreateTrip.postValue(result)
        }
    }

    fun deleteTrip() {
        resultCreateTrip.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.deleteTrip(getSelectedTrip().value?.id!!)
            resultDeleteTrip.postValue(Resource.success(null))
        }
    }

    fun shareTrip(username: String?) {
        if(username == null) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }
        resultShareTrip.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.shareTrip(getSelectedTrip().value?.id!!, username)
            resultShareTrip.postValue(result)
        }
    }

    fun removeShareTrip(username: String?) {
        if(username == null) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }
        resultShareTrip.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = tripRepository.removeShareTrip(getSelectedTrip().value?.id!!, username)
            resultShareTrip.postValue(result)
        }
    }

    fun updateSelectedTrip(trip: Trip?) {
        tripRepository.updateSelectedTrip(trip)
    }

    fun getSelectedTrip(): LiveData<Trip?> {
        return tripRepository.getSelectedTrip()
    }
}