package com.example.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.activites.MainActivity
import com.example.easyfood.adapters.MealsAdapterForFavoritesAndSearch
import com.example.easyfood.databinding.FragmentFavoritesBinding
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var favAdapter:MealsAdapterForFavoritesAndSearch
    private lateinit var binding: FragmentFavoritesBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel=(activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavoritesMeals()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) =true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMail = favAdapter.differ.currentList[position]
                homeViewModel.deleteMeal(deletedMail)

                 Snackbar.make(requireView(), " Meal Deleted ",Snackbar.LENGTH_LONG ).setAction(
                     "Undo"
                 ) {
                     homeViewModel.insertMeal(deletedMail)
                 }.show()


            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavMeals)
            


    }

    private fun prepareRecyclerView() {
        favAdapter= MealsAdapterForFavoritesAndSearch()
        binding.rvFavMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favAdapter
        }

    }

    private fun observeFavoritesMeals() {
         homeViewModel.observeFavoritesMeals().observe(requireActivity(), Observer{
             favAdapter.differ.submitList(it)
         })
    }
}