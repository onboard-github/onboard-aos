package com.yapp.bol.presentation.view.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.usecase.login.GetMyInfoUseCase
import com.yapp.bol.domain.usecase.login.LoginUseCase
import com.yapp.bol.presentation.utils.Constant.EMPTY_STRING
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _nickname = MutableLiveData(EMPTY_STRING)
    val nickname: LiveData<String> = _nickname

    private val _isComplete = MutableLiveData(false)
    val isComplete: LiveData<Boolean> = _isComplete

    private var newNickname = EMPTY_STRING

    init {
        getMyInfoUseCase()
    }

    private fun getMyInfoUseCase() {
        viewModelScope.launch {
            getMyInfoUseCase.invoke().collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { data -> _nickname.value = data.nickname },
                )
            }
        }
    }

    fun putUserName() {
        viewModelScope.launch {
            loginUseCase.putUserName(newNickname)
            _isComplete.value = true
        }
    }

    fun updateNewNickname(value: String) {
        newNickname = value
    }
}
