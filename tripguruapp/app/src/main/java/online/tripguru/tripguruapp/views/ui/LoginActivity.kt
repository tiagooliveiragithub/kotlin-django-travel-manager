package online.tripguru.tripguruapp.views.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityLoginBinding
import online.tripguru.tripguruapp.network.auth.RequestResult
import online.tripguru.tripguruapp.viewmodels.UserViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listeners()
        observe()
    }

    private fun observe() {
        observeIfAuthorized()
        observeSignInResult()
    }

    private fun observeIfAuthorized() {
        userViewModel.isAuthorized().observe(this) { isAuthorized ->
            if (isAuthorized) {
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    private fun observeSignInResult() {
        userViewModel.signInResult.observe(this) { result ->
            when (result) {
                is RequestResult.Authorized -> {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
                is RequestResult.Unauthorized -> {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
                is RequestResult.UnknownError -> {
                    Toast.makeText(this, "An unknown error occurred", Toast.LENGTH_SHORT).show()
                }
                is RequestResult.NoInternet -> {
                    Toast.makeText(this, "No internet available", Toast.LENGTH_SHORT).show()
                }
            }
        }
        userViewModel.isSignedIn().observe(this) { isSignedIn ->
            if (isSignedIn) {
                Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show()
                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }
    private fun listeners() {
        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            userViewModel.signIn(username, password)
        }
        binding.textViewSignUp.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}