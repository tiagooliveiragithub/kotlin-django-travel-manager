package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.FragmentProfileBinding
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.ui.LoginActivity
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding
    @Inject lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageSetup()
        setListeners()
        setObservers()
    }

    private fun pageSetup() {
        binding.textViewProfileName.text = userViewModel.getUserOfflineDetails()
    }

    private fun setObservers() {
        userViewModel.isOnline().observe(viewLifecycleOwner) { isOnline ->
            if (!isOnline) {
                binding.buttonSaveChanges.isEnabled = false
            } else {
                binding.buttonSaveChanges.isEnabled = true
                userViewModel.getUserInfo()
                userViewModel.resultGetUserInfo.observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            result.data?.let {
                                binding.editTextFirstName.setText(it.first_name)
                                binding.editTextLastName.setText(it.last_name)
                                binding.editTextEmail.setText(it.email)
                                glide.load(("https://apicm.tiagooliveira.dev/" + it.avatar)).into(binding.profileImage)
                            }
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                userViewModel.resultEditProfile.observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, R.string.saveprofile_label, Toast.LENGTH_SHORT).show()
                            result.data?.let {
                                binding.editTextFirstName.setText(it.first_name)
                                binding.editTextLastName.setText(it.last_name)
                                binding.editTextEmail.setText(it.email)
                            }
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.buttonSaveChanges.setOnClickListener {
            val firstname = binding.editTextFirstName.text.toString()
            val lastname = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            userViewModel.editUser(firstname, lastname, email, password, confirmPassword)
        }
        binding.buttonSignOut.setOnClickListener {
            userViewModel.signOut()
            Toast.makeText(context, getString(R.string.signout_label), Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}