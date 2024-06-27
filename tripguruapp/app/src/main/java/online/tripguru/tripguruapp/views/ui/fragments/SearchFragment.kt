package online.tripguru.tripguruapp.views.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.FragmentSearchBinding
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.viewmodels.LocalViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.LocalAdapter
import online.tripguru.tripguruapp.views.adapters.OnLocalClickListener

@AndroidEntryPoint
class SearchFragment : Fragment(), OnLocalClickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapterLocal: LocalAdapter
    private val userViewModel: UserViewModel by activityViewModels()
    private val localViewModel: LocalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setRecyclerView()
        localViewModel.getAllLocals()
    }

    private fun setObservers() {
        userViewModel.isOnline().observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                Toast.makeText(context, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
            } else {
                localViewModel.resultGetAllLocals.observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Resource.Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            adapterLocal.setLocals(result.data ?: emptyList())
                        }
                        Resource.Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        val recyclerView = binding.recyclerViewVertical
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapterLocal = LocalAdapter(this)
        recyclerView.adapter = adapterLocal
    }

    override fun onLocalClick(local: Local) {
//        localViewModel.updateSelectedLocal(local)
//        startActivity(Intent(context, CreateLocalActivity::class.java). also { intent ->
//            intent.putExtra("view", true)})
    }
}