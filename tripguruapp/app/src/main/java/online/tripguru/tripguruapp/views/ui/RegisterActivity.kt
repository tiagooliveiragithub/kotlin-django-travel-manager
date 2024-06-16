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
import online.tripguru.tripguruapp.network.Resource
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
        listeners()
        observers()
    }

    private fun observers() {
        userViewModel.isOnline().observe(this) { isOnline ->
            binding.buttonSignUp.isEnabled = isOnline

        }
        userViewModel.isOnline().observe(this) { isConnected ->
            if (!isConnected) {
                Toast.makeText(this, getString(R.string.nointernet_label), Toast.LENGTH_SHORT)
                    .show()
                binding.buttonSignUp.isEnabled = false
            } else {
                binding.buttonSignUp.isEnabled = true
                userViewModel.resultSignUp.observe(this) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                getString(R.string.sign_up_label),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    private fun listeners() {
        buttonAvatarListener()
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
            userViewModel.signUp(
                username,
                firstname,
                lastname,
                email,
                password,
                confirmPassword,
                avatarUri,
            );
        }
    }


    private fun textViewAlreadyRegisteredListener() {
        binding.textViewAlreadyRegistered.setOnClickListener {
            finish()
        }
    }

    private fun buttonAvatarListener() {
         val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriImageSelected ->
            if (uriImageSelected != null) {
                avatarUri = uriImageSelected
            }
        }
        binding.buttonAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

}