package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityMainBinding
import online.tripguru.tripguruapp.viewmodels.UserViewModel
import online.tripguru.tripguruapp.views.ui.fragments.HomeFragment
import online.tripguru.tripguruapp.views.ui.fragments.ProfileFragment
import online.tripguru.tripguruapp.views.ui.fragments.SearchFragment
import online.tripguru.tripguruapp.views.ui.fragments.TripFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        buttonNavigationListener()
        observers()
    }

    private fun observers() {
        userViewModel.isOnline().observe(this) { isConnected ->
            if (!isConnected) {
                Toast.makeText(this, getString(R.string.nointernet_label), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.contentFrame.id, fragment)
            commit()
        }
    }

    private fun buttonNavigationListener() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.icon_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.icon_trip -> {
                    replaceFragment(TripFragment())
                    true
                }
                R.id.icon_user -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.icon_search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                else -> false
            }
        }
    }
}