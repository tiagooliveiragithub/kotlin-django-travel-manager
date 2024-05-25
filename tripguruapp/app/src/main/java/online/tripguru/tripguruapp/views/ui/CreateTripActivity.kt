package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateTripBinding
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.viewmodels.TripViewModel

@AndroidEntryPoint
class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    private val tripViewModel: TripViewModel by viewModels()
    private var trip : Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pageSetup()
        buttonCreateTripListener()

    }

    private fun buttonCreateTripListener() {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val startDate = binding.editTextDescription.text.toString()
            tripViewModel.insert(name, startDate)
            finish()
        }
    }

    private fun pageSetup() {
        trip = tripViewModel.getSelectedTrip()
        tripViewModel.setSelectedTrip(null)
        if (trip != null) {
            binding.editTextTitle.setText(trip?.tripName)
            binding.buttonCreateTrip.text = getString(R.string.edittrip_button_label)
        } else {
            binding.buttonCreateTrip.text = getString(R.string.createtrip_button_label)
        }
    }
}