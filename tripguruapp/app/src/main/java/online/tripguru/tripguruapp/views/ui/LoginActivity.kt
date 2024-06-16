package online.tripguru.tripguruapp.views.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityLoginBinding
import online.tripguru.tripguruapp.network.Resource
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
        observers()
    }

    private fun observers() {
        userViewModel.isOnline().observe(this) { isConnected ->
            if (!isConnected) {
                Toast.makeText(this, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.autoSignIn()
                userViewModel.resultSignIn.observe(this) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, getString(R.string.loginsuccess_label), Toast.LENGTH_SHORT).show()
                            Intent(this, MainActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
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