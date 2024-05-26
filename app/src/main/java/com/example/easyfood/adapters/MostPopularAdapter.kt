package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.PopularItemLayoutBinding
import com.example.easyfood.pojo.MealsByCategory

class MostPopularAdapter:RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {


    lateinit var onItemClick:((MealsByCategory)->Unit)
     var onLongItemClick:((MealsByCategory)->Unit)?=null


    private var mealList = ArrayList<MealsByCategory>()

            fun setMeals(mealsList:ArrayList<MealsByCategory>){
                this.mealList=mealsList
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return mealList.size

    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])

        }

        holder.itemView.setOnLongClickListener{
            onLongItemClick?.invoke(mealList[position])
            true
        }

    }
    class PopularMealViewHolder(  val binding:PopularItemLayoutBinding):RecyclerView.ViewHolder(binding.root)
}