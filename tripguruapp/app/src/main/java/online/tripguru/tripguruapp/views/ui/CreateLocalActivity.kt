package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateLocalBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.viewmodels.TripViewModel

@AndroidEntryPoint
class CreateLocalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateLocalBinding
    private val tripViewModel: TripViewModel by viewModels()
    private var local : Local? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pageSetup()
        buttonCreateLocalListener()

    }

    private fun buttonCreateLocalListener() {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            tripViewModel.insertLocal(name)
            finish()
        }
    }

    private fun pageSetup() {
        local = tripViewModel.getSelectedLocal()
        tripViewModel.setSelectedLocal(null)
        if (local != null) {
            binding.editTextName.setText(local?.name)
            binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
        } else {
            binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
        }
    }

}