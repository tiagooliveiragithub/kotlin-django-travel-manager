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
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.helpers.TripViewModelFactory
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.repositories.TripRepository
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.views.adapters.TripAdapter2
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity


class TripFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var tripAdapter: TripAdapter2? = null
    private lateinit var binding: FragmentTripBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(inflater, container, false)

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
        recyclerView = binding.recyclerViewVertical
        tripAdapter = TripAdapter2(emptyList()) // Initialize the adapter with an empty list
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = tripAdapter

        binding.buttonCreateLocal.setOnClickListener {
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
    }
}