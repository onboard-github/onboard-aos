package com.yapp.bol.data.model.user

sealed class DeleteAccountResponse(val code: String?) {
    object Success : DeleteAccountResponse(null)
    object FailCauseOwner : DeleteAccountResponse(code = "User002")
}
