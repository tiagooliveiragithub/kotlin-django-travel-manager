package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateTripBinding
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.TripResponse
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel

@AndroidEntryPoint
class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    private val tripViewModel: TripViewModel by viewModels()
    private val authViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
    }

    private fun setupObservers() {
        setupOnlineStatusObserver()
        setupSelectedTripObserver()
        createTripResultObserver()
    }

    private fun setupOnlineStatusObserver() {
        authViewModel.isOnline().observe(this) { isOnline ->
            updateUiForOnlineStatus(isOnline)
        }
    }

    private fun updateUiForOnlineStatus(isOnline: Boolean) {
        binding.buttonCreateTrip.visibility = if (isOnline) View.VISIBLE else View.GONE
    }

    private fun setupSelectedTripObserver() {
        tripViewModel.getSelectedTrip().observe(this) { selectedTrip ->
            updateUiForSelectedTrip(selectedTrip)
        }
    }

    private fun updateUiForSelectedTrip(selectedTrip: Trip?) {
        if (selectedTrip != null) {
            binding.editTextTitle.setText(selectedTrip.name)
            binding.editTextDescription.setText(selectedTrip.description)
            binding.buttonCreateTrip.text = getString(R.string.edittrip_button_label)
            setupListenersUpdate(selectedTrip)
        } else {
            binding.editTextTitle.text = null
            binding.editTextDescription.text = null
            binding.buttonCreateTrip.text = getString(R.string.createtrip_button_label)
            setupListenersCreate()
        }
    }

    private fun createTripResultObserver() {
        tripViewModel.resultCreateTrip.observe(this) { result ->
            handleCreateTripResult(result)
        }
    }

    private fun handleCreateTripResult(result: Resource<TripResponse>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.successtrip_label, Toast.LENGTH_LONG).show()
                finish()
            }
            Resource.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListenersCreate() {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            tripViewModel.insertTrip(name, description)
        }
    }

    private fun setupListenersUpdate(selectedTrip: Trip) {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            tripViewModel.updateTrip(selectedTrip.id!!, name, description)
        }
    }
}