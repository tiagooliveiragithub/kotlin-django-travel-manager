package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateTripBinding
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel

@AndroidEntryPoint
class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val authViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observers()
    }

    private fun observers() {
        mainViewModel.getSelectedTrip().observe(this) { trip ->
            if (trip != null) {
                binding.editTextTitle.setText(trip.name)
                binding.editTextDescription.setText(trip.description)
                binding.buttonCreateTrip.text = getString(R.string.edittrip_button_label)
                setupListenersUpdate(trip)
            } else {
                binding.buttonCreateTrip.text = getString(R.string.createtrip_button_label)
                setupListenersCreate()
            }
        }
        authViewModel.isOnline().observe(this) { isOnline ->
            binding.buttonCreateTrip.isEnabled = isOnline
        }
        mainViewModel.resultCreateTrip.observe(this) { result ->
            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(
                    this,
                    "Trip created successfully",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(
                    this,
                    "Error creating trip",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        mainViewModel.resultUpdateTrip.observe(this) { result ->
            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(
                    this,
                    "Trip updated successfully",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(
                    this,
                    "Error updating trip",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupListenersCreate() {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.insertTrip(name, description)
        }
    }

    private fun setupListenersUpdate(trip: Trip) {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.updateTrip(trip.id!!, name, description)
        }
    }

}