package com.yapp.bol.domain.model

@Deprecated("need to delete")
data class JoinedGroupV2Item(
    val groupId: Int,
    val groupName: String,
    val nickname: String,
    val organization: String,
    val matchCount: Int,
    val memberId: Int,
)
