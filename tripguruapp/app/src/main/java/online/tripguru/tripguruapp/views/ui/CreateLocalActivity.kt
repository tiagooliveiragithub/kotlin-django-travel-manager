package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateLocalBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel

@AndroidEntryPoint
class CreateLocalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateLocalBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val authViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observers()
    }

    private fun observers() {
        mainViewModel.getSelectedLocal().observe(this) { local ->
            if (local != null) {
                binding.editTextName.setText(local.name)
                binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
                setupListenersEdit(local)
            } else {
                binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
                setupListenersCreate()
            }
        }
        authViewModel.isAuthorized().observe(this) { isAuthorized ->
            if (!isAuthorized) {
                finish()
            }
        }
        authViewModel.isOnline().observe(this) { isOnline ->
            if (!isOnline) {
                binding.buttonCreateLocal.isEnabled = false
                Toast.makeText(this, "No internet available", Toast.LENGTH_SHORT).show()
            } else {
                binding.buttonCreateLocal.isEnabled = true
            }

        }
    }

    private fun setupListenersCreate() {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
//            mainViewModel.insertLocal(name)
            finish()
        }
    }

    private fun setupListenersEdit(local: Local) {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
//            mainViewModel.updateLocal(local.copy(name = name))
            finish()
        }
    }


}