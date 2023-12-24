package com.yapp.bol.presentation.view.home.rank.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.ItemRankNoRankBinding

class UserNoRankItemViewHolder(
    private val binding: ItemRankNoRankBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): UserNoRankItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_rank_no_rank, parent, false)
            val binding = ItemRankNoRankBinding.bind(view)
            return UserNoRankItemViewHolder(binding)
        }
    }
}
