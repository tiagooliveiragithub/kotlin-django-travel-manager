package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.databinding.FragmentHomeBinding
import online.tripguru.tripguruapp.helpers.TripViewModelFactory
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.repositories.TripRepository
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.TripAdapter
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import java.util.Date


class HomeFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var tripAdapter: TripAdapter? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val tripDao = AppDatabase.getDatabase(requireContext()).tripDao()
        val repository = TripRepository(tripDao)
        val viewModelFactory = TripViewModelFactory(repository)

        tripViewModel = ViewModelProvider(this, viewModelFactory)[TripViewModel::class.java]


        tripViewModel.allTrips.observe(viewLifecycleOwner, Observer { trips ->
            // Update the cached copy of the words in the adapter.
            trips?.let {
                tripAdapter?.setTrips(it)
                tripAdapter?.notifyDataSetChanged() // Notify the adapter that the data has changed
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        tripAdapter = TripAdapter(emptyList()) // Initialize the adapter with an empty list
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.adapter = tripAdapter

        binding.buttonCreateTrip.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
    }

    private fun generateFakeTrips(): List<Trip> {
        val trips: MutableList<Trip> = ArrayList()

        trips.add(Trip(tripName = "Summer Vacation", startDate = Date().toString()))
        trips.add(Trip(tripName = "Ski Trip", startDate = Date().toString()))
        trips.add(Trip(tripName = "City Exploration", startDate = Date().toString()))
        trips.add(Trip(tripName = "Cultural Tour",  startDate = Date().toString()))
        trips.add(Trip(tripName = "Hiking Adventure",  startDate = Date().toString()))
        trips.add(Trip(tripName = "Road Trip", startDate = Date().toString()))
        trips.add(Trip(tripName = "Business Conference", startDate = Date().toString()))
        trips.add(Trip(tripName = "Beach Party",  startDate = Date().toString()))
        trips.add(Trip(tripName = "Mountain Climbing",  startDate = Date().toString()))
        trips.add(Trip(tripName = "Foodie Tour", startDate = Date().toString()))

        return trips
    }
}