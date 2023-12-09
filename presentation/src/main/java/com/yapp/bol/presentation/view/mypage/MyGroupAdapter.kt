package com.yapp.bol.presentation.view.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.yapp.bol.presentation.databinding.ItemMypageGroupBinding
import com.yapp.bol.presentation.model.MyGroupUiModel

class MyGroupAdapter : ListAdapter<MyGroupUiModel, MyGroupAdapter.MyGroupViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        val binding = ItemMypageGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class MyGroupViewHolder(
        private val binding: ItemMypageGroupBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyGroupUiModel) {
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<MyGroupUiModel>() {
            override fun areItemsTheSame(oldItem: MyGroupUiModel, newItem: MyGroupUiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyGroupUiModel, newItem: MyGroupUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
