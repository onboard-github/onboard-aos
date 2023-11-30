package com.yapp.bol.presentation.view.home.rank.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.domain.model.Role
import com.yapp.bol.domain.model.UserRankItem
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.databinding.ItemRankAfter4Binding
import com.yapp.bol.presentation.model.HomeUserRankItem
import com.yapp.bol.presentation.utils.Converter.convertPlayCount
import com.yapp.bol.presentation.utils.Converter.convertScore

class UserRankItemAfter4ViewHolder(
    private val binding: ItemRankAfter4Binding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            // TODO : MVP 개발 단계일지 모르지만 각 유저 클릭 시 액션
        }
    }

    fun bind(item: HomeUserRankItem) {
        binding.setItems(item.userRankItem, item.isMe)
    }

    private fun ItemRankAfter4Binding.setItems(
        userRankItem: UserRankItem,
        isMe: Boolean,
    ) {
        userRankItem.apply {
            if (rank != null) {
                tvRank.text = rank.toString()
            } else { tvRank.text = "-" }
            tvName.text = name
            tvPlayCount.text = playCount.convertPlayCount()
            tvWinRate.text = score.convertScore()
            ivRecentUser.visibility = if (isChangeRecent) {
                View.VISIBLE
            } else { View.GONE }
            if (role is Role.GUEST) {
                imgDice.visibility = View.INVISIBLE
                imgDiceGuest.visibility = View.VISIBLE
            } else {
                imgDice.visibility = View.VISIBLE
                imgDiceGuest.visibility = View.INVISIBLE
            }
        }

        if (isMe) {
            viewMe.root.visibility = View.VISIBLE
        } else {
            viewMe.root.visibility = View.GONE
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserRankItemAfter4ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_rank_after_4, parent, false)
            val binding = ItemRankAfter4Binding.bind(view)
            return UserRankItemAfter4ViewHolder(binding)
        }
    }
}
