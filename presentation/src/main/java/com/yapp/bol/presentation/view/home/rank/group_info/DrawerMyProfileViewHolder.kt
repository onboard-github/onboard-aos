package com.yapp.bol.presentation.view.home.rank.group_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.domain.model.UserRankItem
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.ViewHomeDrawerUserProfileBinding
import com.yapp.bol.presentation.utils.Converter.convertPlayCount

class DrawerMyProfileViewHolder(
    private val binding: ViewHomeDrawerUserProfileBinding,
    private val onClick: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userRankItem: UserRankItem) {
        binding.tvName.text = userRankItem.name
        binding.tvPlayCount.text = userRankItem.playCount.convertPlayCount()
        binding.btnEdit.setOnClickListener {
            onClick(userRankItem.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClick: (Long) -> Unit
        ): DrawerMyProfileViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.view_home_drawer_user_profile, parent, false)
            val binding = ViewHomeDrawerUserProfileBinding.bind(view)
            return DrawerMyProfileViewHolder(binding, onClick)
        }
    }
}
