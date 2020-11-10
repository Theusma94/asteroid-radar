package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,
                MainViewModel.Factory(
                        requireContext().getString(R.string.API_KEY),
                        requireActivity().application
                )
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val adapter = AsteroidsAdapter {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        }
        binding.asteroidRecycler.adapter = adapter
        setHasOptionsMenu(true)

        viewModel.resultAsteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @ExperimentalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_all_menu -> {
                viewModel.getWeekAsteroids()
            }
            R.id.show_rent_menu -> {
                viewModel.getTodayAsteroids()
            }
            R.id.show_buy_menu -> {
                viewModel.getAllAsteroids()
            }
        }
        return true
    }
}
