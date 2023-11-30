package com.yapp.bol.presentation.model

import com.yapp.bol.domain.model.GroupItem

sealed class GroupSearchUiModel {
    data class GroupList(val groupItem: GroupItem) : GroupSearchUiModel()
    data class GroupNotFound(val keyword: String) : GroupSearchUiModel()
}
