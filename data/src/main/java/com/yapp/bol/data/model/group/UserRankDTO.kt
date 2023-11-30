package com.yapp.bol.data.model.group

import com.google.gson.annotations.SerializedName

data class UserRankDTO(
    val id: Long,
    val memberId: Long,
    val userId: Long,
    val rank: Int?,
    @SerializedName("nickname")
    val name: String,
    @SerializedName("score")
    val score: Int?,
    @SerializedName("matchCount")
    val playCount: Int?,
    @SerializedName("isChangeRecent")
    val isChangeRecent: Boolean,
    val role: String,
)
