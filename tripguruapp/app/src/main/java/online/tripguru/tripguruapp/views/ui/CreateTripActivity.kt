package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import online.tripguru.tripguruapp.databinding.ActivityCreateTripBinding
import online.tripguru.tripguruapp.helpers.TripViewModelFactory
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.repositories.TripRepository
import online.tripguru.tripguruapp.viewmodels.TripViewModel

class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tripDao = AppDatabase.getDatabase(this).tripDao()
        val repository = TripRepository(tripDao)
        val viewModelFactory = TripViewModelFactory(repository)

        tripViewModel = ViewModelProvider(this, viewModelFactory)[TripViewModel::class.java]

        binding.buttonCreateTrip.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            // Create a new Trip object
            val trip = Trip(tripName = title, startDate = description)

            // Use the ViewModel to insert the new trip into the database
            tripViewModel.insert(trip)
            finish()
        }
    }
}