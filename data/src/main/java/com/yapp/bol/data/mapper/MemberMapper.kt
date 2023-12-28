package com.yapp.bol.data.mapper

import com.yapp.bol.data.model.member.MatchCountInGroupResponse
import com.yapp.bol.data.model.member.MemberDTO
import com.yapp.bol.data.model.member.MemberListResponse
import com.yapp.bol.data.model.member.MemberValidApiResponse
import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.MatchCountInGroupItem
import com.yapp.bol.domain.model.MemberItem
import com.yapp.bol.domain.model.MemberItems
import com.yapp.bol.domain.model.NicknameValidItem

object MemberMapper {

    fun ApiResult<MemberValidApiResponse>.validToDomain(): ApiResult<NicknameValidItem> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(NicknameValidItem(data.isAvailable, data.reason))
            is ApiResult.Error -> ApiResult.Error(exception)
        }
    }

    fun ApiResult<MemberListResponse>.memberListToDomain(): ApiResult<MemberItems> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(
                MemberItems(
                    members = data.contents.map { it.toItem() },
                    cursor = data.cursor,
                    hasNext = data.hasNext
                )
            )

            is ApiResult.Error -> ApiResult.Error(exception)
        }
    }

    private fun MemberDTO.toItem(): MemberItem {
        return MemberItem(
            id = this.id,
            role = this.role,
            nickname = this.nickname,
            level = this.level
        )
    }

    fun ApiResult<MatchCountInGroupResponse>.matchCountToDomain(): ApiResult<MatchCountInGroupItem> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(MatchCountInGroupItem(data.matchCount))
            is ApiResult.Error -> ApiResult.Error(exception)
        }
    }
}
