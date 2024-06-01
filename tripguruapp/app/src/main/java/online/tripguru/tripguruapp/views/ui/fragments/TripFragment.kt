package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapterVertical
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity

@AndroidEntryPoint
class TripFragment : Fragment(), OnLocalClickListener {
    private lateinit var localAdapter: LocalAdapterVertical
    private lateinit var binding: FragmentTripBinding
    private lateinit var trip: Trip
    private val mainViewModel: MainViewModel by activityViewModels()
    private val authViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        listeners()
        observers()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewVertical
        recyclerView.layoutManager = LinearLayoutManager(context)
        localAdapter = LocalAdapterVertical(this)
        recyclerView.adapter = localAdapter
    }

    private fun observers() {
        mainViewModel.getAllLocalsforSelectedTrip().observe(viewLifecycleOwner) { locals ->
            locals?.let {
                localAdapter.setLocals(it)
            }
        }
        authViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                Toast.makeText(
                    requireContext(),
                    "Disconnected from the internet",
                    Toast.LENGTH_LONG
                ).show()
                binding.buttonEditTripPage.isEnabled = false
                binding.buttonCreateLocalPage.isEnabled = false
                binding.buttonDeleteTrip.isEnabled = false
            } else {
                binding.buttonEditTripPage.isEnabled = true
                binding.buttonCreateLocalPage.isEnabled = true
                binding.buttonDeleteTrip.isEnabled = true
            }
        }
        mainViewModel.getSelectedTrip().observe(viewLifecycleOwner) { trip ->
            trip?.let {
                this.trip = it
                binding.textViewTripName.text = it.name
            }
        }

    }

    private fun listeners() {
        binding.buttonEditTripPage.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
        binding.buttonCreateLocalPage.setOnClickListener {
            mainViewModel.updateSelectedLocal(null)
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
        binding.buttonDeleteTrip.setOnClickListener {
            mainViewModel.deleteTrip(trip)
            changeToHomeFragment()
        }
    }

    override fun onLocalClick(local: Local) {
        mainViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }

    private fun changeToHomeFragment() {
        (activity as MainActivity).replaceFragment(HomeFragment())
    }

    override fun onResume() {
        super.onResume()
        // TODO: temporary solution to update the list after deleting a local
        mainViewModel.refreshAllLocals()
    }


}