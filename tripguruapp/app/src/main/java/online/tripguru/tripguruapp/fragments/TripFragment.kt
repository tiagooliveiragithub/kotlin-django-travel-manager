package online.tripguru.tripguruapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.CreateLocalActivity
import online.tripguru.tripguruapp.adapters.Trip
import online.tripguru.tripguruapp.adapters.TripAdapter2
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import java.util.Date


class TripFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var tripAdapter: TripAdapter2? = null
    private var tripList: List<Trip>? = null

    private lateinit var binding: FragmentTripBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerViewVertical
        tripList = generateFakeTrips()
        tripAdapter = TripAdapter2(tripList!!)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = tripAdapter

        binding.buttonCreateLocal.setOnClickListener {
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }

    }

    private fun generateFakeTrips(): List<Trip> {
        val trips: MutableList<Trip> = ArrayList()

        trips.add(Trip("Summer Vacation",  Date().toString()))
        trips.add(Trip("Ski Trip",  Date().toString()))
        trips.add(Trip("City Exploration",  Date().toString()))
        trips.add(Trip("Cultural Tour", Date().toString()))
        trips.add(Trip("Hiking Adventure",  Date().toString()))
        trips.add(Trip("Road Trip",  Date().toString()))
        trips.add(Trip("Business Conference", Date().toString()))
        trips.add(Trip("Beach Party",  Date().toString()))
        trips.add(Trip("Mountain Climbing",  Date().toString()))
        trips.add(Trip("Foodie Tour",  Date().toString()))

        return trips
    }
}