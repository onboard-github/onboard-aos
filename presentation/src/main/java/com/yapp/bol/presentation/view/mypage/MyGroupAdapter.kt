package com.yapp.bol.presentation.view.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.R

import com.yapp.bol.presentation.databinding.ItemMypageGroupBinding
import com.yapp.bol.presentation.model.MyGroupUiModel

class MyGroupAdapter(
    private val moveProfileSetting: (groupId: Int, groupName: String, nickname: String, memberId: Int) -> Unit,
) : ListAdapter<MyGroupUiModel, MyGroupAdapter.MyGroupViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupViewHolder {
        val binding = ItemMypageGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyGroupViewHolder(binding, moveProfileSetting)
    }

    override fun onBindViewHolder(holder: MyGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyGroupViewHolder(
        private val binding: ItemMypageGroupBinding,
        private val moveProfileSetting: (groupId: Int, groupName: String, nickname: String, memberId: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyGroupUiModel) = with(binding) {
            tvGroupName.text = item.groupName
            tvNickname.text = item.nickname
            tvGroupOrganization.text = item.organization
            tvMatchCount.text = String.format(root.resources.getString(R.string.group_match_count), item.matchCount)
            ivModify.setOnClickListener {
                moveProfileSetting(item.groupId, item.groupName, item.nickname, item.memberId)
            }
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<MyGroupUiModel>() {
            override fun areItemsTheSame(oldItem: MyGroupUiModel, newItem: MyGroupUiModel): Boolean {
                return oldItem.groupId == newItem.groupId
            }

            override fun areContentsTheSame(oldItem: MyGroupUiModel, newItem: MyGroupUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
