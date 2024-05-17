package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.FragmentHomeBinding
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.OnTripClickListener
import online.tripguru.tripguruapp.views.adapters.TripAdapter
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity


@AndroidEntryPoint
class HomeFragment : Fragment(), OnTripClickListener {

    private val tripViewModel: TripViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
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

        setRecyclerView()
        buttonCreateTripListener()

        observer()
    }

    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapterTrip = TripAdapter(this)
        recyclerView.adapter = adapterTrip
    }

    private fun buttonCreateTripListener() {
        binding.buttonCreateTrip.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
    }

    private fun observer() {
        tripViewModel.allTrips.observe(viewLifecycleOwner) { trips ->
            trips?.let {
                adapterTrip.setTrips(it)
            }
        }
    }

    override fun onTripClick(trip: Trip) {
        tripViewModel.setSelectedTrip(trip)
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, TripFragment())
            .commit()
        (activity as MainActivity).binding.bottomNavigation.selectedItemId = R.id.icon_trip

    }

}