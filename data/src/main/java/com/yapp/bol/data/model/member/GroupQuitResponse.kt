package com.yapp.bol.data.model.member

sealed class GroupQuitResponse(val code: String?) {
    object Success : GroupQuitResponse(null)
    object FailCauseOwner : GroupQuitResponse(code = "Member008")
    object FailCauseOnlyOneMember : GroupQuitResponse(code = "Member009")
}
