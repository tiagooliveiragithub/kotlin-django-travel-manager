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
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapterVertical
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.adapters.OnTripClickListener
import online.tripguru.tripguruapp.views.adapters.TripAdapter
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity


@AndroidEntryPoint
class HomeFragment : Fragment(), OnTripClickListener, OnLocalClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterTrip: TripAdapter
    private lateinit var adapterLocal: LocalAdapterVertical

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPage()
        listeners()
        observers()
    }

    private fun setPage() {
        binding.textViewName.text = "${getString(R.string.hometitle_label)} ${userViewModel.getUserOfflineDetails()}!"
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapterTrip = TripAdapter(this)
        recyclerView.adapter = adapterTrip

        val recyclerViewVertical = binding.recyclerViewVertical
        recyclerViewVertical.layoutManager = LinearLayoutManager(context)
        adapterLocal = LocalAdapterVertical(this)
        recyclerViewVertical.adapter = adapterLocal
    }

    private fun listeners() {
        binding.buttonCreateTrip.setOnClickListener {
            mainViewModel.updateSelectedTrip(null)
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
    }

    private fun observers() {
        userViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                binding.buttonCreateTrip.isEnabled = false
            } else {
                binding.buttonCreateTrip.isEnabled = true
                mainViewModel.refreshFetch()
                mainViewModel.resultAllDataFetch.observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.buttonCreateTrip.isEnabled = false
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            binding.buttonCreateTrip.isEnabled = true
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                R.string.error_fetching_data_label,
                                Toast.LENGTH_LONG
                            ).show()
                            binding.buttonCreateTrip.isEnabled = false
                        }
                    }
                }
            }
        }
        mainViewModel.getAllTrips().observe(viewLifecycleOwner) {
            adapterTrip.setTrips(it)
        }
        mainViewModel.getAllLocals().observe(viewLifecycleOwner) {
            adapterLocal.setLocals(it)
        }
    }

    override fun onTripClick(trip: Trip) {
        mainViewModel.updateSelectedTrip(trip)
        (activity as MainActivity).replaceFragment(TripFragment())
        (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.icon_trip
    }

    override fun onLocalClick(local: Local) {
        mainViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }
}