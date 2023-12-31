package com.yapp.bol.presentation.view.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.usecase.group.GetJoinedGroupUseCase
import com.yapp.bol.domain.usecase.login.GetMyInfoUseCase
import com.yapp.bol.presentation.mapper.GroupMapper.convertJoinedGroupItemToUiState
import com.yapp.bol.presentation.model.MyGroupUiModel
import com.yapp.bol.presentation.utils.Constant.EMPTY_STRING
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getJoinedGroupUseCase: GetJoinedGroupUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : ViewModel() {

    private val _joinedGroups = MutableLiveData<List<MyGroupUiModel>>(listOf())
    val joinedGroups: LiveData<List<MyGroupUiModel>> = _joinedGroups

    private val _userName = MutableLiveData(EMPTY_STRING)
    val userName: LiveData<String> = _userName

    private val _matchTotalCount = MutableLiveData<Long>()
    val matchTotalCount: LiveData<Long> = _matchTotalCount

    init {
        getJoinedGroup()
        getMyInfo()
        getMyTotalMatchCount()
    }

    fun getJoinedGroup() {
        viewModelScope.launch {
            getJoinedGroupUseCase.fetchV2().collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { data ->
                        _joinedGroups.value = data.map { item -> item.convertJoinedGroupItemToUiState() }
                    },
                )
            }
        }
    }

    fun getMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase().collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { data -> _userName.value = data.nickname },
                )
            }
        }
    }

    fun getMyTotalMatchCount() {
        viewModelScope.launch {
            getMyInfoUseCase.getMyTotalMatchCount().collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { data -> _matchTotalCount.value = data.matchCount },
                )
            }
        }
    }
}
