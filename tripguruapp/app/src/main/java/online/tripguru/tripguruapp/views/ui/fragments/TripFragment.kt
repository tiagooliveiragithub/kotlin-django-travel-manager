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
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.viewmodels.LocalViewModel
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapter
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener
import online.tripguru.tripguruapp.views.ui.CreateLocalActivity
import online.tripguru.tripguruapp.views.ui.CreateTripActivity
import online.tripguru.tripguruapp.views.ui.MainActivity

@AndroidEntryPoint
class TripFragment : Fragment(), OnLocalClickListener {
    private lateinit var localAdapter: LocalAdapter
    private lateinit var binding: FragmentTripBinding
    private val tripViewModel: TripViewModel by activityViewModels()
    private val localViewModel: LocalViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        setObservers()
        selectedTripObserver()
    }

    private fun setObservers() {
        userViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                Toast.makeText(context, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
                disableButtons()
            } else {
                enableButtons()
                deleteTripResultObserver()
                shareTripResultObserver()
            }
        }
    }

    private fun selectedTripObserver() {
        tripViewModel.getSelectedTrip().observe(viewLifecycleOwner) { selectedTrip ->
            if (selectedTrip != null) {
                binding.textViewTripName.text = selectedTrip.name
                binding.textViewOwners.text = getString(R.string.sharedwith_label) + selectedTrip.owners
                binding.buttonEditTripPage.visibility = View.VISIBLE
                binding.buttonCreateLocal.visibility = View.VISIBLE
                binding.buttonDeleteTrip.visibility = View.VISIBLE
                binding.textViewShareTrip.visibility = View.VISIBLE
                binding.editTextShareTrip.visibility = View.VISIBLE
                binding.buttonShareTrip.visibility = View.VISIBLE
                binding.buttonRemoveShareTrip.visibility = View.VISIBLE
                binding.textViewOwners.visibility = View.VISIBLE
                binding.recyclerViewVertical.visibility = View.VISIBLE
                binding.textViewTripLocals.visibility = View.VISIBLE
                localViewModel.getAllLocalsForSelectedTrip().observe(viewLifecycleOwner) { locals ->
                    localAdapter.setLocals(locals)
                }
            } else {
                binding.textViewTripName.text = getString(R.string.notripselected_label)
                binding.buttonEditTripPage.visibility = View.GONE
                binding.buttonCreateLocal.visibility = View.GONE
                binding.buttonDeleteTrip.visibility = View.GONE
                binding.editTextShareTrip.visibility = View.GONE
                binding.textViewShareTrip.visibility = View.GONE
                binding.buttonShareTrip.visibility = View.GONE
                binding.buttonRemoveShareTrip.visibility = View.GONE
                binding.textViewOwners.visibility = View.GONE
                binding.recyclerViewVertical.visibility = View.GONE
                binding.textViewTripLocals.visibility = View.GONE
                binding.textViewTripName.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    private fun deleteTripResultObserver() {
        tripViewModel.resultDeleteTrip.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        R.string.successdeletetrip_label,
                        Toast.LENGTH_LONG
                    ).show()
                    changeToHomeFragment()
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun shareTripResultObserver() {
        tripViewModel.resultShareTrip.observe(viewLifecycleOwner) { result ->
            when (result?.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        R.string.successsharetrip_label,
                        Toast.LENGTH_LONG
                    ).show()
                    tripViewModel.updateSelectedTrip(result.data)
                    tripViewModel.resultShareTrip.postValue(null)
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun setListeners() {
        binding.buttonEditTripPage.setOnClickListener {
            startActivity(Intent(context, CreateTripActivity::class.java))
        }
        binding.buttonCreateLocal.setOnClickListener {
            localViewModel.updateSelectedLocal(null)
            startActivity(Intent(context, CreateLocalActivity::class.java))
        }
        binding.buttonDeleteTrip.setOnClickListener {
            tripViewModel.deleteTrip()
            tripViewModel.updateSelectedTrip(null)
            changeToHomeFragment()
        }
        binding.buttonShareTrip.setOnClickListener {
            val username = binding.editTextShareTrip.text.toString()
            tripViewModel.shareTrip(username)
        }
        binding.buttonRemoveShareTrip.setOnClickListener {
            val username = binding.editTextShareTrip.text.toString()
            tripViewModel.removeShareTrip(username)
        }
    }

    private fun disableButtons() {
        binding.buttonCreateLocal.isEnabled = false
        binding.buttonDeleteTrip.isEnabled = false
        binding.buttonShareTrip.isEnabled = false
        binding.buttonRemoveShareTrip.isEnabled = false
    }

    private fun enableButtons() {
        binding.buttonCreateLocal.isEnabled = true
        binding.buttonDeleteTrip.isEnabled = true
        binding.buttonShareTrip.isEnabled = true
        binding.buttonRemoveShareTrip.isEnabled = true
    }

    override fun onLocalClick(local: Local) {
        localViewModel.updateSelectedLocal(local)
        startActivity(Intent(context, CreateLocalActivity::class.java))
    }

    private fun changeToHomeFragment() {
        (activity as MainActivity).replaceFragment(HomeFragment())
    }

    private fun setRecyclerView() {
        val recyclerView = binding.recyclerViewVertical
        recyclerView.layoutManager = LinearLayoutManager(context)
        localAdapter = LocalAdapter(this)
        recyclerView.adapter = localAdapter
    }

}