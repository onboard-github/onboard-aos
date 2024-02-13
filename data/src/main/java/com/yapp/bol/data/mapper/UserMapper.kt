package com.yapp.bol.data.mapper

import com.yapp.bol.data.model.user.JoinedGroupResponse
import com.yapp.bol.data.model.user.OnBoardResponse
import com.yapp.bol.data.model.user.TotalMatchCountResponse
import com.yapp.bol.data.model.user.UserResponse
import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.domain.model.OnBoardingItem
import com.yapp.bol.domain.model.user.TotalMatchCountItem
import com.yapp.bol.domain.model.user.UserItem

object UserMapper {

    fun ApiResult<JoinedGroupResponse>.toJoinedGroupItem(): ApiResult<List<JoinedGroupItem>> =
        when (this) {
            is ApiResult.Success -> ApiResult.Success(this.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(exception)
        }

    private fun JoinedGroupResponse.toDomain(): List<JoinedGroupItem> =
        this.contents.map { joinedGroupDTO ->
            JoinedGroupItem(
                id = joinedGroupDTO.id,
                name = joinedGroupDTO.name,
                description = joinedGroupDTO.description,
                organization = joinedGroupDTO.organization,
                profileImageUrl = joinedGroupDTO.profileImageUrl,
                memberId = joinedGroupDTO.memberId,
                nickname = joinedGroupDTO.nickname,
                matchCount = joinedGroupDTO.matchCount,
            )
        }

    fun ApiResult<UserResponse>.toUserDomain(): ApiResult<UserItem> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(UserItem(this.data.id, this.data.nickname))
            is ApiResult.Error -> ApiResult.Error(this.exception)
        }
    }

    fun ApiResult<OnBoardResponse>.toBoardDomain(): ApiResult<OnBoardingItem> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(OnBoardingItem(data.onboarding, data.mainGroupId))
            is ApiResult.Error -> ApiResult.Error(exception)
        }
    }

    fun ApiResult<TotalMatchCountResponse>.totalMatchCountToDomain(): ApiResult<TotalMatchCountItem> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(TotalMatchCountItem(this.data.matchCount))
            is ApiResult.Error -> ApiResult.Error(exception)
        }
    }
}
