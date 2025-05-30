package com.citrus.cookiteasy.presentation.certaintrain

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Exercise
import org.w3c.dom.Text

class ExerciseAdapter(private val exercises: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.exercise_title)
        val expandIcon: ImageView = itemView.findViewById(R.id.expanded_btn)
        val expandedContent : LinearLayout = itemView.findViewById(R.id.expanded_content)
        val image : ImageView = itemView.findViewById(R.id.exercise_image)
        val description: TextView = itemView.findViewById(R.id.exercise_description)
        val sets: TextView = itemView.findViewById(R.id.sets_text_view)
        val reps : TextView = itemView.findViewById(R.id.reps_text_view)
        val duration : TextView = itemView.findViewById(R.id.duration_text_view)
        val rest : TextView = itemView.findViewById(R.id.rest_after_text_view)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (expandedPositions.contains(position)) {
                        expandedPositions.remove(position)
                    } else {
                        expandedPositions.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.name.text = exercise.title
        holder.description.text = exercise.description
         Glide.with(holder.itemView.context).load(exercise.imageUrl).into(holder.image)
        holder.sets.text = "Подходов: ${exercise.sets}"
        holder.reps.text = "Повторений: ${exercise.reps}"

        if (exercise.durationSec == 0) {
            holder.duration.text = "Продолжительность: ${exercise.durationSec} сек"
        } else {
            holder.duration.visibility = View.GONE
        }

        holder.rest.text = "Отдых между подходами: ${exercise.restAfterSec} сек"

        val isExpanded = expandedPositions.contains(position)
        holder.expandedContent.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.expandIcon.setImageResource(
            if (isExpanded) R.drawable.ic_expanded_less else R.drawable.ic_expand_more
        )
    }

    override fun getItemCount() = exercises.size
}
