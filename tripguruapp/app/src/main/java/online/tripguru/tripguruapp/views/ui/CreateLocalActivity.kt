package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateLocalBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.Resource
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
                binding.editTextDescription.setText(local.description)
                binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
                binding.buttonDeleteLocal.visibility = View.VISIBLE
                setupListenersEdit(local)
            } else {
                binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
                binding.buttonDeleteLocal.visibility = View.GONE
                setupListenersCreate()
            }
        }
        authViewModel.isOnline().observe(this) { isOnline ->
            if (!isOnline) {
                binding.buttonCreateLocal.isEnabled = false
                binding.buttonDeleteLocal.isEnabled = false
            } else {
                binding.buttonCreateLocal.isEnabled = true
                binding.buttonDeleteLocal.isEnabled = true
            }
        }
        mainViewModel.resultCreateLocal.observe(this) { result ->
            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(
                    this,
                    "Local created successfully",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
        mainViewModel.resultUpdateLocal.observe(this) { result ->
            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(
                    this,
                    "Local updated successfully",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
        mainViewModel.resultDeleteLocal.observe(this) { result ->
            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(
                    this,
                    "Local deleted successfully",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun setupListenersCreate() {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.insertLocal(name, description)
        }
    }

    private fun setupListenersEdit(local: Local) {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.updateLocal(local.id!!, name, description)

        }
        binding.buttonDeleteLocal.setOnClickListener {
            mainViewModel.deleteLocal(local.id!!)
        }
    }
}