package com.yapp.bol.presentation.view.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.user.DeleteAccountItem
import com.yapp.bol.domain.model.user.UserItem
import com.yapp.bol.domain.usecase.auth.DeleteAccountUseCase
import com.yapp.bol.domain.usecase.auth.DeleteRefreshTokenUseCase
import com.yapp.bol.domain.usecase.group.GetJoinedGroupUseCase
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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
        deleteTokenUseCase.invoke()
        deleteAccountUseCase.invoke().map { deleteAccountItem ->
            when (deleteAccountItem) {
                is DeleteAccountItem.Success -> _userUiState.value = SettingUiState.Success
                is DeleteAccountItem.FailUnknownError -> _userUiState.value = SettingUiState.Loading
                is DeleteAccountItem.FailCauseOwner -> {
                    setupOwnerGroupInfo()
                }
            }
        }
    }

    private fun setupOwnerGroupInfo() {
        getJoinedGroupUseCase().map {
            checkedApiResult(
                apiResult = it,
                success = { list ->
                    // todo UI State에 적합한 값 넣어주기
                },
            )
        }
    }

}
