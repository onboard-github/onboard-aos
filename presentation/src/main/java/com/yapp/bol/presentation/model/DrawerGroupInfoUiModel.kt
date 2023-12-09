package com.yapp.bol.presentation.model

import com.yapp.bol.domain.model.GroupDetailItem
import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.domain.model.UserRankItem

sealed class DrawerGroupInfoUiModel {
    data class CurrentGroupInfo(val groupDetailItem: GroupDetailItem) : DrawerGroupInfoUiModel()
    data class MyProfileInfo(val userRankItem: UserRankItem) : DrawerGroupInfoUiModel()
}
