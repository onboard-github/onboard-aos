package com.yapp.bol.domain.model.user

sealed class GroupQuitItem() {
    object Success : GroupQuitItem()
    object FailCauseOwner : GroupQuitItem()
    object FailCauseOnlyOneMember : GroupQuitItem()
    object FailUnknownError : GroupQuitItem()
}
