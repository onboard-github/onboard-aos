package com.yapp.bol.presentation.view.setting.group

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupSettingViewModel @Inject constructor() : ViewModel() {
    var groupId: Long = NEED_TO_SET

    companion object {
        private const val NEED_TO_SET = -1L
    }
}
