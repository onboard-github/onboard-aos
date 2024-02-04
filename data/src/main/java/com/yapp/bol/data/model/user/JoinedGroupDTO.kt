package com.yapp.bol.data.model.user

data class JoinedGroupDTO(
    val id: Long,
    val name: String,
    val description: String,
    val organization: String,
    val profileImageUrl: String,
    val memberId: Long,
    val nickname: String,
    val matchCount: Int,
)
