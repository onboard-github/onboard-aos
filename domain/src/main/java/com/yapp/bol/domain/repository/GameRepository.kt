package com.yapp.bol.domain.repository

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.GameItem
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGameList(groupId: Int, sort: GameSortType = GameSortType.FIXED): Flow<ApiResult<List<GameItem>>>
}

enum class GameSortType(val value: String) {
    MATCH_COUNT("MATCH_COUNT"),
    FIXED("FIXED"),
}
