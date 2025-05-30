package com.citrus.cookiteasy.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Goal

class GoalAdapter(private val goals: List<Goal>, val navController : NavController) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GoalViewHolder,
        position: Int
    ) {
        val goal = goals[position]
        holder.nameTextView.text = goal.name
        holder.iconImageView.setImageResource(goal.iconResId)


        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToTrainList(goal.name)
            navController.navigate(action)
        }
    }

    override fun getItemCount() = goals.size

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.category_title)
        val iconImageView: ImageView = itemView.findViewById(R.id.category_background)
    }

}