package com.citrus.cookiteasy.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citrus.cookiteasy.data.database.CategoryItem
import com.citrus.cookiteasy.databinding.ItemCategoryBinding

class CategoryAdapter : ListAdapter<CategoryItem, CategoryViewHolder>(DiffUtilCallbackCategory()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            item?.let {
                categoryTitle.text = it.title
                categoryBackground.setImageResource(it.imageUrl)
            }
        }
    }


}

class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCallbackCategory : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
        oldItem == newItem

}