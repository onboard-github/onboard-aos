package com.yapp.bol.presentation.model

import com.yapp.bol.domain.model.JoinedGroupItem

data class JoinedGroupViewItem(
    val joinedGroupItem: JoinedGroupItem,
    val isCurrentGroup: Boolean,
)
