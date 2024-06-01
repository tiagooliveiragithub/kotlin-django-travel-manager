package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityRegisterBinding
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.viewmodels.UserViewModel

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listeners()
        observers()
    }

    private fun observers() {
        userViewModel.isOnline().observe(this) { isOnline ->
            binding.buttonSignUp.isEnabled = isOnline

        }
        userViewModel.resultSignUp.observe(this) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Signed up", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun listeners() {
        buttonSignUpListener()
        textViewAlreadyRegisteredListener()
    }

    private fun buttonSignUpListener() {
        binding.buttonSignUp.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val firstname = binding.editTextFirstName.text.toString()
            val lastname = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            userViewModel.signUp(username, firstname, lastname, email, password, confirmPassword)
        }
    }

    private fun textViewAlreadyRegisteredListener() {
        binding.textViewAlreadyRegistered.setOnClickListener {
            finish()
        }
    }
}