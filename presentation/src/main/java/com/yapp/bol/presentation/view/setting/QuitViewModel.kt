package com.yapp.bol.presentation.view.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.core.Role
import com.yapp.bol.domain.model.user.DeleteAccountItem
import com.yapp.bol.domain.model.user.UserItem
import com.yapp.bol.domain.usecase.auth.DeleteAccountUseCase
import com.yapp.bol.domain.usecase.auth.DeleteRefreshTokenUseCase
import com.yapp.bol.domain.usecase.group.GetJoinedGroupUseCase
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuitViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val deleteTokenUseCase: DeleteRefreshTokenUseCase,
    private val getJoinedGroupUseCase: GetJoinedGroupUseCase,
) : ViewModel() {

    private val _userUiState = MutableStateFlow<SettingUiState<UserItem>>(SettingUiState.Loading)
    val userUiState: StateFlow<SettingUiState<UserItem>> = _userUiState

    fun deleteAccount() = viewModelScope.launch {
        _userUiState.value = SettingUiState.Loading
        deleteAccountUseCase.invoke().collectLatest { deleteAccountItem ->
            when (deleteAccountItem) {
                is DeleteAccountItem.Success -> {
                    deleteTokenUseCase.invoke()
                    _userUiState.value = SettingUiState.Success
                }
                is DeleteAccountItem.FailUnknownError -> {
                    _userUiState.value = SettingUiState.UnknownError(IllegalStateException())
                }
                is DeleteAccountItem.FailCauseOwner -> {
                    setupOwnerGroupInfo()
                }
            }
        }
    }

    private suspend fun setupOwnerGroupInfo() {
        getJoinedGroupUseCase().collectLatest {
            checkedApiResult(
                apiResult = it,
                success = { joinedList ->
                    joinedList
                        .first { group -> group.role is Role.OWNER }
                        .apply {
                            _userUiState.value = SettingUiState.FailCauseOwner(id, name)
                        }
                },
            )
        }
    }

}
