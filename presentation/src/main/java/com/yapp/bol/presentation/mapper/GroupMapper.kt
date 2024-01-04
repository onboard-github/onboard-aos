package com.yapp.bol.presentation.mapper

import com.yapp.bol.domain.model.JoinedGroupV2Item
import com.yapp.bol.presentation.model.MyGroupUiModel

object GroupMapper {
    fun JoinedGroupV2Item.convertJoinedGroupItemToUiState(): MyGroupUiModel {
        return MyGroupUiModel(
            groupId = groupId,
            groupName = groupName,
            nickname = nickname,
            organization = organization,
            matchCount = matchCount,
            memberId = memberId,
        )
    }
}
