package com.yapp.bol.presentation.view.group.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yapp.bol.domain.model.GroupItem
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.ItemGroupListBinding

class GroupListViewHolder(
    private val binding: ItemGroupListBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val glide by lazy { Glide.with(binding.root) }

    fun bind(groupItem: GroupItem, showJoinGroupDialog: (GroupItem) -> Unit) {
        binding.apply {
            tvGroupDescription.text = groupItem.description
            tvGroupName.text = groupItem.name
            tvGroupOrganization.text = groupItem.organization
            "${groupItem.memberCount}명".also { tvGroupSize.text = it }
            ivGroupImage.setImageWithGlide(groupItem.profileImageUrl)

            if (groupItem.organization.isEmpty()) { viewSeparator.visibility = View.GONE }
            else { viewSeparator.visibility = View.VISIBLE }

            binding.root.setOnClickListener {
                showJoinGroupDialog(groupItem)
            }
        }
    }

    // TODO : merge 후 util 함수로 교체
    private fun ImageView.setImageWithGlide(uri: String) {
        glide
            .load(uri)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimension(R.dimen.group_search_item_image_radius).toInt()),
            )
            .into(this)
    }

    companion object {
        fun create(parent: ViewGroup): GroupListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemGroupListBinding.inflate(inflater, parent, false)
            return GroupListViewHolder(binding)
        }
    }
}
