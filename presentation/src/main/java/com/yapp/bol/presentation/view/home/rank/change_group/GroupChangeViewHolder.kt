package com.yapp.bol.presentation.view.home.rank.change_group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.databinding.ItemChangeGroupBinding
import com.yapp.bol.presentation.model.JoinedGroupViewItem

class GroupChangeViewHolder(
    private val binding: ItemChangeGroupBinding,
    private val onClick: GroupChangeItemClickListener?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: JoinedGroupViewItem) {
        configureUI(item)
        handleEvent(item.joinedGroupItem.id)
    }

    private fun configureUI(item: JoinedGroupViewItem) {
        binding.tvGroupName.text = item.joinedGroupItem.name
        binding.viewSelectedGroup.isVisible = item.isCurrentGroup
        binding.ivCheck.isVisible = item.isCurrentGroup
        binding.root.isClickable = !(item.isCurrentGroup)
    }

    private fun handleEvent(groupId: Long) {
        binding.root.setOnClickListener {
            onClick?.onClick(groupId)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onClick: GroupChangeItemClickListener?): GroupChangeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChangeGroupBinding.inflate(inflater, parent, false)
            return GroupChangeViewHolder(binding, onClick)
        }
    }
}
