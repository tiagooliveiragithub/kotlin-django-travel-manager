package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityCreateTripBinding
import online.tripguru.tripguruapp.viewmodels.TripViewModel

@AndroidEntryPoint
class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    private val tripViewModel: TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}