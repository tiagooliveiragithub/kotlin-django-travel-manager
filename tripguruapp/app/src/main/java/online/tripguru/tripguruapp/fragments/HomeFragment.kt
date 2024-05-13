package online.tripguru.tripguruapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.adapters.Trip
import online.tripguru.tripguruapp.adapters.TripAdapter
import java.util.Date


class HomeFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var tripAdapter: TripAdapter? = null
    private var tripList: List<Trip>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(online.tripguru.tripguruapp.R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(online.tripguru.tripguruapp.R.id.recyclerView)
        tripList = generateFakeTrips()
        tripAdapter = TripAdapter(tripList!!)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.adapter = tripAdapter
    }

    private fun generateFakeTrips(): List<Trip> {
        val trips: MutableList<Trip> = ArrayList()

        trips.add(Trip("Summer Vacation",  Date().toString()))
        trips.add(Trip("Ski Trip", Date().toString()))
        trips.add(Trip("City Exploration", Date().toString()))
        trips.add(Trip("Cultural Tour",  Date().toString()))
        trips.add(Trip("Hiking Adventure",  Date().toString()))
        trips.add(Trip("Road Trip", Date().toString()))
        trips.add(Trip("Business Conference", Date().toString()))
        trips.add(Trip("Beach Party",  Date().toString()))
        trips.add(Trip("Mountain Climbing",  Date().toString()))
        trips.add(Trip("Foodie Tour", Date().toString()))

        return trips
    }
}