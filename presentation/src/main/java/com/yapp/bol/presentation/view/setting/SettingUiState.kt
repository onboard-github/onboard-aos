package com.yapp.bol.presentation.view.setting

sealed class SettingUiState<out T>(val _data: T?) {
    object Loading : SettingUiState<Nothing>(_data = null)
    data class FailCauseOwner(val groupId: Long, val groupName: String) : SettingUiState<Nothing>(_data = null)
    data class UnknownError(val error: Throwable) : SettingUiState<Nothing>(_data = null)
    object Success : SettingUiState<Nothing>(_data = null)
}
