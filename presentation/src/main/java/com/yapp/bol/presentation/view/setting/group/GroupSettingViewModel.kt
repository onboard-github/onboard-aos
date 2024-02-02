package com.yapp.bol.presentation.view.setting.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.usecase.group.DeleteGroupUseCase
import com.yapp.bol.presentation.utils.Constant.EMPTY_STRING
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupSettingViewModel @Inject constructor(
    private val deleteGroupUseCase: DeleteGroupUseCase,
) : ViewModel() {
    private var groupId: Long = NEED_TO_SET
    var groupName: String = EMPTY_STRING
        private set

    private val _isGroupDeleted = MutableLiveData<Boolean?>(null)
    val isGroupDeleted: LiveData<Boolean?> = _isGroupDeleted

    fun setGroupId(groupId: Long) {
        this.groupId = groupId
    }

    fun setGroupName(groupName: String) {
        this.groupName = groupName
    }

    companion object {
        private const val NEED_TO_SET = -1L
    }

    fun deleteGroup() {
        viewModelScope.launch {
            deleteGroupUseCase(groupId.toString()).collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { _ -> _isGroupDeleted.value = true },
                    error = { _ -> _isGroupDeleted.value = false },
                )
            }
        }
    }
}
