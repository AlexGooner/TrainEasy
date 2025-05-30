package com.citrus.cookiteasy.presentation.traininglist

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Training
import com.citrus.cookiteasy.presentation.ColorsFromRes
import com.google.android.material.button.MaterialButton

class TrainingListAdapter(private val trains: List<Training>, val navController: NavController) :
    RecyclerView.Adapter<TrainingListAdapter.TrainListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrainListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_train, parent, false)
        return TrainListViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(
        holder: TrainListViewHolder,
        position: Int
    ) {
        val train = trains[position]
        holder.nameTV.text = train.title
        holder.descriptionTV.text = train.description



        if (train.difficulty.toInt() <= 2) {
            holder.easyBtn.setBackgroundColor(ColorsFromRes.green)
        } else if (train.difficulty.toInt() > 2 && train.difficulty.toInt() <= 4) {
            holder.easyBtn.setBackgroundColor(ColorsFromRes.yellow)
            holder.mediumBtn.setBackgroundColor(ColorsFromRes.yellow)
        } else {
            holder.easyBtn.setBackgroundColor(ColorsFromRes.red)
            holder.mediumBtn.setBackgroundColor(ColorsFromRes.red)
            holder.hardBtn.setBackgroundColor(ColorsFromRes.red)
        }

        holder.itemView.setOnClickListener {
            val action = TrainingListFragmentDirections.actionTrainListToCertainTrain(
                trainingId = train.id,
                difficulty = train.difficulty
            )
            navController.navigate(action)
        }


    }

    override fun getItemCount() = trains.size


    class TrainListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.train_title)
        val descriptionTV: TextView = itemView.findViewById(R.id.train_description_text_view)
        val easyBtn: MaterialButton = itemView.findViewById(R.id.difficulty_first_btn)
        val mediumBtn: MaterialButton = itemView.findViewById(R.id.difficulty_second_btn)
        val hardBtn: MaterialButton = itemView.findViewById(R.id.difficulty_third_btn)

    }
}