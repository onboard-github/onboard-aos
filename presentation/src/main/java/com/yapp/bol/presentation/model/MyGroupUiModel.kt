package com.yapp.bol.presentation.model

data class MyGroupUiModel(
    val groupId: Int,
    val groupName: String,
    val nickname: String,
    val organization: String,
    val matchCount: Int,
    val memberId: Int,
)
