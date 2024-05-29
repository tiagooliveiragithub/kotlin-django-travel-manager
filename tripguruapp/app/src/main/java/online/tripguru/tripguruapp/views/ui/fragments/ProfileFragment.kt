package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.FragmentProfileBinding
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.ui.LoginActivity

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        listeners()
    }

    private fun observers() {
        userViewModel.isOnline().observe(viewLifecycleOwner) { isOnline ->
            if (!isOnline) {
                binding.buttonSaveChanges.isEnabled = false
                Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show()
            } else {
                // Change after finish save changes feature
                binding.buttonSaveChanges.isEnabled = false
            }
        }
    }

    private fun listeners() {
        binding.buttonSaveChanges.setOnClickListener {
            val firstname = binding.editTextFirstName.text.toString()
            val lastname = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            // userViewModel.editUser(username, firstname, lastname, email, password, confirmPassword)
        }
        binding.buttonSignOut.setOnClickListener {
            userViewModel.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}