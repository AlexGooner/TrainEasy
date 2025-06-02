package com.citrus.cookiteasy.presentation.certaintrain

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Exercise
import com.citrus.cookiteasy.databinding.ItemExerciseBinding
import org.w3c.dom.Text

class ExerciseAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()
    private var lastExpandedPosition = -1

    inner class ExerciseViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(exercise: Exercise, isExpanded: Boolean) {
            with(binding) {
                exerciseTitle.text = exercise.title
                exerciseDescription.text = exercise.description
                setsTextView.text = "Подходов: ${exercise.sets}"
                repsTextView.text = "Повторений: ${exercise.reps}"
                restAfterTextView.text = "Отдых: ${exercise.restAfterSec} сек"

                exercise.durationSec?.let {
                    if (it > 0) {
                        durationTextView.text = "Длительность: ${exercise.durationSec} сек"
                        durationTextView.visibility = View.VISIBLE
                    } else {
                        durationTextView.visibility = View.GONE
                    }
                } ?: run {
                    durationTextView.visibility = View.GONE
                }

                Glide.with(itemView.context)
                    .load(exercise.imageUrl)
                    .downsample(DownsampleStrategy.CENTER_INSIDE) // Оптимальное масштабирование
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(800, 800)
                    .override(exerciseImage.width, exerciseImage.height) // Под размеры ImageView
                    .into(exerciseImage)

                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        toggleExpansion(position)
                    }
                }

                if (isExpanded) {
                    expandWithAnimation()
                } else {
                    collapseWithAnimation()
                }
            }
        }

        private fun toggleExpansion(position: Int) {
            val wasExpanded = expandedPositions.contains(position)
            if (wasExpanded) {
                expandedPositions.remove(position)
                collapseWithAnimation()
            } else {
                expandedPositions.add(position)
                expandWithAnimation()
                (itemView.parent as? RecyclerView)?.smoothScrollToPosition(position)
            }
            animateArrowIcon(!wasExpanded)
        }

        private fun expandWithAnimation() {
            TransitionManager.beginDelayedTransition(
                itemView as ViewGroup,
                TransitionSet()
                    .addTransition(Fade(Fade.IN))
                    .setDuration(1000)
                    .setInterpolator(FastOutSlowInInterpolator())
            )
            binding.expandedContent.visibility = View.VISIBLE
        }

        private fun collapseWithAnimation() {
            TransitionManager.beginDelayedTransition(
                itemView as ViewGroup,
                TransitionSet()
                    .addTransition(Fade(Fade.OUT))
                    .setDuration(1000)
                    .setInterpolator(FastOutSlowInInterpolator())
            )
            binding.expandedContent.visibility = View.GONE
        }

        private fun animateArrowIcon(expanded: Boolean) {
            ObjectAnimator.ofFloat(
                binding.expandedBtn,
                "rotation",
                if (expanded) 0f else 180f,
                if (expanded) 180f else 0f
            ).apply {
                duration = 300
                interpolator = FastOutSlowInInterpolator()
                start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        val isExpanded = expandedPositions.contains(position)
        holder.bind(exercise, isExpanded)
    }

    override fun getItemCount() = exercises.size

    override fun onViewRecycled(holder: ExerciseViewHolder) {
        holder.itemView.clearAnimation()
        super.onViewRecycled(holder)
    }
}