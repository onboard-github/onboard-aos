package com.yapp.bol.presentation.view.home.rank.group_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.ViewHomeDrawerUserProfileBinding
import com.yapp.bol.presentation.utils.Converter.convertPlayCount

class DrawerMyProfileViewHolder(
    private val binding: ViewHomeDrawerUserProfileBinding,
    private val onClick: (Long, String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(myProfileInfo: JoinedGroupItem) {
        binding.tvName.text = myProfileInfo.nickname
        binding.tvPlayCount.text = myProfileInfo.matchCount.convertPlayCount()
        binding.btnEdit.setOnClickListener {
            onClick(myProfileInfo.memberId, myProfileInfo.nickname)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClick: (Long, String) -> Unit
        ): DrawerMyProfileViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.view_home_drawer_user_profile, parent, false)
            val binding = ViewHomeDrawerUserProfileBinding.bind(view)
            return DrawerMyProfileViewHolder(binding, onClick)
        }
    }
}
