package com.yapp.bol.presentation.view.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.user.UserItem
import com.yapp.bol.domain.usecase.auth.DeleteAccountUseCase
import com.yapp.bol.domain.usecase.auth.DeleteRefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuitViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val deleteTokenUseCase: DeleteRefreshTokenUseCase,
) : ViewModel() {

    private val _userUiState = MutableStateFlow<SettingUiState<UserItem>>(SettingUiState.Loading)
    val userUiState: StateFlow<SettingUiState<UserItem>> = _userUiState

    fun deleteAccount() = viewModelScope.launch {
        deleteTokenUseCase.invoke()
    }

}
