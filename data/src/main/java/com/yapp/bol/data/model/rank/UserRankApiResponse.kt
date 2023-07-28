package com.yapp.bol.data.model.rank

import com.google.gson.annotations.SerializedName

data class UserRankApiResponse(
    val contents: List<UserRankDTO>,
)

data class UserRankDTO(
    val id: Long,
    val rank: Int?,
    @SerializedName("nickname")
    val name: String,
    @SerializedName("score")
    val score: Int?,
    @SerializedName("matchCount")
    val playCount: Int?,
    @SerializedName("isChangeRecent")
    val isChangeRecent: Boolean,
)