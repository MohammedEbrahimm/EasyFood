package com.example.easyfood.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapters.CategoryMealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer {mealsList->
            binding.tvCategoryCount.text=mealsList.size.toString()
            categoryMealsAdapter.setMealsList(mealsList)


        })

    }

    private fun prepareRecyclerView() {

        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter =categoryMealsAdapter
        }
    }
}