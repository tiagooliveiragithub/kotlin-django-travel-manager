package online.tripguru.tripguruapp.views.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import online.tripguru.tripguruapp.databinding.FragmentSearchBinding
import online.tripguru.tripguruapp.viewmodels.MainViewModel

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


}