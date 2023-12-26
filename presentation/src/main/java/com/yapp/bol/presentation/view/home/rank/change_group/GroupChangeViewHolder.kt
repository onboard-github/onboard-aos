package com.yapp.bol.presentation.view.home.rank.change_group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.databinding.ItemChangeGroupBinding
import com.yapp.bol.presentation.model.JoinedGroupUiModel

class GroupChangeViewHolder(
    private val binding: ItemChangeGroupBinding,
    private val onClick: GroupChangeItemClickListener?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: JoinedGroupUiModel) {
        configureUI(item)
        handleEvent(item.joinedGroupItem.id, item.isCurrentGroup)
    }

    private fun configureUI(item: JoinedGroupUiModel) {
        binding.tvGroupName.text = item.joinedGroupItem.name
        binding.viewSelectedGroup.isVisible = item.isCurrentGroup
        binding.ivCheck.isVisible = item.isCurrentGroup
    }

    private fun handleEvent(groupId: Long, isCurrentGroup: Boolean) {
        binding.root.setOnClickListener {
            if (!isCurrentGroup) { onClick?.onClick(groupId) }
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
