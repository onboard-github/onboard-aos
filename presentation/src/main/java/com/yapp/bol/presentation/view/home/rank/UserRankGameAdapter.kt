package com.yapp.bol.presentation.view.home.rank

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.yapp.bol.domain.model.GameItem

// todo : 중복 코드
// 작업 둘 다 멈췄을 때 삭제 필요
class UserRankGameAdapter : ListAdapter<GameItem, UserRankGameViewHolder>(diff) {

    private var selectedPosition: Int? = null
    private lateinit var onClick: (Int) -> Unit

    fun setOnClickListener(onClick: (Int) -> Unit) {
        this.onClick = onClick
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankGameViewHolder =
        UserRankGameViewHolder.create(parent, onClick)

    override fun onBindViewHolder(holder: UserRankGameViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position == selectedPosition)
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<GameItem>() {
            override fun areItemsTheSame(oldItem: GameItem, newItem: GameItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GameItem, newItem: GameItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
