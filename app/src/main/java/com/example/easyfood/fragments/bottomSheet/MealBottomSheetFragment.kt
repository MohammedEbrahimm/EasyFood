package com.example.easyfood.fragments.bottomSheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activites.MainActivity
import com.example.easyfood.activites.MealActivity
import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val ARG_PARAM1 = "param1"



class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMealBottomSheetBinding
    private lateinit var homeViewModel: HomeViewModel
    private var MEAL_ID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            MEAL_ID = it.getString(ARG_PARAM1)

        }

        homeViewModel = (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MEAL_ID?.let {mealId->
            homeViewModel.getMealById(mealId)
        }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()


    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {

            if (mealName != null && mealThumb != null){
              val intent = Intent(activity,MealActivity::class.java)
                intent.apply {
                    putExtra("MEAL_ID",MEAL_ID)
                    putExtra("MEAL_NAME",mealName)
                    putExtra("MEAL_THUMB",mealThumb)
                }
                startActivity(intent)

            }

        }
    }

    private var mealName:String?=null
    private var mealThumb:String?=null


    private fun observeBottomSheetMeal() {

        homeViewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner, Observer {meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.mealSheetImgView)
            binding.tvMealSheetArea.text = meal.strArea
            binding.tvMealSheetCategory.text= meal.strCategory
            binding.tvBottomMealSheetName.text= meal.strMeal

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }


    companion object {

        @JvmStatic fun newInstance(param1: String ) =
                MealBottomSheetFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                       
                    }
                }
    }
}