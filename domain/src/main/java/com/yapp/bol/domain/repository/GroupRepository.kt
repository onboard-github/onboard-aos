package com.yapp.bol.domain.repository

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.CheckGroupJoinByAccessCodeItem
import com.yapp.bol.domain.model.GroupDetailItem
import com.yapp.bol.domain.model.GroupMemberItem
import com.yapp.bol.domain.model.GroupSearchItem
import com.yapp.bol.domain.model.NewGroupItem
import com.yapp.bol.domain.model.RandomImageItem
import com.yapp.bol.domain.model.UserRankListItem
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun postCreateGroup(
        name: String,
        description: String,
        organization: String,
        imageUrl: String,
        uuid: String,
        nickname: String,
    ): Flow<ApiResult<NewGroupItem>>

    fun getUserRank(
        groupId: Int,
        gameId: Int,
    ): Flow<ApiResult<UserRankListItem>>

    fun getGroupDefaultImage(): Flow<ApiResult<RandomImageItem>>

    suspend fun searchGroup(
        name: String,
        page: Int,
        pageSize: Int,
    ): ApiResult<GroupSearchItem>

    fun getGroupDetail(groupId: Long): Flow<ApiResult<GroupDetailItem>>

    fun checkGroupJoinAccessCode(
        groupId: String,
        accessCode: String,
    ): Flow<ApiResult<CheckGroupJoinByAccessCodeItem>>

    fun patchGroupMemberNickname(
        groupId: Long,
        memberId: Long,
        newNickname: String,
    ): Flow<ApiResult<GroupMemberItem>>
}
