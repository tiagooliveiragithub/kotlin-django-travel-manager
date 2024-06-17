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
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.viewmodels.MainViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.adapters.PhotoAdapter

@AndroidEntryPoint
class CreateLocalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateLocalBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val authViewModel: UserViewModel by viewModels()
    private lateinit var adapter: PhotoAdapter
    private var imageUri: Uri? = null

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPickMedia()
        observers()
    }

    private fun observers() {
        authViewModel.isOnline().observe(this) { isOnline ->
            if (!isOnline) {
                binding.buttonDeleteLocal.visibility = View.GONE
                binding.buttonCreateLocal.visibility = View.GONE
            } else {
                binding.buttonCreateLocal.visibility = View.VISIBLE
                binding.buttonDeleteLocal.visibility = View.VISIBLE
                selectedLocalObserver()
                createLocalResultObserver()
                deleteLocalResultObserver()
            }
        }
    }

    private fun selectedLocalObserver() {
        mainViewModel.getSelectedLocal().observe(this) { selectedLocal ->
            if (selectedLocal != null) {
                binding.buttonDeleteLocal.visibility = View.VISIBLE
                binding.textViewGallery.visibility = View.VISIBLE
                binding.galleryImages.visibility = View.VISIBLE
                binding.buttonAddImage.visibility = View.VISIBLE
                binding.editTextName.setText(selectedLocal.name)
                binding.editTextDescription.setText(selectedLocal.description)
                binding.buttonCreateLocal.text = getString(R.string.editlocal_button_label)
                setUpRecyclerView(selectedLocal)
                setupListenersEdit(selectedLocal)
                buttonAvatarListener()
                setupImageObserver()
            } else {
                binding.buttonDeleteLocal.visibility = View.GONE
                binding.buttonCreateLocal.text = getString(R.string.createlocal_button_label)
                setupListenersCreate()
            }
        }
    }

    private fun createLocalResultObserver() {
        mainViewModel.resultCreateLocal.observe(this) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        R.string.successlocal_label,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun deleteLocalResultObserver() {
        mainViewModel.resultDeleteLocal.observe(this) { result ->
            when (result.status) {
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        R.string.successdeletelocal_label,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupListenersCreate() {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.insertLocal(name, description)
        }
    }

    private fun setupListenersEdit(selectedLocal: Local) {
        binding.buttonCreateLocal.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            mainViewModel.updateLocal(selectedLocal.id!!, name, description, imageUri)
        }
        binding.buttonDeleteLocal.setOnClickListener {
            mainViewModel.deleteLocal(selectedLocal.id!!)
        }
    }

    private fun setUpRecyclerView(selectedLocal: Local) {
        mainViewModel.getLocalImages(selectedLocal.id!!)
        adapter = PhotoAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }

    private fun setupImageObserver() {
        mainViewModel.resultImageFetch.observe(this) { result ->
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
                    Toast.makeText(
                        this,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }

    private fun buttonAvatarListener() {
        binding.buttonAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setPickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriImageSelected ->
            imageUri = uriImageSelected
            binding.buttonAddImage.text = "Image Selected"
        }
    }

}
