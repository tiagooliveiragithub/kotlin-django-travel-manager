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
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.TripAdapterVertical
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity

@AndroidEntryPoint
class TripFragment : Fragment() {
    private lateinit var adapterTrip: TripAdapterVertical
    private lateinit var binding: FragmentTripBinding
    private val tripViewModel: TripViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewTrip = binding.recyclerViewVertical
        recyclerViewTrip.layoutManager = LinearLayoutManager(context)
        adapterTrip = TripAdapterVertical()
        recyclerViewTrip.adapter = adapterTrip

        buttonCreateLocalPageListener()

        observer()

    }

    private fun buttonCreateLocalPageListener() {
        binding.buttonCreateLocalPage.setOnClickListener {
            startActivity(Intent(context, CreateLocalActivity::class.java))
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