package com.yapp.bol.domain.repository

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.GameItem
import com.yapp.bol.domain.model.GameSortType
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGameList(groupId: Int, sort: GameSortType = GameSortType.FIXED): Flow<ApiResult<List<GameItem>>>
}
