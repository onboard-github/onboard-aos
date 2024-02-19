package com.yapp.bol.presentation.mapper

import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.presentation.model.MyGroupUiModel

object GroupMapper {
    fun JoinedGroupItem.convertJoinedGroupItemToUiState(): MyGroupUiModel {
        return MyGroupUiModel(
            groupId = id.toInt(),
            groupName = name,
            nickname = nickname,
            organization = organization,
            matchCount = matchCount,
            memberId = memberId.toInt(),
        )
    }
}
