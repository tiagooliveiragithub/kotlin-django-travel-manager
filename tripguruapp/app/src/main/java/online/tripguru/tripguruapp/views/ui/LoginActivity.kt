package online.tripguru.tripguruapp.views.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityLoginBinding
import online.tripguru.tripguruapp.network.auth.RequestResult
import online.tripguru.tripguruapp.viewmodels.AuthViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonLoginListener()
        textViewSignUpListener()

        observeSignInResult()

    }

    private fun observeSignInResult() {
        authViewModel.signInResult.observe(this) { result ->
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
            }
        }
    }

    private fun buttonLoginListener() {
        // Login using auth repository
        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            authViewModel.signIn(username, password)
        }
    }

    private fun textViewSignUpListener() {
        binding.textViewSignUp.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}