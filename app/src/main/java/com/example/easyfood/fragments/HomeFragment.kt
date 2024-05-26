package com.example.easyfood.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activites.CategoryMealsActivity
import com.example.easyfood.activites.MainActivity
import com.example.easyfood.activites.MealActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.MostPopularAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragments.bottomSheet.MealBottomSheetFragment
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel:HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object{
        const val CATEGORY_NAME="com.example.easyfood.fragments.categoryName"

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        homeViewModel = (activity as MainActivity).viewModel
        popularItemsAdapter=MostPopularAdapter()
        categoriesAdapter =CategoriesAdapter()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        homeViewModel.getRandomMeal()
         observerRandomMeal()

        homeViewModel.getPopularItems()
        observerPopularItemsLiveData()
        preparePopularItemsRecyclerView()


        onRandomMealClick()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observerCategoriesLiveData()

        onCategoryClick()

        onPopularItemLongClick()

        onSearchItemClick()


    }

    private fun onSearchItemClick() {
        binding.imageSearch.setOnClickListener {
             findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
         popularItemsAdapter.onLongItemClick ={meal->
             val mealBottomSheetFragment =MealBottomSheetFragment.newInstance(meal.idMeal)
             mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
         }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }

    }

    private fun prepareCategoriesRecyclerView() {

        binding.recyclerViewCategories.layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            binding.recyclerViewCategories.adapter = categoriesAdapter

    }

    private fun observerCategoriesLiveData() {
         homeViewModel.observeCategoriesMutableLiveData().observe(viewLifecycleOwner, Observer {categories->
             categoriesAdapter.setCategoryList(categories)

         })
    }

    private fun onPopularItemClick() {

        popularItemsAdapter.onItemClick = {meal->

            val intent = Intent(activity,MealActivity::class.java)

            intent.putExtra("MEAL_ID",meal.idMeal)
            intent.putExtra("MEAL_NAME",meal.strMeal)
            intent.putExtra("MEAL_THUMB",meal.strMealThumb)
                startActivity(intent)
        }

    }

    private fun preparePopularItemsRecyclerView() {
        binding.recyclerViewPopular.layoutManager= LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopular.adapter =popularItemsAdapter
    }

    private fun observerPopularItemsLiveData() {
         homeViewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
         ) {mealList->

             popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)
         }
    }

    private fun onRandomMealClick() {
         binding.randomMealImage.setOnClickListener {

             val intent = Intent(activity,MealActivity::class.java).also {

                 it.putExtra("MEAL_ID",randomMeal.idMeal)
                 it.putExtra("MEAL_NAME",randomMeal.strMeal)
                 it.putExtra("MEAL_THUMB",randomMeal.strMealThumb)
                 startActivity(it)

             }
         }
    }

    private fun observerRandomMeal() {

        homeViewModel.observeRandomMeal().observe(viewLifecycleOwner
        ) { value ->
            Glide.with(this@HomeFragment)
                .load(value!!.strMealThumb)
                .into(binding.randomMealImage)

            // this step for access the data that come from retrofit as livedata to variable we are named as randomMeal
            this@HomeFragment.randomMeal = value
        }

    }



    }
