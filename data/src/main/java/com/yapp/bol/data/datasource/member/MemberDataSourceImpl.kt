package com.yapp.bol.data.datasource.member

import com.yapp.bol.data.model.base.ErrorResponse
import com.yapp.bol.data.model.member.GroupQuitRequest
import com.yapp.bol.data.model.member.GuestAddApiRequest
import com.yapp.bol.data.model.member.JoinGroupApiRequest
import com.yapp.bol.data.model.member.MatchCountInGroupResponse
import com.yapp.bol.data.model.member.MemberListResponse
import com.yapp.bol.data.model.member.MemberValidApiResponse
import com.yapp.bol.data.remote.MemberApi
import com.yapp.bol.domain.handle.BaseRepository
import com.yapp.bol.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi,
) : BaseRepository(), MemberDataSource {

    override fun getValidateNickName(
        groupId: Int,
        nickname: String,
    ): Flow<ApiResult<MemberValidApiResponse>> = flow {
        val result = safeApiCall { memberApi.getValidateNickName(groupId, nickname) }
        emit(result)
    }

    override fun getMemberList(
        groupId: Int,
        pageSize: Int,
        cursor: String?,
        nickname: String?,
    ): Flow<ApiResult<MemberListResponse>> = flow {
        val result = safeApiCall { memberApi.getMemberList(groupId, pageSize, cursor, nickname) }
        emit(result)
    }

    override suspend fun postGuestMember(groupId: Int, nickname: String) {
        memberApi.postGuestMember(groupId, GuestAddApiRequest(nickname))
    }

    override fun joinGroup(
        groupId: String,
        accessCode: String,
        nickname: String,
        guestId: Int?,
    ): Flow<ApiResult<ErrorResponse>> = flow {
        val result = safeApiCall { memberApi.joinGroup(groupId, JoinGroupApiRequest(nickname, accessCode, guestId)) }
        emit(result)
    }

    override fun getMatchCountInGroup(groupId: Long): Flow<ApiResult<MatchCountInGroupResponse>> = flow {
        emit(safeApiCall { memberApi.getMatchCountInGroup(groupId) })
    }

    override fun quitGroup(
        groupId: Long,
        nickname: String,
    ): Flow<ApiResult<ResponseBody>> = flow {
        val result = safeApiCall { memberApi.quitGroup(groupId, GroupQuitRequest(nickname)) }
        emit(result)
    }
}
