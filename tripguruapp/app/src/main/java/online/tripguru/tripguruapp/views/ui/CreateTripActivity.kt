package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.view.View
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

        authViewModel.isOnline().observe(this) { isOnline ->
            if (!isOnline) {
                binding.buttonCreateTrip.visibility = View.GONE
            } else {
                binding.buttonCreateTrip.visibility = View.VISIBLE
                selectedTripObserver()
                createTripResultObserver()
            }

        }

    }

    private fun selectedTripObserver() {
        mainViewModel.getSelectedTrip().observe(this) { selectedTrip ->
            if (selectedTrip != null) {
                binding.editTextTitle.setText(selectedTrip.name)
                binding.editTextDescription.setText(selectedTrip.description)
                binding.buttonCreateTrip.text = getString(R.string.edittrip_button_label)
                setupListenersUpdate(selectedTrip)
            } else {
                binding.buttonCreateTrip.text = getString(R.string.createtrip_button_label)
                setupListenersCreate()
            }
        }
    }

    private fun createTripResultObserver() {
        mainViewModel.resultCreateTrip.observe(this) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Trip saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.FIELDS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, getString(result.fields!!), Toast.LENGTH_SHORT).show()
                }
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

    private fun setupListenersUpdate(selectedTrip: Trip) {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.updateTrip(selectedTrip.id!!, name, description)
        }
    }

}