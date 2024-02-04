package com.yapp.bol.presentation.mapper

import com.yapp.bol.domain.model.GameItem
import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.domain.model.UserRankItem
import com.yapp.bol.domain.model.UserRankListItem
import com.yapp.bol.presentation.model.DrawerGroupInfoUiModel
import com.yapp.bol.presentation.model.GameItemWithSelected
import com.yapp.bol.presentation.model.HomeGameItemUiModel
import com.yapp.bol.presentation.model.HomeUserRankItem
import com.yapp.bol.presentation.model.JoinedGroupUiModel
import com.yapp.bol.presentation.model.UserRankUiModel
import com.yapp.bol.presentation.utils.config.HomeConfig

object HomeMapper {

    fun UserRankListItem.toUserRankUiModel(myId: Long): List<UserRankUiModel> {
        val userRank1To3 = mutableListOf<HomeUserRankItem>()
        val userRankAfter4 = mutableListOf<UserRankUiModel>()
        val resultList = mutableListOf<UserRankUiModel>()

        // 멤버가 그룹장 자신 혼자인 경우 - 초대 UI를 위한 empty list return
        if (userRankItemList.size <= HomeConfig.NO_RANK_THRESHOLD) { return resultList }

        // 랭킹 없는 멤버들만 있는 경우 (size는 반드시 1 초과)
        if (userRankItemList.first().rank == null) {
            resultList.add(UserRankUiModel.UserRankNoRank)
            resultList.addAll(
                userRankItemList.map {
                    UserRankUiModel.UserRankAfter4(HomeUserRankItem(it, myId == it.userId))
                }
            )
            resultList.add(UserRankUiModel.UserRankPadding)
            return resultList
        }

        userRankItemList.forEach { userRankItem ->
            userRankItem.rank?.let {
                if (it <= 3) {
                    userRank1To3.add(HomeUserRankItem(userRankItem, myId == userRankItem.userId))
                } else {
                    userRankAfter4.add(
                        UserRankUiModel.UserRankAfter4(HomeUserRankItem(userRankItem, myId == userRankItem.userId))
                    )
                }
            } ?: kotlin.run {
                userRankAfter4.add(
                    UserRankUiModel.UserRankAfter4(HomeUserRankItem(userRankItem, myId == userRankItem.userId))
                )
            }
        }

        resultList.add(UserRankUiModel.UserRank1to3(userRank1To3))
        resultList.addAll(userRankAfter4)
        if (resultList.isNotEmpty()) {
            resultList.add(UserRankUiModel.UserRankPadding)
        }

        return resultList
    }

    fun UserRankListItem.getMyInfo(myId: Long): UserRankItem? {
        return userRankItemList.find {
            it.userId == myId
        }
    }

    fun List<GameItem>.toHomeGameItemUiModelList(): List<HomeGameItemUiModel> {
        val resultList: MutableList<HomeGameItemUiModel> = mutableListOf()

        resultList.add(HomeGameItemUiModel.Padding)
        this.map { resultList.add(HomeGameItemUiModel.GameItem(GameItemWithSelected(it, false))) }
        resultList.add(HomeGameItemUiModel.Padding)

        return resultList
    }

    fun List<JoinedGroupItem>.toOtherGroupInfo(currentGroupId: Long): List<JoinedGroupUiModel> {
        return this.map {
            JoinedGroupUiModel(
                joinedGroupItem = it,
                isCurrentGroup = it.id == currentGroupId
            )
        }
    }

    fun List<JoinedGroupItem>.toMyGroupProfileInfo(currentGroupId: Long): DrawerGroupInfoUiModel? {
        return this.find { it.id == currentGroupId }?.let { DrawerGroupInfoUiModel.MyProfileInfo(it) }
    }
}
