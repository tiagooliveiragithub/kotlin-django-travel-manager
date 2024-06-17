package online.tripguru.tripguruapp.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) {
    fun getCurrentLocation(onSuccess: (Location?) -> Unit, onFailure: (Exception) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    onSuccess(location)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            onFailure(SecurityException("Location permission not granted"))
        }
    }
}