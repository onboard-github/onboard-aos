package com.yapp.bol.presentation.model

sealed class GroupQuitUiModel {
    object Success : GroupQuitUiModel()
    object Loading : GroupQuitUiModel()
    object FailCauseOwner : GroupQuitUiModel()
    object FailCauseOnlyOneMember : GroupQuitUiModel()
    object FailUnknownError : GroupQuitUiModel()
}
