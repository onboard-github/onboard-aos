package com.yapp.bol.presentation.view.setting.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.Role
import com.yapp.bol.domain.usecase.login.MatchUseCase
import com.yapp.bol.presentation.mapper.MatchMapper.toPresentation
import com.yapp.bol.presentation.model.MemberInfo
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeOwnerViewModel @Inject constructor(
    private val matchUseCase: MatchUseCase,
) : ViewModel() {

    private val _members = MutableLiveData<List<MemberInfo>?>(null)
    val members: LiveData<List<MemberInfo>?> = _members

    private var hasNext = true
    private var loadingState = false
    private var cursor: String? = null

    fun getMembers(nickname: String? = null, groupId: Int) {
        if (hasNext.not() || loadingState) return
        loadingState = true
        viewModelScope.launch {
            matchUseCase.getMemberList(groupId, 20, cursor, nickname).collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { data ->

                        _members.value = data.members
                            .filter { member -> member.role == Role.HOST.string }
                            .map { member -> member.toPresentation() }
                        cursor = data.cursor
                        hasNext = data.hasNext
                        loadingState = false
                    },
                )
            }
        }
    }

    fun updateMember(id: Int, isSelected: Boolean) {
        val newMembers = members.value?.map { member ->
            if (member.id == id) {
                member.copy(isChecked = isSelected)
            } else {
                member.copy(isChecked = false)
            }
        }

        _members.value = newMembers
    }

    fun clearNextPage() {
        cursor = null
        hasNext = true
    }
}
