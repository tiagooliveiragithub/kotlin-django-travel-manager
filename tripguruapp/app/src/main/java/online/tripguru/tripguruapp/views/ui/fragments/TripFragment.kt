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
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.FragmentTripBinding
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapterVertical
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity

@AndroidEntryPoint
class TripFragment : Fragment(), OnLocalClickListener {
    private lateinit var localAdapter: LocalAdapterVertical
    private lateinit var binding: FragmentTripBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val authViewModel: UserViewModel by activityViewModels()

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
        listeners()
        observers()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewVertical
        recyclerView.layoutManager = LinearLayoutManager(context)
        localAdapter = LocalAdapterVertical(this)
        recyclerView.adapter = localAdapter
    }

    private fun observers() {
        authViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                disableButtons()
            } else {
                mainViewModel.resultAllDataFetch.observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            disableButtons()
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            enableButtons()
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            disableButtons()
                        }
                        Resource.Status.FIELDS -> {
                            Toast.makeText(context, getString(result.fields!!), Toast.LENGTH_SHORT)
                                .show()
                            enableButtons()
                        }
                    }
                }
                mainViewModel.resultDeleteTrip.observe(viewLifecycleOwner) { result ->
                    if (result.status == Resource.Status.SUCCESS) {
                        Toast.makeText(
                            requireContext(),
                            "Trip deleted successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        mainViewModel.getSelectedTrip().observe(viewLifecycleOwner)  { selectedTrip ->
            if (selectedTrip != null) {
                setPageSelectedTripLayout(selectedTrip)
                mainViewModel.getAllLocalsforSelectedTrip().observe(viewLifecycleOwner) { trips ->
                    localAdapter.setLocals(trips)
                }
            } else {
                setPageNoTripSelectedLayout()
            }
        }
    }

    private fun listeners() {
        binding.buttonEditTripPage.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
        binding.buttonCreateLocalPage.setOnClickListener {
            mainViewModel.updateSelectedLocal(null)
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
        binding.buttonDeleteTrip.setOnClickListener {
            mainViewModel.deleteTrip(mainViewModel.getSelectedTrip().value?.id!!)
            changeToHomeFragment()
        }
    }

    override fun onLocalClick(local: Local) {
        mainViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }

    private fun changeToHomeFragment() {
        (activity as MainActivity).replaceFragment(HomeFragment())
    }

    private fun setPageSelectedTripLayout(trip : Trip) {
        binding.textViewTripName.text = trip.name
        binding.buttonEditTripPage.visibility = View.VISIBLE
        binding.buttonCreateLocalPage.visibility = View.VISIBLE
        binding.buttonDeleteTrip.visibility = View.VISIBLE
        binding.recyclerViewVertical.visibility = View.VISIBLE
        binding.textViewTripLocals.visibility = View.VISIBLE
    }

    private fun setPageNoTripSelectedLayout() {
        binding.textViewTripName.text = getString(R.string.notripselected_label)
        binding.buttonEditTripPage.visibility = View.GONE
        binding.buttonCreateLocalPage.visibility = View.GONE
        binding.buttonDeleteTrip.visibility = View.GONE
        binding.recyclerViewVertical.visibility = View.GONE
        binding.textViewTripLocals.visibility = View.GONE
        binding.textViewTripName.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun disableButtons() {
        binding.buttonEditTripPage.isEnabled = false
        binding.buttonCreateLocalPage.isEnabled = false
        binding.buttonDeleteTrip.isEnabled = false
    }

    private fun enableButtons() {
        binding.buttonEditTripPage.isEnabled = true
        binding.buttonCreateLocalPage.isEnabled = true
        binding.buttonDeleteTrip.isEnabled = true
    }
}