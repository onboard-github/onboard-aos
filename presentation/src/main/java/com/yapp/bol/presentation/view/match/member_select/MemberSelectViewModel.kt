package com.yapp.bol.presentation.view.match.member_select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.usecase.login.GetMyInfoUseCase
import com.yapp.bol.domain.usecase.login.MatchUseCase
import com.yapp.bol.presentation.mapper.MatchMapper.toPresentation
import com.yapp.bol.presentation.model.MemberInfo
import com.yapp.bol.presentation.utils.checkedApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MemberSelectViewModel @Inject constructor(
    private val matchUseCase: MatchUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : ViewModel() {

    private val _members = MutableLiveData(listOf<MemberInfo>())
    val members: LiveData<List<MemberInfo>> = _members

    private val _players = MutableLiveData(listOf<MemberInfo>())
    val players: LiveData<List<MemberInfo>> = _players

    private val _isCompleteButtonEnabled = MutableLiveData(false)
    val isCompleteButtonEnabled: LiveData<Boolean> = _isCompleteButtonEnabled

    private val _isNickNameValidate = MutableLiveData(false)
    val isNickNameValidate: LiveData<Boolean> = _isNickNameValidate

    private val _playerState = MutableStateFlow<Boolean?>(null)
    val playerState = _playerState.asStateFlow()

    val dynamicPlayers = arrayListOf<MemberInfo>()
    private var maxPlayers = 0
    private var minPlayers = 0

    private var allMembers: List<MemberInfo> = listOf()
    private var loadingState = false
    private var groupId = 0
    private var cursor: String? = null
    private var hasNext = true
    var validReason: String? = null

    init {
        getMembers()
    }

    fun getMembers(nickname: String? = null) {
        if (hasNext.not() || loadingState) return
        loadingState = true
        viewModelScope.launch {
            val userNickname = withContext(Dispatchers.IO) { getMyInfo() }
            val memberList = withContext(Dispatchers.IO) { getMemberList(nickname) }.map {
                if (it.nickname == userNickname) it.copy(isMe = true) else it
            }
            allMembers = setMemberIsChecked(allMembers + memberList)
            updateMembers()
        }
    }

    fun addGuestMember(nickname: String) {
        viewModelScope.launch {
            postGuestMember(nickname)
        }
    }

    fun getValidateNickName(groupId: Int, nickname: String) {
        viewModelScope.launch {
            matchUseCase.getValidateNickName(groupId, nickname).collect {
                checkedApiResult(
                    apiResult = it,
                    success = { data ->
                        validReason = data.reason
                        _isNickNameValidate.value = data.isAvailable
                    },
                )
            }
        }
    }

    private suspend fun getMyInfo(): String {
        var nickname = ""
        matchUseCase.getUserInfo().collect {
            checkedApiResult(
                apiResult = it,
                success = { userItem -> nickname = userItem.nickname },
            )
        }

        return nickname
    }

    fun setMaxPlayers(count: Int) {
        maxPlayers = count
    }

    fun setMinPlayers(count: Int) {
        minPlayers = count
    }

    private suspend fun getMemberList(nickname: String? = null): List<MemberInfo> {
        var memberList = listOf<MemberInfo>()
        matchUseCase.getMemberList(groupId, 20, cursor, nickname).collectLatest {
            checkedApiResult(
                apiResult = it,
                success = { data ->
                    memberList = data.members.map { member -> member.toPresentation() }
                    cursor = data.cursor
                    hasNext = data.hasNext
                },
            )
        }
        return memberList
    }

    private suspend fun postGuestMember(nickname: String) {
        matchUseCase.postGuestMember(groupId, nickname)
        clearNextPage()
        getMembers()
    }

    fun updateMemberIsChecked(position: Int, isChecked: Boolean) {
        allMembers[position].isChecked = isChecked
    }

    fun checkedSelectMembers(memberInfo: MemberInfo) {
        val selectMember = dynamicPlayers.find { it.id == memberInfo.id }
        if (dynamicPlayers.contains(selectMember)) {
            dynamicPlayers.remove(selectMember)
        } else {
            dynamicPlayers.add(memberInfo)
        }
        _players.value = dynamicPlayers.toList()
        _playerState.value = players.value?.isEmpty()?.not()
        checkedCompleteButtonEnabled()
    }

    fun removeMember(memberId: Int) {
        allMembers = allMembers.map { if (it.id == memberId) it.copy(isChecked = false) else it }
        updateMembers()
    }

    fun clearNextPage() {
        allMembers = listOf()
        cursor = null
        hasNext = true
    }

    private fun updateMembers() {
        loadingState = false
        _members.value = allMembers.toList()
    }

    fun updateGroupId(id: Int) {
        groupId = id
    }

    private fun setMemberIsChecked(memberList: List<MemberInfo>): List<MemberInfo> {
        return memberList.map { memberInfo ->
            val isChecked = players.value?.any { it.id == memberInfo.id } ?: false
            if (isChecked) {
                memberInfo.copy(isChecked = true)
            } else {
                memberInfo
            }
        }
    }

    fun checkedMaxPlayers(): Boolean {
        return (players.value?.size ?: 0) < maxPlayers
    }

    private fun checkedCompleteButtonEnabled() {
        _isCompleteButtonEnabled.value = (players.value?.size ?: 0) >= minPlayers
    }
}
