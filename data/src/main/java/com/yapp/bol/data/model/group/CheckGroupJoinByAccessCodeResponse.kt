package com.yapp.bol.data.model.group

import com.google.gson.annotations.SerializedName

data class CheckGroupJoinByAccessCodeResponse(
    @SerializedName("result")
    val isNewMember: Boolean,
)
