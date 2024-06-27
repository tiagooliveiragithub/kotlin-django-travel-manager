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
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.FragmentHomeBinding
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.localstorage.models.Trip
import online.tripguru.tripguruapp.viewmodels.LocalViewModel
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapter
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.adapters.OnTripClickListener
import online.tripguru.tripguruapp.views.adapters.TripAdapter
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity


@AndroidEntryPoint
class HomeFragment : Fragment(), OnTripClickListener, OnLocalClickListener {

    private val tripViewModel: TripViewModel by activityViewModels()
    private val localViewModel: LocalViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterLocal: LocalAdapter
    private lateinit var adapterTrip: TripAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPage()
        setupListeners()
        setupObservers()
        tripViewModel.refreshAllTrips()
        localViewModel.refreshAllLocals()
    }

    private fun setupPage() {
        binding.textViewName.text = "${getString(R.string.hometitle_label)} ${userViewModel.getUserOfflineDetails()}!"
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapterTrip = TripAdapter(this)
        binding.recyclerView.adapter = adapterTrip

        binding.recyclerViewVertical.layoutManager = LinearLayoutManager(context)
        adapterLocal = LocalAdapter(this)
        binding.recyclerViewVertical.adapter = adapterLocal
    }

    private fun setupListeners() {
        binding.buttonCreateTrip.setOnClickListener {
            tripViewModel.updateSelectedTrip(null)
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
    }

    private fun setupObservers() {
        userViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            updateUiForOnlineStatus(isConnected)
        }
        tripViewModel.getAllOfflineTrips().observe(viewLifecycleOwner) { trips ->
            adapterTrip.setTrips(trips)
        }
        localViewModel.getAllOfflineLocals().observe(viewLifecycleOwner) { locals ->
            adapterLocal.setLocals(locals)
        }
    }

    private fun updateUiForOnlineStatus(isConnected: Boolean) {
        if (!isConnected) {
            Toast.makeText(context, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
            binding.buttonCreateTrip.isEnabled = false
        } else {
            binding.buttonCreateTrip.isEnabled = true
        }

    }

    override fun onTripClick(trip: Trip) {
        tripViewModel.updateSelectedTrip(trip)
        (activity as MainActivity).replaceFragment(TripFragment())
        (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.icon_trip
    }

    override fun onLocalClick(local: Local) {
        localViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }
}