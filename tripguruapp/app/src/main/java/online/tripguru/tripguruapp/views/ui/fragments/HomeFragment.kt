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
    private val authViewModel: UserViewModel by activityViewModels()
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
        setRecyclerViews()
        listeners()
        observers()
    }

    private fun setRecyclerViews() {
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
        authViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                binding.buttonCreateTrip.isEnabled = false
                Toast.makeText(
                    requireContext(),
                    "Disconnected from the internet",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.buttonCreateTrip.isEnabled = true
            }
        }
        mainViewModel.getAllTrips().observe(viewLifecycleOwner) {
            adapterTrip.setTrips(it)
        }
        mainViewModel.getAllLocals().observe(viewLifecycleOwner) {
            adapterLocal.setLocals(it)
        }
        mainViewModel.resultAllDataFetch.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error fetching data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }



    override fun onTripClick(trip: Trip) {
        mainViewModel.updateSelectedTrip(trip)
        changeToHomeFragment()
    }

    override fun onLocalClick(local: Local) {
        mainViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }

    private fun changeToHomeFragment() {
        (activity as MainActivity).replaceFragment(TripFragment())
    }

    override fun onResume() {
        super.onResume()
        // TODO : Temporary solution to refresh data
        mainViewModel.refreshFetch()
    }



}