package online.tripguru.tripguruapp.views.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityRegisterBinding
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.network.SignupResponse
import online.tripguru.tripguruapp.viewmodels.UserViewModel

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val userViewModel: UserViewModel by viewModels()
    private var avatarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.buttonAvatar.setOnClickListener { pickAvatar() }
        binding.buttonSignUp.setOnClickListener { registerUser() }
        binding.textViewAlreadyRegistered.setOnClickListener { finish() }
    }

    private fun setupObservers() {
        userViewModel.isOnline().observe(this) { isConnected ->
            updateUiForOnlineStatus(isConnected)
        }
    }

    private fun updateUiForOnlineStatus(isConnected: Boolean) {
        binding.buttonSignUp.isEnabled = isConnected
        if (isConnected) {
            setupSignUpObserver()
        }
    }

    private fun setupSignUpObserver() {
        userViewModel.resultSignUp.observe(this) { result ->
            handleSignUpResult(result)
        }
    }

    private fun handleSignUpResult(result: Resource<SignupResponse>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.sign_up_label), Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            Resource.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickAvatar() {
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                avatarUri = uri
                binding.buttonAvatar.text = getString(R.string.avatar_selected_label)
            }
        }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun registerUser() {
        val username = binding.editTextUsername.text.toString()
        val firstname = binding.editTextFirstName.text.toString()
        val lastname = binding.editTextLastName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()
        userViewModel.signUp(username, firstname, lastname, email, password, confirmPassword, avatarUri)
    }
}