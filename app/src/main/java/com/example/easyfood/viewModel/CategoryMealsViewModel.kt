package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel:ViewModel() {
    val mealsMutableLiveData=MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName:String){
         RetrofitInstance.api.getMealByCategory(categoryName).enqueue(object :Callback<MealsByCategoryList>{
             override fun onResponse(
                 call: Call<MealsByCategoryList>,
                 response: Response<MealsByCategoryList>
             ) {
                response.body()?.let {mealsList->
                    mealsMutableLiveData.postValue(mealsList.meals)
                }
             }

             override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                 Log.d("CategoryMealsViewModel",t.message.toString())
             }
         })
    }
    fun observeMealsLiveData():LiveData<List<MealsByCategory>>{
        return mealsMutableLiveData
    }
}