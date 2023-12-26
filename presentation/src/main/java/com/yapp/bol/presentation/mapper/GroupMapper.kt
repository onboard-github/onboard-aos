package com.yapp.bol.presentation.mapper

import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.presentation.model.MyGroupUiModel

object GroupMapper {
    fun JoinedGroupItem.convertJoinedGroupItemToUiState(): MyGroupUiModel {
        return MyGroupUiModel(
            id = id,
            name = name,
            description = description,
            organization = organization,
        )
    }
}
