package online.tripguru.tripguruapp.views.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityCreateTripBinding
import online.tripguru.tripguruapp.helpers.Resource
import online.tripguru.tripguruapp.localstorage.models.Trip
import online.tripguru.tripguruapp.viewmodels.TripViewModel
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateTripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTripBinding
    private val tripViewModel: TripViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setPickMedia()
        setupListenerImage()
    }

    private fun setupObservers() {
        setupOnlineStatusObserver()
        setupSelectedTripObserver()
        createTripResultObserver()
    }

    private fun setupOnlineStatusObserver() {
        userViewModel.isOnline().observe(this) { isOnline ->
            updateUiForOnlineStatus(isOnline)
        }
    }

    private fun updateUiForOnlineStatus(isOnline: Boolean) {

        if (!isOnline) {
            Toast.makeText(this, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
            binding.buttonCreateTrip.isEnabled = false
        } else {
            binding.buttonCreateTrip.isEnabled = true
        }
    }

    private fun setupSelectedTripObserver() {
        tripViewModel.getSelectedTrip().observe(this) { selectedTrip ->
            updateUiForSelectedTrip(selectedTrip)
        }
    }

    private fun updateUiForSelectedTrip(selectedTrip: Trip?) {
        if (selectedTrip != null) {
            binding.editTextTitle.setText(selectedTrip.name)
            binding.editTextDescription.setText(selectedTrip.description)
            setDatePickerDate(binding.startDate, selectedTrip.startDate)
            setDatePickerDate(binding.endDate, selectedTrip.endDate)
            Glide.with(this).load(selectedTrip.image).into(binding.imageView)
            binding.buttonCreateTrip.text = getString(R.string.edittrip_button_label)
            setupListenersUpdate()
        } else {
            binding.editTextTitle.text = null
            binding.editTextDescription.text = null
            binding.buttonCreateTrip.text = getString(R.string.createtrip_button_label)
            setupListenersCreate()
        }
    }

    private fun createTripResultObserver() {
        tripViewModel.resultCreateTrip.observe(this) { result ->
            handleCreateTripResult(result)
        }
    }

    private fun handleCreateTripResult(result: Resource<Trip>) {
        when (result.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.successtrip_label, Toast.LENGTH_LONG).show()
                tripViewModel.updateSelectedTrip(result.data)
                finish()
            }
            Resource.Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setupListenersCreate() {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val startDate = getDateFromDatePicker(binding.startDate)
            val endDate = getDateFromDatePicker(binding.endDate)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedStartDate = dateFormat.format(startDate)
            val formattedEndDate = dateFormat.format(endDate)

            tripViewModel.insertTrip(name, description, formattedStartDate, formattedEndDate, imageUri)
        }
    }

    private fun setupListenersUpdate() {
        binding.buttonCreateTrip.setOnClickListener {
            val name = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val startDate = getDateFromDatePicker(binding.startDate)
            val endDate = getDateFromDatePicker(binding.endDate)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedStartDate = dateFormat.format(startDate)
            val formattedEndDate = dateFormat.format(endDate)

            tripViewModel.updateTrip(name, description, formattedStartDate, formattedEndDate, imageUri)
        }
    }

    private fun setupListenerImage() {
        binding.buttonAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest())
        }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }

    private fun setDatePickerDate(datePicker: DatePicker, date: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = dateFormat.parse(date)

        val calendar = Calendar.getInstance()
        if (parsedDate != null) {
            calendar.time = parsedDate
        }

        datePicker.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun setPickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uriImageSelected ->
            imageUri = uriImageSelected
            binding.buttonAddImage.text = getString(R.string.image_selected_label)
            binding.buttonAddImage.backgroundTintList = getColorStateList(R.color.colorTextDisabled)
            Glide.with(this).load(uriImageSelected).into(binding.imageView)
        }
    }
}