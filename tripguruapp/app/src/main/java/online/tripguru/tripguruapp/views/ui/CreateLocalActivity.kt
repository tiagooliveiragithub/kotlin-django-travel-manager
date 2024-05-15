package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.ActivityCreateLocalBinding

@AndroidEntryPoint
class CreateLocalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateLocalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCreateLocal.setOnClickListener {
            finish()
        }

    }
}