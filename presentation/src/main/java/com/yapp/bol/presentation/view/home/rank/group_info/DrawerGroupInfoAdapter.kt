package com.yapp.bol.presentation.view.home.rank.group_info

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.model.DrawerGroupInfoUiModel

class DrawerGroupInfoAdapter(
    private val userProfileEditOnClick: (Long) -> Unit,
    private val copyButtonOnClick: (String) -> Unit,
) : ListAdapter<DrawerGroupInfoUiModel, RecyclerView.ViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == R.layout.item_group_info_detail) {
            DrawerCurrentGroupInfoViewHolder.create(parent, copyButtonOnClick)
        } else {
            DrawerMyProfileViewHolder.create(parent, userProfileEditOnClick)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel?.let {
            when (uiModel) {
                is DrawerGroupInfoUiModel.CurrentGroupInfo ->
                    (holder as DrawerCurrentGroupInfoViewHolder).bind(uiModel.groupDetailItem)
                is DrawerGroupInfoUiModel.MyProfileInfo ->
                    (holder as DrawerMyProfileViewHolder).bind(uiModel.userRankItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DrawerGroupInfoUiModel.CurrentGroupInfo -> R.layout.item_group_info_detail
            is DrawerGroupInfoUiModel.MyProfileInfo -> R.layout.view_home_drawer_user_profile
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<DrawerGroupInfoUiModel>() {
            override fun areItemsTheSame(oldItem: DrawerGroupInfoUiModel, newItem: DrawerGroupInfoUiModel): Boolean {
                return when (oldItem) {
                    is DrawerGroupInfoUiModel.CurrentGroupInfo -> {
                        newItem is DrawerGroupInfoUiModel.CurrentGroupInfo &&
                            oldItem.groupDetailItem.id == newItem.groupDetailItem.id
                    }

                    is DrawerGroupInfoUiModel.MyProfileInfo -> {
                        newItem is DrawerGroupInfoUiModel.MyProfileInfo &&
                            oldItem.userRankItem.id == newItem.userRankItem.id
                    }
                }
            }

            override fun areContentsTheSame(oldItem: DrawerGroupInfoUiModel, newItem: DrawerGroupInfoUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
