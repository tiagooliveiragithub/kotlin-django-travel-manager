package online.tripguru.tripguruapp.views.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.databinding.ActivityMainBinding
import online.tripguru.tripguruapp.views.ui.fragments.HomeFragment
import online.tripguru.tripguruapp.views.ui.fragments.ProfileFragment
import online.tripguru.tripguruapp.views.ui.fragments.SearchFragment
import online.tripguru.tripguruapp.views.ui.fragments.TripFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(HomeFragment())
        buttonNavigationListener()

    }

    private fun replaceFragment(fragment: Fragment) {
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