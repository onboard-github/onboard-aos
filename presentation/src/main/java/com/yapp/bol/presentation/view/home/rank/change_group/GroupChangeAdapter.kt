package com.yapp.bol.presentation.view.home.rank.change_group

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.yapp.bol.presentation.model.JoinedGroupUiModel as Item

class GroupChangeAdapter : ListAdapter<Item, GroupChangeViewHolder>(diff) {

    private var onClick: GroupChangeItemClickListener? = null

    fun setOnGroupClickListener(onClick: GroupChangeItemClickListener?) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupChangeViewHolder.create(parent, onClick)

    override fun onBindViewHolder(holder: GroupChangeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.isCurrentGroup == newItem.isCurrentGroup &&
                    oldItem.joinedGroupItem.id == newItem.joinedGroupItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}
