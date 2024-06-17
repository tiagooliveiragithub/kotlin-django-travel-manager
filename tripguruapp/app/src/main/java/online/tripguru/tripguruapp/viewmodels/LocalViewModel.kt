package online.tripguru.tripguruapp.viewmodels

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
import kotlinx.coroutines.withContext
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.helpers.getFileFromUri
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.LocalImageResponse
import online.tripguru.tripguruapp.network.LocalRequest
import online.tripguru.tripguruapp.network.LocalResponse
import online.tripguru.tripguruapp.repositories.LocalRepository
import online.tripguru.tripguruapp.repositories.LocationRepository
import online.tripguru.tripguruapp.repositories.TripRepository
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val tripRepository: TripRepository,
    private val locationRepository: LocationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val resultRefreshLocals = MutableLiveData<Resource<List<LocalResponse>>>()
    val resultCreateLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultDeleteLocal = MutableLiveData<Resource<LocalResponse>>()
    val resultImageFetch = MutableLiveData<Resource<List<LocalImageResponse>>>()
    val currentLocation = MutableLiveData<Location?>()
    val currentAddress = MutableLiveData<String?>()

    fun refreshAllLocals() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.refreshAllLocals()
            resultRefreshLocals.postValue(result)
        }
    }

    fun getAllOfflineLocals(): LiveData<List<Local>> {
        return localRepository.getAllLocals()
    }

    fun getAllLocalsForSelectedTrip(): LiveData<List<Local>> {
        val selectedTrip = tripRepository.getSelectedTrip().value ?: return MutableLiveData()
        return localRepository.getLocalsForTrip(selectedTrip.id!!)
    }

    fun insertLocal(name: String, description: String) {
        resultCreateLocal.postValue(Resource.loading())
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }

        val latitude = currentLocation.value?.latitude
        val longitude = currentLocation.value?.longitude
        val address = currentAddress.value
        val tripId = tripRepository.getSelectedTrip().value?.id

        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.insertLocal(LocalRequest(null, tripId, name, description, latitude, longitude, address))
            resultCreateLocal.postValue(result)
        }
    }

    fun updateLocal(id: Int, name: String, description: String, imageUri: Uri?) {
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }

        val latitude = currentLocation.value?.latitude
        val longitude = currentLocation.value?.longitude
        val address = currentAddress.value
        val tripId = tripRepository.getSelectedTrip().value?.id

        resultCreateLocal.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.updateLocal(LocalRequest(id, tripId, name, description, latitude, longitude, address))
            resultCreateLocal.postValue(result)
        }

        if (imageUri != null) {
            viewModelScope.launch(Dispatchers.IO) {
                localRepository.uploadImage(
                    localId = id,
                    imageUriPart = getFileFromUri(context, imageUri, "image")
                )
            }
        }
    }

    fun deleteLocal(id: Int) {
        resultDeleteLocal.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteLocal(id)
            resultDeleteLocal.postValue(Resource.success(null))
        }
        localRepository.updateSelectedLocal(null)
    }

    fun getLocalImages(localId: Int) {
        resultImageFetch.postValue(Resource.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = localRepository.getLocalImages(localId)
            resultImageFetch.postValue(result)
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            locationRepository.getCurrentLocation(
                onSuccess = { location ->
                    currentLocation.postValue(location)
                    if (location != null) {
                        fetchAddress(location)
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun fetchAddress(location: Location) {
        viewModelScope.launch {
            try {
                val addresses: List<Address> = withContext(Dispatchers.IO) {
                    Geocoder(context, Locale.getDefault()).getFromLocation(location.latitude, location.longitude, 1)!!
                }
                if (addresses.isNotEmpty()) {
                    currentAddress.postValue(addresses[0].getAddressLine(0))
                } else {
                    currentAddress.postValue("Address not found")
                }
            } catch (e: Exception) {
                currentAddress.postValue("Address lookup failed: ${e.message}")
            }
        }
    }

    fun updateSelectedLocal(local: Local?) {
        localRepository.updateSelectedLocal(local)
    }

    fun getSelectedLocal(): LiveData<Local?> {
        return localRepository.getSelectedLocal()
    }
}