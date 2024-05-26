package com.example.easyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easyfood.R
import com.example.easyfood.activites.MainActivity
import com.example.easyfood.adapters.MealsAdapterForFavoritesAndSearch
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var serachRecyclerViewAdapter:MealsAdapterForFavoritesAndSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.imageSearchArrow.setOnClickListener {
            searchMeals()
        }
        observeSearchedMealLiveData()

        var searchJob:Job?=null
        binding.edSearchBox.addTextChangedListener {searchQuery->
            searchJob?.cancel()
            searchJob =lifecycleScope.launch {
                delay(500)
                homeViewModel.searchMeal(searchQuery.toString())

            }
        }
    }

    private fun observeSearchedMealLiveData() {
         homeViewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner, Observer { mealsList ->
             serachRecyclerViewAdapter.differ.submitList(mealsList)

         })
    }

    private fun searchMeals() {
         val searchQuery = binding.edSearchBox.text.toString()

        if (searchQuery.isNotEmpty()){
            homeViewModel.searchMeal(searchQuery)

        }


    }

    private fun prepareRecyclerView() {
         serachRecyclerViewAdapter = MealsAdapterForFavoritesAndSearch()
        binding.rvSearchedMeal.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter =serachRecyclerViewAdapter
        }
    }
}