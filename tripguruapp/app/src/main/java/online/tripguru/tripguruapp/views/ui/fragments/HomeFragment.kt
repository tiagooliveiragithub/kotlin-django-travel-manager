package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.FragmentHomeBinding
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.TripAdapter
import online.tripguru.tripguruapp.views.ui.CreateTripActivity


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val tripViewModel: TripViewModel by viewModels()
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

        val recyclerViewTrip = binding.recyclerView
        recyclerViewTrip.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapterTrip = TripAdapter()
        recyclerViewTrip.adapter = adapterTrip

        buttonCreateTripListener()
        observer()

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
}