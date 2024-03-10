package com.yapp.bol.presentation.view.home.rank.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.ItemRankPaddingBinding

class UserRankPaddingViewHolder(
    private val binding: ItemRankPaddingBinding,
    private val onGuestAddingButtonClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.btnGuestAdd.setOnClickListener { onGuestAddingButtonClicked() }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onGuestAddingButtonClicked: () -> Unit
        ): UserRankPaddingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_rank_padding, parent, false)
            val binding = ItemRankPaddingBinding.bind(view)
            return UserRankPaddingViewHolder(binding, onGuestAddingButtonClicked)
        }
    }
}
