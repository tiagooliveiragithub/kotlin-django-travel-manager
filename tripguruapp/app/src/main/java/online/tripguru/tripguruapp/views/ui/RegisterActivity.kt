package online.tripguru.tripguruapp.views.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityRegisterBinding
import online.tripguru.tripguruapp.network.auth.RequestResult
import online.tripguru.tripguruapp.viewmodels.AuthViewModel

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonSignUpListener()
        textViewAlreadyRegisteredListener()

        observerSignUpResult()
    }

    private fun observerSignUpResult() {
        authViewModel.signUpResult.observe(this) { result ->
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
            }
        }
    }


    private fun buttonSignUpListener() {
        binding.buttonSignUp.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val firstname = binding.editTextFirstName.text.toString()
            val lastname = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            authViewModel.signUp(username, firstname, lastname, email, password, confirmPassword)
        }
    }

    private fun textViewAlreadyRegisteredListener() {
        binding.textViewAlreadyRegistered.setOnClickListener {
            finish()
        }
    }
}