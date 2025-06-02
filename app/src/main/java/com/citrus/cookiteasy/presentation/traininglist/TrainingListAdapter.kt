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
import com.google.android.material.progressindicator.CircularProgressIndicator

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
        holder.descriptionTV.text = truncateTextWithSentences(train.description ?: "")
        holder.ratingText.text = train.difficulty.toString()


        when(train.difficulty) {
            1,2 -> holder.progressCircular.setIndicatorColor(ColorsFromRes.green)
            3,4 -> holder.progressCircular.setIndicatorColor(ColorsFromRes.yellow)
            else -> holder.progressCircular.setIndicatorColor(ColorsFromRes.red)
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

    fun truncateTextWithSentences(text: String, maxLength: Int = 110): String {
        if (text.length <= maxLength) return text

        // Берем первые maxLength символов
        val truncated = text.take(maxLength)

        // Находим индексы всех точек
        val dotIndices = truncated.mapIndexedNotNull { index, c ->
            index.takeIf { c == '.' }
        }

        return when {
            // Если есть точки - берем до последней полной точки
            dotIndices.isNotEmpty() -> truncated.substring(0, dotIndices.last() + 1)
            // Если нет точек - обрезаем с многоточием
            else -> text.take(maxLength - 3) + "..."
        }
    }


    class TrainListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.train_title)
        val descriptionTV: TextView = itemView.findViewById(R.id.train_description_text_view)
        val ratingText : TextView = itemView.findViewById(R.id.ratingText)
        val progressCircular : CircularProgressIndicator = itemView.findViewById(R.id.progress_circular)

    }
}