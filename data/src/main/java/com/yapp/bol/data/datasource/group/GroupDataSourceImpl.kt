package com.yapp.bol.data.datasource.group

import com.yapp.bol.data.model.base.ErrorResponse
import com.yapp.bol.data.model.group.CheckGroupJonByAccessCodeRequest
import com.yapp.bol.data.model.group.NewGroupApiRequest
import com.yapp.bol.data.model.group.CheckGroupJoinByAccessCodeResponse
import com.yapp.bol.data.model.group.GroupDetailResponse
import com.yapp.bol.data.model.group.GroupSearchApiResponse
import com.yapp.bol.data.model.group.NewGroupApiResponse
import com.yapp.bol.data.model.group.RandomImageResponse
import com.yapp.bol.data.model.group.UserRankApiResponse
import com.yapp.bol.data.remote.GroupApi
import com.yapp.bol.domain.handle.BaseRepository
import com.yapp.bol.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GroupDataSourceImpl @Inject constructor(
    private val groupApi: GroupApi,
) : BaseRepository(), GroupDataSource {

    override fun postCreateGroup(
        name: String,
        description: String,
        organization: String,
        imageUrl: String,
        uuid: String,
        nickname: String,
    ): Flow<ApiResult<NewGroupApiResponse>> = flow {
        val result = safeApiCall {
            groupApi.postCreateGroup(NewGroupApiRequest(name, description, organization, imageUrl, uuid, nickname))
        }
        emit(result)
    }

    override suspend fun searchGroup(name: String, page: Int, pageSize: Int):
        ApiResult<GroupSearchApiResponse> {
        return safeApiCall {
            groupApi.getGroupSearchResult(
                name = name,
                page = page.toString(),
                pageSize = pageSize.toString(),
            )
        }
    }

    override fun getGroupDefaultImage(): Flow<ApiResult<RandomImageResponse>> = flow {
        val result = safeApiCall { groupApi.getGroupDefaultImage() }
        emit(result)
    }

    override fun getGroupDetail(groupId: Long): Flow<ApiResult<GroupDetailResponse>> =
        flow {
            safeApiCall {
                groupApi.getGroupDetail(groupId)
            }.also { emit(it) }
        }

    override fun getUserRank(groupId: Int, gameId: Int): Flow<ApiResult<UserRankApiResponse>> =
        flow {
            safeApiCall {
                groupApi.getUserRank(
                    groupId = groupId,
                    gameId = gameId,
                )
            }.also { emit(it) }
        }

    override fun checkGroupJoinAccessCode(
        groupId: String,
        accessCode: String,
    ): Flow<ApiResult<CheckGroupJoinByAccessCodeResponse>> {
        return flow {
            val result = safeApiCall {
                groupApi.checkGroupJoinAccessCode(groupId, CheckGroupJonByAccessCodeRequest(accessCode))
            }
            emit(result)
        }
    }

    override fun updateOwner(groupId: Int, memberId: Int): Flow<ApiResult<ErrorResponse>> = flow {
        val result = safeApiCall { groupApi.updateOwner(groupId = groupId.toString(), memberId = memberId.toString()) }
        emit(result)
    }
}
