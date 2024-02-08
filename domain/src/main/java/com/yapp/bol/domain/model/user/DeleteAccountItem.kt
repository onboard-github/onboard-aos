package com.yapp.bol.domain.model.user

sealed class DeleteAccountItem() {
    object Success : DeleteAccountItem()
    object FailCauseOwner : DeleteAccountItem()
    object FailUnknownError : DeleteAccountItem()
}
