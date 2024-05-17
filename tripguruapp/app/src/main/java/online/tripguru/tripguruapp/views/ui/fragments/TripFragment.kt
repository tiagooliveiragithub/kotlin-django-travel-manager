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
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapterVertical
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity

@AndroidEntryPoint
class TripFragment : Fragment(), OnLocalClickListener {
    private lateinit var localAdapter: LocalAdapterVertical
    private lateinit var binding: FragmentTripBinding
    private val tripViewModel: TripViewModel by activityViewModels()
    private var trip : Trip? = null

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
        buttonCreateLocalPageListener()

        pageSetup()
        observer()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewVertical
        recyclerView.layoutManager = LinearLayoutManager(context)
        localAdapter = LocalAdapterVertical(this)
        recyclerView.adapter = localAdapter
    }

    override fun onLocalClick(local: Local) {
        tripViewModel.setSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }

    private fun observer() {
        tripViewModel.getLocalsForTrip().observe(viewLifecycleOwner) { locals ->
            locals?.let {
                localAdapter.setLocals(it)
            }
        }
    }

    private fun buttonCreateLocalPageListener() {
        binding.buttonCreateLocalPage.setOnClickListener {
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
    }

    private fun pageSetup() {
        // get the first trip
        if (tripViewModel.getSelectedTrip() == null) {
            trip = tripViewModel.allTrips.value?.get(0)
        } else trip = tripViewModel.getSelectedTrip()

        binding.textViewTripName.text = trip?.tripName
    }

}