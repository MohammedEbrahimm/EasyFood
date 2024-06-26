package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.Category
import com.example.easyfood.pojo.CategoryList
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.pojo.MealsByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.pojo.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {

    private var randomMealMutableLiveData = MutableLiveData<Meal>()
    private var popularItemsMutableLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesMutableLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private val searchMealsLiveData = MutableLiveData<List<Meal>>()


    private var saveStateRandomMeal: Meal ?=null


    fun observeFavoritesMeals():LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }
    fun observeRandomMeal():LiveData<Meal> = randomMealMutableLiveData
    fun getRandomMeal(){
        saveStateRandomMeal?.let {
            randomMealMutableLiveData.postValue(it)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
               if (response.body() != null){
                   val randomMeal:Meal = response.body()!!.meals[0]
                   randomMealMutableLiveData.value = randomMeal
                   saveStateRandomMeal = randomMeal
               }else
                   return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })


    }
    fun getPopularItems(){

        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {

                if (response.body()!=null){
                    popularItemsMutableLiveData.value = response.body()!!.meals


                }else
                    return
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {

                Log.d("HomeFragment",t.message.toString())
            }

        })

    }

    fun observeBottomSheetMealLiveData():LiveData<Meal> {
        return bottomSheetMealLiveData
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {meal ->
                    bottomSheetMealLiveData.value =meal
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }

        })
    }



    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsMutableLiveData
    }
    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                 response.body()?.let {categoryList ->
                     categoriesMutableLiveData.postValue(categoryList.categories)

                 }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {

                Log.d("HomeViewModel",t.message.toString())
            }
        })
    }
    fun observeCategoriesMutableLiveData():LiveData<List<Category>>{
        return categoriesMutableLiveData
    }
    fun insertMeal(meal:Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }


    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun searchMeal(searchQuery:String){
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }


        })
    }
    fun observeSearchedMealsLiveData():LiveData<List<Meal>> =searchMealsLiveData


}