package com.yapp.bol.domain.repository

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.ErrorItem
import com.yapp.bol.domain.model.MatchCountInGroupItem
import com.yapp.bol.domain.model.MemberItems
import com.yapp.bol.domain.model.NicknameValidItem
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface MemberRepository {

    fun getValidateNickName(
        groupId: Int,
        nickname: String,
    ): Flow<ApiResult<NicknameValidItem>>

    fun getMemberList(
        groupId: Int,
        pageSize: Int,
        cursor: String?,
        nickname: String?,
    ): Flow<ApiResult<MemberItems>>

    suspend fun postGuestMember(groupId: Int, nickname: String)

    fun joinGroup(
        groupId: String,
        accessCode: String,
        nickname: String,
        guestId: Int?,
    ): Flow<ApiResult<ErrorItem>>

    fun getMatchCountInGroup(groupId: Long): Flow<ApiResult<MatchCountInGroupItem>>

    fun quitGroup(
        groupId: Long,
        nickname: String,
    ): Flow<ApiResult<ResponseBody>>
}
