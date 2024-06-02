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
        authViewModel.isOnline().observe(this) { isOnline ->
            if (!isOnline) {
                Toast.makeText(this, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
                binding.buttonDeleteLocal.visibility = View.GONE
                binding.buttonCreateLocal.visibility = View.GONE
            } else {
                Toast.makeText(this, getString(R.string.yesinternet_label), Toast.LENGTH_SHORT).show()
                binding.buttonCreateLocal.visibility = View.VISIBLE
                binding.buttonDeleteLocal.visibility = View.VISIBLE
                mainViewModel.resultCreateLocal.observe(this) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Local saved successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                        Resource.Status.FIELDS -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                getString(result.fields!!),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                result.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
        }
        mainViewModel.getSelectedLocal().observe(this) { selectedLocal ->
            if (selectedLocal != null) {
                binding.buttonDeleteLocal.visibility = View.VISIBLE
                binding.editTextName.setText(selectedLocal.name)
                binding.editTextDescription.setText(selectedLocal.description)
                binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
                setupListenersEdit(selectedLocal)
            } else {
                binding.buttonDeleteLocal.visibility = View.GONE
                binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
                setupListenersCreate()
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

    private fun setupListenersEdit(selectedLocal: Local) {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.updateLocal(selectedLocal.id!!, name, description)
        }
        binding.buttonDeleteLocal.setOnClickListener {
            mainViewModel.deleteLocal(selectedLocal.id!!)
        }
    }
}