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
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.network.ImageResponse
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
    private val userViewModel: UserViewModel by viewModels()
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
        userViewModel.isOnline().observe(this) { isOnline ->
            updateUiForOnlineStatus(isOnline)
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

        if(!isOnline) {
            Toast.makeText(this, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
        }

        localViewModel.getSelectedLocal().observe(this) { selectedLocal ->
            if (selectedLocal != null) {
                if (isOnline) {
                    binding.buttonDeleteLocal.visibility = View.VISIBLE
                    binding.buttonCreateLocal.visibility = View.VISIBLE
                } else {
                    binding.buttonDeleteLocal.visibility = View.GONE
                    binding.buttonCreateLocal.visibility = View.GONE
                }
                binding.editTextName.setText(selectedLocal.name)
                binding.editTextDescription.setText(selectedLocal.description)
                binding.textViewAddress.text = selectedLocal.address
                adapter.setPhotoItems(selectedLocal.images)
                binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
                setupListenersEdit()
                updateMapWithLocation(selectedLocal.latitude, selectedLocal.longitude)
            } else {
                if (isOnline) {
                    binding.buttonCreateLocal.visibility = View.VISIBLE
                } else {
                    binding.buttonDeleteLocal.visibility = View.GONE
                    binding.buttonCreateLocal.visibility = View.GONE
                }
                binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
                setupListenersCreate()
            }
        }

    }

    private fun handleCreateLocalResult(result: Resource<Local>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.successlocal_label, Toast.LENGTH_LONG).show()
                localViewModel.updateSelectedLocal(result.data)
                finish()
            }
            Resource.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleDeleteLocalResult(result: Resource<Local>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.successdeletelocal_label, Toast.LENGTH_LONG).show()
                localViewModel.updateSelectedLocal(null)
                finish()
            }
            else -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleImageFetchResult(result: Resource<List<ImageResponse>>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                adapter.setPhotoItems(result.data?.map { it.image })
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
            localViewModel.insertLocal(name, description, imageUri)
        }
    }

    private fun setupListenersEdit() {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            localViewModel.updateLocal(name, description, imageUri)
        }
        binding.buttonDeleteLocal.setOnClickListener {
            localViewModel.deleteLocal()
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
            binding.buttonAddImage.backgroundTintList = getColorStateList(R.color.colorTextDisabled)
            adapter.addPhotoItem(uriImageSelected!!)
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
