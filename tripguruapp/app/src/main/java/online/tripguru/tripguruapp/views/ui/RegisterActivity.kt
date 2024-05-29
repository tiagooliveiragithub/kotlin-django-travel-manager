package online.tripguru.tripguruapp.views.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityRegisterBinding
import online.tripguru.tripguruapp.network.auth.RequestResult
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
        observeIfOnline()
        observerSignUpResult()
    }

    private fun observeIfOnline() {
        userViewModel.isOnline().observe(this) { isOnline ->
            if (!isOnline) {
                binding.buttonSignUp.isEnabled = false
                Toast.makeText(this, "No internet available", Toast.LENGTH_SHORT).show()
            } else {
                binding.buttonSignUp.isEnabled = true
            }

        }
    }

    private fun observerSignUpResult() {
        userViewModel.signUpResult.observe(this) { result ->
            when (result) {
                is RequestResult.Authorized -> {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
                is RequestResult.Unauthorized -> {
                    Toast.makeText(this, "Invalid Data for registration", Toast.LENGTH_SHORT).show()
                }
                is RequestResult.UnknownError -> {
                    Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_SHORT).show()
                }
                is RequestResult.NoInternet -> {
                    Toast.makeText(this, "No internet available", Toast.LENGTH_SHORT).show()
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