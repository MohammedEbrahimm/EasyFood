package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.easyfood.pojo.Meal

@Dao
interface MealDao {

   /* @Insert
    suspend fun insertMeal(meal: Meal)

    @Update
    suspend fun update(meal: Meal)*/
    // we can use this two functions in one function as following
    @Insert(onConflict = OnConflictStrategy.REPLACE) // if you try to add a meal is already exists the room is updated the table  by this command instead of add new meal
    // so we will command this two functions and keep the upsert only
   suspend fun upsert(meal: Meal)

   @Delete()
   suspend fun delete(meal:Meal)

   @Query("SELECT * FROM mealInformation")
   fun getAllMeals():LiveData<List<Meal>>
}