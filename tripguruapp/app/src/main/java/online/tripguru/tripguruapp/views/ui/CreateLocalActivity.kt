package online.tripguru.tripguruapp.views.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateLocalBinding
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.LocalImageResponse
import online.tripguru.tripguruapp.network.LocalResponse
import online.tripguru.tripguruapp.viewmodels.LocalViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.PhotoAdapter
import org.osmdroid.api.IMapController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class CreateLocalActivity : AppCompatActivity() {

    private val localViewModel: LocalViewModel by viewModels()
    private val authViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityCreateLocalBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var adapter: PhotoAdapter
    private lateinit var mapView: MapView
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        setMap()
        setPickMedia()
        setupObservers()
        setupButtonsListeners()
    }

    private fun setupObservers() {
        authViewModel.isOnline().observe(this) { isOnline ->
            updateUiForOnlineStatus(isOnline)
        }
        localViewModel.getSelectedLocal().observe(this) { selectedLocal ->
            updateUiForSelectedLocal(selectedLocal)
        }
        localViewModel.resultCreateLocal.observe(this) { result ->
            handleCreateLocalResult(result)
        }
        localViewModel.resultDeleteLocal.observe(this) { result ->
            handleDeleteLocalResult(result)
        }
        localViewModel.resultImageFetch.observe(this) { result ->
            handleImageFetchResult(result)
        }
        localViewModel.currentLocation.observe(this) { location ->
            updateMapWithLocation(location?.latitude, location?.longitude)
        }
        localViewModel.currentAddress.observe(this) { address ->
            binding.textViewAddress.text = address
        }
    }

    private fun updateUiForOnlineStatus(isOnline: Boolean) {
        binding.buttonDeleteLocal.visibility = if (isOnline) View.VISIBLE else View.GONE
        binding.buttonCreateLocal.visibility = if (isOnline) View.VISIBLE else View.GONE
    }

    private fun updateUiForSelectedLocal(selectedLocal: Local?) {
        if (selectedLocal != null) {
            binding.buttonDeleteLocal.visibility = View.VISIBLE
            binding.editTextName.setText(selectedLocal.name)
            binding.editTextDescription.setText(selectedLocal.description)
            binding.textViewAddress.text = selectedLocal.address
            binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
            localViewModel.getLocalImages(selectedLocal.id!!)
            setupListenersEdit(selectedLocal)
            updateMapWithLocation(selectedLocal.latitude, selectedLocal.longitude)
        } else {
            binding.buttonDeleteLocal.visibility = View.GONE
            binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
            setupListenersCreate()
        }
    }

    private fun handleCreateLocalResult(result: Resource<LocalResponse>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.successlocal_label, Toast.LENGTH_LONG).show()
                finish()
            }
            Resource.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleDeleteLocalResult(result: Resource<LocalResponse>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.successdeletelocal_label, Toast.LENGTH_LONG).show()
                finish()
            }
            else -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleImageFetchResult(result: Resource<List<LocalImageResponse>>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                adapter.setPhotoUrls(result.data?.map { it.image })
            }
            Resource.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupListenersCreate() {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            localViewModel.insertLocal(name, description)
        }
    }

    private fun setupListenersEdit(selectedLocal: Local) {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            localViewModel.updateLocal(selectedLocal.id!!, name, description, imageUri)
        }
        binding.buttonDeleteLocal.setOnClickListener {
            localViewModel.deleteLocal(selectedLocal.id!!)
        }
    }

    private fun setupButtonsListeners() {
        binding.buttonAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.buttonAddLocation.setOnClickListener {
            localViewModel.fetchCurrentLocation()
        }
    }

    private fun setUpRecyclerView() {
        adapter = PhotoAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }

    private fun setPickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriImageSelected ->
            imageUri = uriImageSelected
            binding.buttonAddImage.text = getString(R.string.image_selected_label)
        }
    }

    private fun updateMapWithLocation(latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return

        val startPoint = GeoPoint(latitude, longitude)
        val mapController: IMapController = mapView.controller
        mapController.setCenter(startPoint)

        val startMarker = Marker(mapView)
        startMarker.position = startPoint
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.clear()
        mapView.overlays.add(startMarker)
        mapView.invalidate()
    }

    private fun setMap() {
        mapView = binding.map
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(20.0)
    }
}
