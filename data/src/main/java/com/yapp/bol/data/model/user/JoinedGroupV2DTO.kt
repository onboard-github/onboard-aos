package com.yapp.bol.data.model.user

data class JoinedGroupV2DTO(
    val groupId: Int,
    val groupName: String,
    val nickname: String,
    val organization: String,
    val matchCount: Int,
    val memberId: Int,
)
