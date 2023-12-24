package com.yapp.bol.presentation.model

import com.yapp.bol.domain.model.JoinedGroupItem

data class JoinedGroupUiModel(
    val joinedGroupItem: JoinedGroupItem,
    val isCurrentGroup: Boolean,
)
