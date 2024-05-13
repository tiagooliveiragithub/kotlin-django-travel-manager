package online.tripguru.tripguruapp.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import online.tripguru.tripguruapp.repositories.TripRepository
import online.tripguru.tripguruapp.viewmodels.TripViewModel

class TripViewModelFactory(private val repository: TripRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}