package com.yapp.bol.data.datasource.member

import com.yapp.bol.data.model.base.ErrorResponse
import com.yapp.bol.data.model.member.MatchCountInGroupResponse
import com.yapp.bol.data.model.member.MemberListResponse
import com.yapp.bol.data.model.member.MemberValidApiResponse
import com.yapp.bol.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface MemberDataSource {

    fun getValidateNickName(
        groupId: Int,
        nickname: String,
    ): Flow<ApiResult<MemberValidApiResponse>>

    fun getMemberList(
        groupId: Int,
        pageSize: Int,
        cursor: String? = null,
        nickname: String? = null,
    ): Flow<ApiResult<MemberListResponse>>

    suspend fun postGuestMember(groupId: Int, nickname: String)

    fun joinGroup(
        groupId: String,
        accessCode: String,
        nickname: String,
        guestId: Int?,
    ): Flow<ApiResult<ErrorResponse>>

    fun getMatchCountInGroup(groupId: Long): Flow<ApiResult<MatchCountInGroupResponse>>

    fun quitGroup(
        groupId: Long,
        nickname: String,
    ): Flow<ApiResult<ResponseBody>>
}
