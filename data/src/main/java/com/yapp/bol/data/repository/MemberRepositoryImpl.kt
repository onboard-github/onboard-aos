package com.yapp.bol.data.repository

import com.yapp.bol.data.datasource.member.MemberDataSource
import com.yapp.bol.data.mapper.CoreMapper.mapperToBaseItem
import com.yapp.bol.data.mapper.MemberMapper.matchCountToDomain
import com.yapp.bol.data.mapper.MemberMapper.memberListToDomain
import com.yapp.bol.data.mapper.MemberMapper.validToDomain
import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.ErrorItem
import com.yapp.bol.domain.model.MatchCountInGroupItem
import com.yapp.bol.domain.model.MemberItems
import com.yapp.bol.domain.model.NicknameValidItem
import com.yapp.bol.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: MemberDataSource,
) : MemberRepository {

    override fun getValidateNickName(groupId: Int, nickname: String): Flow<ApiResult<NicknameValidItem>> {
        return memberDataSource.getValidateNickName(groupId, nickname).map {
            it.validToDomain()
        }
    }

    override fun getMemberList(
        groupId: Int,
        pageSize: Int,
        cursor: String?,
        nickname: String?,
    ): Flow<ApiResult<MemberItems>> {
        return memberDataSource.getMemberList(groupId, pageSize, cursor, nickname).map {
            it.memberListToDomain()
        }
    }

    override suspend fun postGuestMember(groupId: Int, nickname: String) {
        memberDataSource.postGuestMember(groupId, nickname)
    }

    override fun joinGroup(
        groupId: String,
        accessCode: String,
        nickname: String,
        guestId: Int?,
    ): Flow<ApiResult<ErrorItem>> {
        return memberDataSource.joinGroup(groupId, accessCode, nickname, guestId).map { it.mapperToBaseItem() }
    }

    override fun getMatchCountInGroup(groupId: Long): Flow<ApiResult<MatchCountInGroupItem>> {
        return memberDataSource.getMatchCountInGroup(groupId).map { it.matchCountToDomain() }
    }

    override fun quitGroup(
        groupId: Long,
        nickname: String,
    ): Flow<ApiResult<ResponseBody>> {
        return memberDataSource.quitGroup(groupId, nickname)
    }
}
