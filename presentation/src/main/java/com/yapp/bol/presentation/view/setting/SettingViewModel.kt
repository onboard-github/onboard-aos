package com.yapp.bol.presentation.view.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.usecase.login.GetMyInfoUseCase
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : ViewModel() {

    private var _id: Int = 0
    val id: Int
        get() = _id
    private var _nickname = ""
    val nickName: String
        get() = _nickname

    fun getUserInfo() {
        viewModelScope.launch {
            getMyInfoUseCase.invoke().collectLatest { apiResult ->
                checkedApiResult(
                    apiResult = apiResult,
                    success = { data ->
                        _id = data.id
                        _nickname = data.nickname
                    },
                )
            }
        }
    }
}
