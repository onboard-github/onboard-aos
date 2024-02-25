package com.yapp.bol.domain.model

import com.yapp.bol.domain.model.core.Role

data class JoinedGroupItem(
    val id: Long,
    val name: String,
    val description: String,
    val organization: String,
    val profileImageUrl: String,
    val memberId: Long,
    val nickname: String,
    val matchCount: Int,
    val role: Role
)
