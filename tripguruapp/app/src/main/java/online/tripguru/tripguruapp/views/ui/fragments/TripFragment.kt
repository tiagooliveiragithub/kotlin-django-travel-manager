package online.tripguru.tripguruapp.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.ConnectivityLiveData
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapterVertical
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import javax.inject.Inject

@AndroidEntryPoint
class TripFragment : Fragment(), OnLocalClickListener {
    private lateinit var localAdapter: LocalAdapterVertical
    private lateinit var binding: FragmentTripBinding
    private val tripViewModel: TripViewModel by activityViewModels()
    @Inject lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageSetup()

        setUpRecyclerView()

        buttonEditTripListener()
        buttonCreateLocalPageListener()

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
        connectivityLiveData.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                tripViewModel.refreshAllTrips()
                binding.buttonEditTripPage.isEnabled = true
                binding.buttonCreateLocalPage.isEnabled = true
                binding.buttonDeleteTrip.isEnabled = true
            } else {
                Toast.makeText(requireContext(), "Disconnected from the internet", Toast.LENGTH_LONG).show()
                binding.buttonEditTripPage.isEnabled = false
                binding.buttonCreateLocalPage.isEnabled = false
                binding.buttonDeleteTrip.isEnabled = false
            }
        })
    }

    private fun buttonCreateLocalPageListener() {
        binding.buttonCreateLocalPage.setOnClickListener {
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
    }

    private fun buttonEditTripListener() {
        binding.buttonEditTripPage.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
    }

    private fun pageSetup() {
        val trip = tripViewModel.getSelectedTrip()
        if (trip != null) {
            binding.textViewTripName.text = trip.tripName
        }
    }
}