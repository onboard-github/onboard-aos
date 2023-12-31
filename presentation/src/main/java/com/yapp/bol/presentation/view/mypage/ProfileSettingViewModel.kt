package com.yapp.bol.presentation.view.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.NicknameValidItem
import com.yapp.bol.domain.usecase.setting.SettingUseCase
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val settingUseCase: SettingUseCase
) : ViewModel() {

    private val _nicknameValidate = MutableLiveData<NicknameValidItem>()
    val nicknameValidate: LiveData<NicknameValidItem> = _nicknameValidate

    private val _newNickname = MutableLiveData<String>()
    val newNickname: LiveData<String> = _newNickname

    fun getValidateNickName(groupId: Int, nickname: String) {
        viewModelScope.launch {
            settingUseCase.getValidateNickName(groupId, nickname).collect {
                checkedApiResult(
                    apiResult = it,
                    success = { data ->
                        _nicknameValidate.value = data
                    },
                )
            }
        }
    }

    fun patchGroupMemberNicknameUseCase(groupId: Int, memberId: Int, nickname: String) {
        viewModelScope.launch {
            settingUseCase.patchGroupMemberNickname(groupId, memberId, nickname).collect {
                checkedApiResult(
                    apiResult = it,
                    success = { data ->
                        _newNickname.value = data.nickname
                    },
                )
            }
        }
    }
}
